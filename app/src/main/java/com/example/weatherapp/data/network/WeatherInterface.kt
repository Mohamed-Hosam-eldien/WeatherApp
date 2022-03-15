package com.example.weatherapp.data.network

import com.example.weatherapp.data.model.WeatherModel
import retrofit2.Response
import retrofit2.http.GET

interface WeatherInterface {

    @GET("onecall?lat=31.192621623727305&lon=29.889177937839385&lang=ar&units=metric&exclude=hourly.dt,daily.dt&appid=e5a7c0ff66b7ccce6336a786ae939c81")
    suspend fun getCurrentWeather() : Response<WeatherModel>

}