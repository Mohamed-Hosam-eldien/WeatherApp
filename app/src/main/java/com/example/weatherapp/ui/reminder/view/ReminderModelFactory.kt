package com.example.weatherapp.ui.reminder.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.ui.reminder.viewModel.ReminderViewModel

class ReminderModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReminderViewModel(context) as T
    }
}
