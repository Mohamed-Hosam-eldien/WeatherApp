package com.example.weatherapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favorite")
data class FavModel (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var countryName: String,
    var localityName: String,
    var longitude: Double,
    var latitude: Double )
