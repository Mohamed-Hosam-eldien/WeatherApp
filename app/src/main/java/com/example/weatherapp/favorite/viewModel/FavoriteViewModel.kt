package com.example.weatherapp.favorite.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.Repository
import com.example.weatherapp.data.local.FavModel
import com.example.weatherapp.data.model.WeatherModel
import com.example.weatherapp.data.network.NetworkInterFace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(context: Context) : ViewModel(), NetworkInterFace {

    private val repo = Repository(this, context)

    val mutableLiveData : MutableLiveData<WeatherModel> = MutableLiveData<WeatherModel>()
    val liveData : LiveData<WeatherModel> = mutableLiveData

    fun initDatabase() {
        repo.initDB()
    }

    fun insertFav(favLocation: FavModel) {
        viewModelScope.launch {
            repo.insertFavToDB(favLocation)
            Log.d("TAG", "insertFav: done")
        }
    }

    fun getFav(): LiveData<List<FavModel>> {
        return repo.getAllFav()
    }

    suspend fun deleteFromFav(id:Int) {
        return repo.deleteFromFav(id)
    }

    fun setLocationToApi(lat:Double, lng:Double, lang:String, unit:String) {
        repo.getAllDataFromApiByLocation(lat, lng, lang, unit)
    }

    override fun getAllDataFromResponse(weatherModel: WeatherModel?) {
        mutableLiveData.postValue(weatherModel)
    }

}