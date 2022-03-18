package com.example.weatherapp.home.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.Repository
import com.example.weatherapp.data.model.WeatherModel
import com.example.weatherapp.data.network.NetworkInterFace


class HomeViewModel(context : Context) : ViewModel(), NetworkInterFace{

    private val repo = Repository(this,context)

    val mutableLiveData : MutableLiveData<WeatherModel> = MutableLiveData<WeatherModel>()

    fun getData() {
        repo.getAllDataFromApi()
    }

    override fun getAllDataFromResponse(weatherModel: WeatherModel?) {
        mutableLiveData.postValue(weatherModel)
        Log.d("TAG", "getAllDataFromResponse: $weatherModel")
    }

    override fun getAllDataByLocation(lat: Double, lng: Double) {
        TODO("Not yet implemented")
    }

}