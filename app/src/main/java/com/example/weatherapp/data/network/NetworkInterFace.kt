package com.example.weatherapp.data.network

import com.example.weatherapp.models.WeatherModel

interface NetworkInterFace {

    fun getAllDataFromResponse(weatherModel: WeatherModel?)

}