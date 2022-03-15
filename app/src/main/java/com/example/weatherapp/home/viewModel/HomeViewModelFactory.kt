package com.example.weatherapp.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.home.view.ViewModelInterface

class HomeViewModelFactory(private val viewI: ViewModelInterface) : ViewModelProvider.NewInstanceFactory() {
//
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return HomeViewModel(viewI) as T
//    }
}
