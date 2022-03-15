package com.example.weatherapp.home.view

import com.example.weatherapp.data.model.WeatherModel

interface ViewModelInterface {

    fun getWeatherDetails(weatherModel: WeatherModel)

}
