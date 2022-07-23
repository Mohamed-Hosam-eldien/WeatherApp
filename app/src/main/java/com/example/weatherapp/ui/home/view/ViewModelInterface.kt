package com.example.weatherapp.ui.home.view

import com.example.weatherapp.models.WeatherModel

interface ViewModelInterface {

    fun getWeatherDetails(weatherModel: WeatherModel)

}
