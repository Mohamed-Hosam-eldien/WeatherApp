package com.example.weatherapp.home.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.Repository
import com.example.weatherapp.data.model.WeatherModel
import com.example.weatherapp.data.network.RepoInterFace

class HomeViewModel() : ViewModel(), RepoInterFace{

    private val repo: Repository = Repository(this)

    val mutableLiveData : MutableLiveData<WeatherModel> = MutableLiveData<WeatherModel>()

    fun getData() {
        repo.getAllDataFromApi()
    }

    override fun getAllDataFromResponse(weatherModel: WeatherModel?) {
        mutableLiveData.postValue(weatherModel)
        Log.d("TAG", "getAllDataFromResponse: $weatherModel")
    }

}