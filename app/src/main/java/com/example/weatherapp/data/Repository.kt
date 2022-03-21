package com.example.weatherapp.data
import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weatherapp.data.local.FavDao
import com.example.weatherapp.data.local.FavDatabase
import com.example.weatherapp.data.local.FavModel
import com.example.weatherapp.data.local.ReminderDao
import com.example.weatherapp.data.model.ReminderModel
import com.example.weatherapp.data.network.NetworkInterFace
import com.example.weatherapp.data.network.WeatherInterface
import com.example.weatherapp.data.network.WeatherService
import kotlinx.coroutines.*


class Repository(private val repoInterface : NetworkInterFace, var context:Context) {

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
    private lateinit var remDao: ReminderDao

    fun initDB(){
        val favDatabase = FavDatabase.getInstance(context)
        favDao = favDatabase.favDao()
        remDao = favDatabase.remDao()
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


    suspend fun insertRemToDB(remModel : ReminderModel) {
        remDao.insertRem(remModel)
    }

    fun getAllRem(): LiveData<List<ReminderModel>> {
        return remDao.getAllRem()
    }

    suspend fun deleteFromRem(id:Int) {
        return remDao.deleteFromRem(id)
    }

}