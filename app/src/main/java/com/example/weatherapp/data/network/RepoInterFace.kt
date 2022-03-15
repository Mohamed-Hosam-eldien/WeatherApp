package com.example.weatherapp.data.network

import com.example.weatherapp.data.model.WeatherModel

interface RepoInterFace {

    fun getAllDataFromResponse(weatherModel: WeatherModel?)

}