package com.example.weatherapp.data
import android.content.Context
import android.util.Log
import com.example.weatherapp.data.model.WeatherModel
import com.example.weatherapp.data.network.RepoInterFace
import com.example.weatherapp.data.network.WeatherInterface
import com.example.weatherapp.data.network.WeatherService
import kotlinx.coroutines.*


class Repository(private val repoInterface : RepoInterFace) {

    fun getAllDataFromApi() {
        CoroutineScope(Dispatchers.IO).launch {
            val weatherApi = WeatherService.getInstance().create(WeatherInterface::class.java)
            val response = weatherApi.getCurrentWeather()

            if (response.isSuccessful) {
                if (response.body() != null) {
                    repoInterface.getAllDataFromResponse(response.body()!!)
                }
            }
        }
    }

}