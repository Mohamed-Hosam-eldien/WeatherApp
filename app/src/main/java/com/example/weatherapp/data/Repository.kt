package com.example.weatherapp.data
import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weatherapp.data.local.FavDao
import com.example.weatherapp.data.local.FavDatabase
import com.example.weatherapp.data.local.FavModel
import com.example.weatherapp.data.network.NetworkInterFace
import com.example.weatherapp.data.network.WeatherInterface
import com.example.weatherapp.data.network.WeatherService
import kotlinx.coroutines.*


class Repository(private val repoInterface : NetworkInterFace, var context:Context) {

    fun getAllDataFromApi() {
        CoroutineScope(Dispatchers.IO).launch {
            val weatherApi = WeatherService.getInstance().create(WeatherInterface::class.java)
            val response = weatherApi.getCurrentWeather()

            if (response.isSuccessful) {
                if (response.body() != null) {
                    repoInterface.getAllDataFromResponse(response.body()!!)
                }
            }
        }
    }

    fun getAllDataFromApiByLocation(lat:Double, lng:Double, lang:String, unit:String) {
        CoroutineScope(Dispatchers.IO).launch {
            val weatherApi = WeatherService.getInstance().create(WeatherInterface::class.java)
            val response = weatherApi.getWeatherFromLocation(lat, lng, lang, unit)

            if (response.isSuccessful) {
                if (response.body() != null) {
                    repoInterface.getAllDataFromResponse(response.body()!!)
                }
            }
        }
    }


    private lateinit var favDao: FavDao

    fun initDB(){
        val favDatabase = FavDatabase.getInstance(context)
        favDao = favDatabase.favDao()
    }

    suspend fun insertFavToDB(favModel : FavModel) {
        favDao.insertTask(favModel)
    }

    fun getAllFav(): LiveData<List<FavModel>> {
        return favDao.getAllFav()
    }

    suspend fun deleteFromFav(id:Int) {
        return favDao.deleteFromFav(id)
    }

}