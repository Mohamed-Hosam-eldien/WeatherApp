package com.example.weatherapp.data.network

import android.annotation.SuppressLint
import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherService {

    companion object {

        private const val baseUrl = "https://api.openweathermap.org/data/2.5/"

        fun getInstance() : Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

}