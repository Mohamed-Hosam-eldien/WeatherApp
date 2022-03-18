package com.example.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.weatherapp.databinding.ActivitySplashBinding
import com.example.weatherapp.home.viewModel.HomeViewModel
import com.example.weatherapp.home.viewModel.HomeViewModelFactory
import com.example.weatherapp.utils.Common

class Splash : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private val viewModel: HomeViewModel by viewModels() {
        HomeViewModelFactory(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.animationView.setAnimation(R.raw.lotti_weather)

        getWeatherData()

    }

    private fun getWeatherData() {
        viewModel.getData()
        viewModel.mutableLiveData.observe(this) {
            Common.weather = it
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()






            Log.d("TAG", "zone ${it.timezone}")
            Log.d("TAG", "time ${it.timezone_offset}")
            Log.d("TAG", "time ${it.current.dt}")
            Log.d("TAG", "Sunrise  ${it.current.sunrise}")
            Log.d("TAG", "sunset ${it.current.sunset}")
            Log.d("TAG", "humidity ${it.current.humidity}")
            Log.d("TAG", "cloud ${it.current.clouds}")
            Log.d("TAG", "wind speed ${it.current.wind_speed}")
            Log.d("TAG", "id ${it.current.weather[0].id}")
            Log.d("TAG", "description ${it.current.weather[0].description}")
            Log.d("TAG", "icon ${it.current.weather[0].icon}")
            Log.d("TAG", "main ${it.current.weather[0].main}")
            Log.d("TAG", "size ${it.current.temp}")

            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()

        }

    }
}