package com.example.weatherapp.ui.reminder.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.repos.Repository
import com.example.weatherapp.models.ReminderModel
import com.example.weatherapp.models.WeatherModel
import com.example.weatherapp.data.network.NetworkInterFace
import kotlinx.coroutines.launch

class ReminderViewModel(context:Context) : ViewModel(),NetworkInterFace {

    private val repo = Repository(this, context)

    private val mutableLiveData : MutableLiveData<WeatherModel?> = MutableLiveData<WeatherModel?>()

    fun initDatabase() {
        repo.initDB()
    }

    fun insertRem(rem: ReminderModel) {
        viewModelScope.launch {
            repo.insertRemToDB(rem)
            Log.d("TAG", "insertFav: done")
        }
    }

    fun getRem(): LiveData<List<ReminderModel>> {
        return repo.getAllRem()
    }

    suspend fun deleteFromRem(id:Int) {
        return repo.deleteFromRem(id)
    }

    override fun getAllDataFromResponse(weatherModel: WeatherModel?) {
        mutableLiveData.postValue(weatherModel)
    }
}