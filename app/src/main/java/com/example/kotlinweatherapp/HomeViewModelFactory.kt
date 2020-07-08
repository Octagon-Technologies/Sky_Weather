package com.example.kotlinweatherapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinweatherapp.database.WeatherDataBase

class HomeViewModelFactory(
    private val dataBase: WeatherDataBase
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(dataBase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}