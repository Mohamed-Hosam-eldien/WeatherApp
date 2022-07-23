package com.example.weatherapp.ui.settings.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.weatherapp.R
import com.example.weatherapp.ui.Splash
import com.example.weatherapp.databinding.FragmentSettingsBinding
import com.example.weatherapp.ui.favorite.view.MapsActivity
import com.example.weatherapp.ui.home.viewModel.HomeViewModel
import com.example.weatherapp.ui.home.viewModel.HomeViewModelFactory
import com.example.weatherapp.utils.Common
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import io.paperdb.Paper
import java.io.IOException
import java.util.*


class SettingsFragment : Fragment(), PermissionListener {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var dialog : Dialog

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Paper.init(requireContext())

        binding = FragmentSettingsBinding.bind(
            inflater.inflate(
                R.layout.fragment_settings,
                container,
                false
            )
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtCurrentLocation.text = Paper.book().read(Common.Country)

        binding.locationLayout.setOnClickListener { showLocationDialog() }

        binding.layoutUnit.setOnClickListener { showUnitsDialog() }

        binding.layoutWindSpeed.setOnClickListener { showWindSpeedDialog() }

        binding.layoutLang.setOnClickListener { showLanguageDialog() }

        viewModel.liveData.observe(requireActivity()) { Common.weather = it }


        when {
            Paper.book().read<String>(Common.TempUnit).equals("metric") -> {
                binding.txtCurrentUnit.text = getString(R.string.celsius)
            }
            Paper.book().read<String>(Common.TempUnit).equals("standard") -> {
                binding.txtCurrentUnit.text = getString(R.string.fahrenheit)
            }
            Paper.book().read<String>(Common.TempUnit).equals("imperial") -> {
                binding.txtCurrentUnit.text = getString(R.string.imperial)
            }
        }


        if (Paper.book().read<String>(Common.WindSpeed).equals("metre/sec")) {
            binding.txtCurrentWindSpeed.text = getString(R.string.meter_sec)
        } else {
            binding.txtCurrentWindSpeed.text = getString(R.string.miles_hour)
        }

        if (Paper.book().read<String>(Common.Language).equals("ar")) {
            binding.txtCurrentLang.text = getString(R.string.Arabic)
        } else {
            binding.txtCurrentLang.text = getString(R.string.english)
        }

    }

    private fun showLanguageDialog() {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.language_layout, null)
        val radioG: RadioGroup = view.findViewById(R.id.langRadioGroup)
        val dialog = Dialog(requireContext(), R.style.Theme_Dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (Paper.book().read<String>(Common.Language).equals("ar")) {
            radioG.check(R.id.rbArabic)
        } else {
            radioG.check(R.id.rbEnglish)
        }

        radioG.setOnCheckedChangeListener { group, checkedId ->
            run {
                when (checkedId) {
                    R.id.rbArabic -> {
                        Paper.book().write(Common.Language, "ar")
                        binding.txtCurrentLang.text = getString(R.string.Arabic)
                    }
                    R.id.rbEnglish -> {
                        Paper.book().write(Common.Language, "en")
                        binding.txtCurrentLang.text = getString(R.string.english)
                    }

                }

                changeLang(dialog)
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun changeApiData() {
        viewModel.setLocationToApi(
            Paper.book().read<Double>(Common.Lat)!!,
            Paper.book().read<Double>(Common.Lon)!!,
            Paper.book().read<String>(Common.Language).toString(),
            Paper.book().read<String>(Common.TempUnit).toString()
        )

    }

    private fun changeLang(dialog: Dialog) {
        updateViews(Paper.book().read<String>(Common.Language).toString())
        changeApiData()

        requireActivity().recreate()
        dialog.dismiss()
    }

    private fun updateViews(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        requireActivity().baseContext
            .resources
            .updateConfiguration(
                configuration,
                requireActivity().baseContext.resources.displayMetrics
            )
    }

    private fun showWindSpeedDialog() {

        val view = LayoutInflater.from(requireContext()).inflate(R.layout.wind_speed_layout, null)
        val radioG: RadioGroup = view.findViewById(R.id.windRadioGroup)

        val dialog = Dialog(requireContext(), R.style.Theme_Dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (Paper.book().read<String>(Common.WindSpeed).equals("metre/sec")) {
            radioG.check(R.id.rbMeter)
        } else {
            radioG.check(R.id.rbMilles)
        }


        radioG.setOnCheckedChangeListener { group, checkedId ->
            run {
                when (checkedId) {
                    R.id.rbMeter -> {
                        Paper.book().write(Common.WindSpeed, "metre/sec")
                        binding.txtCurrentWindSpeed.text = getString(R.string.meter_sec)
                    }
                    R.id.rbMilles -> {
                        Paper.book().write(Common.WindSpeed, "miles/hour")
                        binding.txtCurrentWindSpeed.text = getString(R.string.miles_hour)
                    }
                }
                changeApiData()
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun showUnitsDialog() {

        val view = LayoutInflater.from(requireContext()).inflate(R.layout.temprature_layout, null)
        val radioG: RadioGroup = view.findViewById(R.id.tempRadioGroup)

        val dialog = Dialog(requireContext(), R.style.Theme_Dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        when {
            Paper.book().read<String>(Common.TempUnit).equals("metric") -> {
                radioG.check(R.id.rbCelsius)
            }
            Paper.book().read<String>(Common.TempUnit).equals("standard") -> {
                radioG.check(R.id.rbFahrenheit)
            }
            Paper.book().read<String>(Common.TempUnit).equals("imperial") -> {
                radioG.check(R.id.rbKelvin)
            }
        }


        radioG.setOnCheckedChangeListener { group, checkedId ->
            run {
                when (checkedId) {
                    R.id.rbCelsius -> {
                        Paper.book().write(Common.TempUnit, "metric")
                        binding.txtCurrentUnit.text = getString(R.string.celsius)
                    }
                    R.id.rbFahrenheit -> {
                        Paper.book().write(Common.TempUnit, "standard")
                        binding.txtCurrentUnit.text = getString(R.string.fahrenheit)
                    }
                    R.id.rbKelvin -> {
                        Paper.book().write(Common.TempUnit, "imperial")
                        binding.txtCurrentUnit.text = getString(R.string.imperial)
                    }
                }
                changeApiData()
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun showLocationDialog() {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.location_layout, null)
        val radioG: RadioGroup = view.findViewById(R.id.locationRadioGroup)
        val btnOk: Button = view.findViewById(R.id.btnOk)
        val progress: ProgressBar = view.findViewById(R.id.progress)

        dialog = Dialog(requireContext(), R.style.Theme_Dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btnOk.setOnClickListener {
            when (radioG.checkedRadioButtonId) {
                R.id.rbLocationMap -> {
                    dialog.dismiss()
                    goToMapActivity()
                }
                R.id.rbCurrentLocation -> {
                    btnOk.visibility = View.GONE
                    progress.visibility = View.VISIBLE
                    getLocation()
                }
            }
        }

        dialog.setContentView(view)
        dialog.show()
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
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun givePermission() {
        Dexter.withActivity(requireActivity())
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

        val result = LocationServices.getSettingsClient(requireContext())
            .checkLocationSettings(locationSettingsRequest)
        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                if (response!!.locationSettingsStates?.isLocationPresent == true) {

                    LocationServices.getFusedLocationProviderClient(requireContext())
                        .requestLocationUpdates(
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
                            requireActivity(),
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


        val gcd = Geocoder(requireActivity(), Locale.getDefault())
        val addresses: List<Address>
        try {
            addresses = gcd.getFromLocation(location.latitude, location.longitude, 1)
            if (addresses.isNotEmpty()) {

                if (!addresses[0].adminArea.isNullOrEmpty() && !addresses[0].countryName.isNullOrEmpty()) {
                    val country = addresses[0].adminArea + " - " + addresses[0].countryName
                    Paper.book().write(Common.Country, country)
                    binding.txtCurrentLocation.text = country
                    changeApiData()
                    dialog.dismiss()
                }

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            Splash.REQUEST_CHECK_SETTINGS -> {
                if (resultCode == Activity.RESULT_OK) {
                    getCurrentLocation()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun goToMapActivity() {
        val intent = Intent(requireContext(), MapsActivity::class.java)
        intent.putExtra("fromSplash", "splash")
        startActivity(intent)
    }

}