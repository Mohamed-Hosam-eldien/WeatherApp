package com.example.weatherapp.favorite.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherModel
import com.example.weatherapp.databinding.ActivityLocationDetailsBinding
import com.example.weatherapp.favorite.viewModel.FavViewModelFactory
import com.example.weatherapp.favorite.viewModel.FavoriteViewModel
import com.example.weatherapp.home.view.DailyAdapter
import com.example.weatherapp.home.view.HourlyAdapter
import com.example.weatherapp.utils.Common
import io.paperdb.Paper
import java.text.SimpleDateFormat
import java.util.*

class LocationDetails : AppCompatActivity() {

    private lateinit var binding: ActivityLocationDetailsBinding
    private lateinit var weather : WeatherModel


    private val viewModel: FavoriteViewModel by viewModels {
        FavViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtGovernorate.text = intent.getStringExtra("Country")

        viewModel.setLocationToApi(intent.getDoubleExtra("Lat",0.0),
            intent.getDoubleExtra("Lon",0.0),
            Paper.book().read<String>(Common.Language).toString(),
            Paper.book().read<String>(Common.TempUnit).toString())


        viewModel.liveData.observe(this) {
            weather = it

            Timer().schedule(object : TimerTask() {
                override fun run() {

                    runOnUiThread {

                        setCurrentData()

                        setHourlyData()

                        setDailyData()

                        binding.cardDetails.visibility = View.VISIBLE
                        binding.progress.visibility = View.GONE
                        binding.cardBackground.visibility = View.VISIBLE
                    }
                }
            }, 1500)

        }

    }

    private fun setDailyData() {
        binding.dailyRecycler.setHasFixedSize(true)
        binding.dailyRecycler.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false)

        val adapter = DailyAdapter(weather.daily)
        binding.dailyRecycler.adapter = adapter
    }

    private fun setHourlyData() {
        binding.hourlyRecycler.setHasFixedSize(true)
        binding.hourlyRecycler.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false)

        val adapter = HourlyAdapter(weather.hourly)
        binding.hourlyRecycler.adapter = adapter
    }


    @SuppressLint("SetTextI18n")
    private fun setCurrentData() {
        binding.txtTemp.text = (weather.current.temp).toInt().toString()
        binding.txtWeatherState.text = weather.current.weather[0].description

        binding.txtHumidity.text = "${weather.current.humidity} %"
        binding.txtWindSpeed.text = "${weather.current.wind_speed} m/s"
        binding.txtPressure.text = "${weather.current.pressure} hpa"
        binding.txtClouds.text = "${weather.current.clouds} %"

        setCurrentDate()

        when(Paper.book().read<String>(Common.TempUnit)) {
            "metric" -> binding.txtTempUnit.text = "C"
            "standard" -> binding.txtTempUnit.text = "F"
            "imperial" -> binding.txtTempUnit.text = "I"
        }

        when(weather.current.weather[0].icon) {
            "01d" -> {
                binding.imgTemp.setImageResource(R.drawable.sunny)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.clear_sky_gr)
            }
            "01n" -> {
                binding.imgTemp.setImageResource(R.drawable.moon)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.clear_sky_gr)
            }
            "02d" -> {
                binding.imgTemp.setImageResource(R.drawable.few_clouds)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.few_clouds_gr)
            }
            "02n" -> {
                binding.imgTemp.setImageResource(R.drawable.night_cloud)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.few_clouds_gr)
            }
            "03d" -> {
                binding.imgTemp.setImageResource(R.drawable.scattered_clouds)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.scattered_clouds_gr)
            }
            "03n" -> {
                binding.imgTemp.setImageResource(R.drawable.scattered_clouds)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.scattered_clouds_gr)
            }
            "04d" -> {
                binding.imgTemp.setImageResource(R.drawable.broken_clouds)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.broken_clouds_gr)
            }
            "04n" -> {
                binding.imgTemp.setImageResource(R.drawable.broken_clouds)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.broken_clouds_gr)
            }
            "09d" -> {
                binding.imgTemp.setImageResource(R.drawable.shower_rain)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.rain_gr)
            }
            "09n" -> {
                binding.imgTemp.setImageResource(R.drawable.shower_rain)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.rain_gr)
            }
            "10d" -> {
                binding.imgTemp.setImageResource(R.drawable.heavy_rain)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.rain_gr)
            }
            "10n" -> {
                binding.imgTemp.setImageResource(R.drawable.heavy_rain)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.rain_gr)
            }
            "11d" -> {
                binding.imgTemp.setImageResource(R.drawable.thunder)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.thunder_gr)
            }
            "11n" -> {
                binding.imgTemp.setImageResource(R.drawable.thunder)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.thunder_gr)
            }
            "13d" -> {
                binding.imgTemp.setImageResource(R.drawable.snow)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.snow_gr)
            }
            "13n" -> {
                binding.imgTemp.setImageResource(R.drawable.snow)
                //binding.weatherLayoutBackground.setBackgroundResource(R.drawable.snow_gr)
            }

        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setCurrentDate() {
        val sdf: SimpleDateFormat = if(Paper.book().read<String>(Common.Language).toString() == "en")
            SimpleDateFormat("EEEE dd/M", Locale.US)
        else
            SimpleDateFormat("EEEE dd/M")

        val currentDate = sdf.format(Date())
        binding.txtCurrentDate.text = currentDate
    }

}