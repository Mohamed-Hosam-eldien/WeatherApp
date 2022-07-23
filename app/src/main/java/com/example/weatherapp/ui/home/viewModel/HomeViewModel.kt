package com.example.weatherapp.ui.home.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.repos.Repository
import com.example.weatherapp.models.WeatherModel
import com.example.weatherapp.data.network.NetworkInterFace


class HomeViewModel(context : Context) : ViewModel(), NetworkInterFace{

    private val repo = Repository(this,context)

    private val mutableLiveData : MutableLiveData<WeatherModel?> = MutableLiveData<WeatherModel?>()
    val liveData : LiveData<WeatherModel?> = mutableLiveData

    fun setLocationToApi(lat:Double, lng:Double, lang:String, unit:String) {
        repo.getAllDataFromApiByLocation(lat, lng, lang, unit)
    }

    override fun getAllDataFromResponse(weatherModel: WeatherModel?) {
        mutableLiveData.postValue(weatherModel)
    }


}