package com.example.weatherapp.favorite.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.data.local.FavModel
import com.example.weatherapp.databinding.ActivityMapsBinding
import com.example.weatherapp.favorite.viewModel.FavViewModelFactory
import com.example.weatherapp.favorite.viewModel.FavoriteViewModel
import com.example.weatherapp.utils.Common
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import io.paperdb.Paper
import java.io.IOException
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, PermissionListener, GoogleMap.OnMapClickListener {

    private val viewModel:FavoriteViewModel by viewModels {
        FavViewModelFactory(this)
    }

    companion object {
        const val REQUEST_CHECK_SETTINGS = 43
    }
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    private lateinit var binding: ActivityMapsBinding

    private lateinit var googleMap: GoogleMap
    private lateinit var location:LatLng


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.choose_location)

        val fromSplash = intent.getStringExtra("fromSplash")

        if(fromSplash != null) {
            binding.btnAdd.text = getString(R.string.add_this_location)
        } else {
            binding.btnAdd.text = getString(R.string.add_to_favorite)
        }

        viewModel.initDatabase()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        binding.btnAdd.setOnClickListener {

            if(fromSplash == null) {
                val model = FavModel(
                    0,
                    binding.txtCountry.text.toString(),
                    binding.txtLoyality.text.toString(),
                    location.longitude,
                    location.latitude
                )

                setToFavorite(model)
                finish()

            } else {
                Paper.book().write(Common.Lat, location.latitude)
                Paper.book().write(Common.Lon, location.longitude)
                Paper.book().write(Common.Country, binding.txtCountry.text.toString())
                binding.btnAdd.visibility = View.GONE
                binding.progress.visibility = View.VISIBLE
                getWeatherData()
            }

        }

    }


    private fun getWeatherData() {
        if(Paper.book().read<String>(Common.Language) == null) {
            Paper.book().write(Common.Language, "en")
        }
        if(Paper.book().read<String>(Common.TempUnit) == null) {
            Paper.book().write(Common.TempUnit, "metric")
        }

        viewModel.setLocationToApi(location.latitude,
            location.longitude,
            Paper.book().read<String>(Common.Language).toString(),
            Paper.book().read<String>(Common.TempUnit).toString())

        viewModel.liveData.observe(this) {
            Common.weather = it
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun setToFavorite(favModel: FavModel) {
        // add to room db
        viewModel.insertFav(favModel)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        googleMap.uiSettings.isMyLocationButtonEnabled = true
        googleMap.uiSettings.isZoomControlsEnabled = true

        googleMap.setOnMapClickListener(this)


        //getCurrentLocation()

//        if (isPermissionGiven()){
//
//        } else {
//            givePermission()
//        }

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun isPermissionGiven(): Boolean{
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun givePermission() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(this)
            .check()
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        getCurrentLocation()
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: PermissionRequest?,
        token: PermissionToken?
    ) {
        token!!.continuePermissionRequest()
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        // handle
        Toast.makeText(this, "Permission required for showing location", Toast.LENGTH_LONG).show()
        finish()
    }

    private fun getCurrentLocation() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = (10 * 1000).toLong()
        locationRequest.fastestInterval = 2000

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        val locationSettingsRequest = builder.build()

        val result = LocationServices.getSettingsClient(this).checkLocationSettings(locationSettingsRequest)
        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                if (response!!.locationSettingsStates?.isLocationPresent == true){
                    getLastLocation()
                }
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvable = exception as ResolvableApiException
                        resolvable.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                    } catch (e: IntentSender.SendIntentException) {
                    } catch (e: ClassCastException) {
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> { }
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationProviderClient.lastLocation
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    val mLastLocation = task.result


                } else {
                    Toast.makeText(this, "No current location found", Toast.LENGTH_LONG).show()
                }
            }
    }

    @SuppressLint("SetTextI18n")
    override fun onMapClick(selectedLocation: LatLng) {

        location = selectedLocation

        val gcd = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>
        try {
            addresses = gcd.getFromLocation(selectedLocation.latitude, selectedLocation.longitude, 1)
            if (addresses.isNotEmpty()) {

                if(!addresses[0].adminArea.isNullOrEmpty() && !addresses[0].countryName.isNullOrEmpty()) {
                    binding.txtCountry.text = addresses[0].adminArea + " - " + addresses[0].countryName
                }

                if(!addresses[0].locality.isNullOrEmpty()) {
                    binding.txtLoyality.text = addresses[0].locality
                    binding.txtLoyality.visibility = View.VISIBLE
                } else {
                    binding.txtLoyality.visibility = View.GONE
                }
                binding.txtHint.visibility = View.GONE
                binding.dataLayout.visibility = View.VISIBLE
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val icon = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(this.resources, R.drawable.location_marker))
        googleMap.clear()
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(selectedLocation.latitude, selectedLocation.longitude))
                .icon(icon)
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> {
                if (resultCode == Activity.RESULT_OK) {
                    getCurrentLocation()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}