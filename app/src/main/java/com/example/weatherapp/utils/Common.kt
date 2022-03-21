package com.example.weatherapp.utils

import com.example.weatherapp.data.model.WeatherModel

class Common {

    companion object {
        val Country: String = "country"
        val Lat: String = "lat"
        val Lon: String = "lon"
        val TempUnit: String = "temp_unit"
        val WindSpeed: String = "wind_speed"
        var weather: WeatherModel? = null
        var weatherByLocation: WeatherModel? = null
        var Language = "language"
    }
}