package com.example.weatherapp.data.local

interface LocalInterface {

    suspend fun insertFavLocation(favLocation : FavModel)

}