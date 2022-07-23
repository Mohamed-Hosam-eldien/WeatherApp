package com.example.weatherapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivitySplashBinding
import com.example.weatherapp.ui.favorite.view.MapsActivity
import com.example.weatherapp.ui.home.viewModel.HomeViewModel
import com.example.weatherapp.ui.home.viewModel.HomeViewModelFactory
import com.example.weatherapp.utils.Common
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import io.paperdb.Paper
import java.io.IOException
import java.util.*

class Splash : AppCompatActivity(), PermissionListener {

    private lateinit var binding: ActivitySplashBinding

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(this)
    }

    companion object {
        const val REQUEST_CHECK_SETTINGS = 43
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Paper.init(this)

        if (isOnline(this)) {

            fusedLocationClient = getFusedLocationProviderClient(this)

            if (Paper.book().read<Double>(Common.Lat)?.toDouble() == null) {
                showLocationDialog()
            } else {
                binding.animationView.setAnimation(R.raw.lotti_weather)
                getWeatherData(
                    Paper.book().read<Double>(Common.Lat)!!,
                    Paper.book().read<Double>(Common.Lon)!!
                )
            }
        } else {
            showConnectionDialog()
        }

    }

    private fun showConnectionDialog() {

        val view = LayoutInflater.from(this).inflate(R.layout.internet_layout, null)
        val btn: Button = view.findViewById(R.id.btnTryAgain)

        val dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btn.setOnClickListener {
            recreate()
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val capabilities =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            } else {
                TODO("VERSION.SDK_INT < M")
            }

        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
        return false
    }

    private fun showLocationDialog() {

        val view = LayoutInflater.from(this).inflate(R.layout.location_layout, null)
        val radioG: RadioGroup = view.findViewById(R.id.locationRadioGroup)
        val btnOk: Button = view.findViewById(R.id.btnOk)
        val txt: TextView = view.findViewById(R.id.txtLabel)
        val prog: ProgressBar = view.findViewById(R.id.progress)

        txt.setTextColor(Color.WHITE)

        btnOk.setOnClickListener {
            when (radioG.checkedRadioButtonId) {
                R.id.rbCurrentLocation -> {
                    btnOk.visibility = View.GONE
                    prog.visibility = View.VISIBLE
                    getLocation()
                }
                R.id.rbLocationMap -> {
                    goToMapActivity()
                }
            }
        }

        val dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.setContentView(view)
        dialog.show()
    }

    private fun goToMapActivity() {
        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra("fromSplash", "splash")
        startActivity(intent)
        finish()
    }

    private fun getLocation() {
        if (isPermissionGiven()) {
            getCurrentLocation()
        } else {
            givePermission()
        }
    }


    private fun isPermissionGiven(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
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

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = (10 * 1000).toLong()
        locationRequest.fastestInterval = 2000
        locationRequest.numUpdates = 1

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        val locationSettingsRequest = builder.build()

        val result =
            LocationServices.getSettingsClient(this).checkLocationSettings(locationSettingsRequest)
        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                if (response!!.locationSettingsStates?.isLocationPresent == true) {

                    getFusedLocationProviderClient(this).requestLocationUpdates(
                        locationRequest, @SuppressLint("MissingPermission")
                        object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult) {
                                // do work here
                                onLocationChanged(locationResult.lastLocation)
                            }
                        },
                        Looper.myLooper()!!
                    )
                }
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvable = exception as ResolvableApiException
                        resolvable.startResolutionForResult(
                            this,
                            MapsActivity.REQUEST_CHECK_SETTINGS
                        )
                    } catch (e: IntentSender.SendIntentException) {
                    } catch (e: ClassCastException) {
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                }
            }
        }
    }

    fun onLocationChanged(location: Location?) {
        // New location has now been determined
        Paper.book().write(Common.Lat, location!!.latitude)
        Paper.book().write(Common.Lon, location.longitude)


        val gcd = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>
        try {
            addresses = gcd.getFromLocation(location.latitude, location.longitude, 1)
            if (addresses.isNotEmpty()) {

                if (!addresses[0].adminArea.isNullOrEmpty() && !addresses[0].countryName.isNullOrEmpty()) {
                    val country = addresses[0].adminArea + " - " + addresses[0].countryName
                    Paper.book().write(Common.Country, country)

                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            getWeatherData(location.latitude, location.longitude)
                        }
                    }, 2000)

                }

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

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

    private fun getWeatherData(latitude: Double, longitude: Double) {
        if (Paper.book().read<String>(Common.Language) == null) {
            Paper.book().write(Common.Language, "en")
        }
        if (Paper.book().read<String>(Common.TempUnit) == null) {
            Paper.book().write(Common.TempUnit, "metric")
        }

        updateViews(Paper.book().read<String>(Common.Language).toString())

        viewModel.setLocationToApi(
            latitude,
            longitude,
            Paper.book().read<String>(Common.Language).toString(),
            Paper.book().read<String>(Common.TempUnit).toString()
        )

        viewModel.liveData.observe(this) {
            Common.weather = it
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun updateViews(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        baseContext
            .resources
            .updateConfiguration(configuration, baseContext.resources.displayMetrics)
    }
}