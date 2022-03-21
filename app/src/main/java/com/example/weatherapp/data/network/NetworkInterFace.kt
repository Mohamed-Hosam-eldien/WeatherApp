package com.example.weatherapp.data.network

import com.example.weatherapp.data.model.WeatherModel

interface NetworkInterFace {

    fun getAllDataFromResponse(weatherModel: WeatherModel?)

}