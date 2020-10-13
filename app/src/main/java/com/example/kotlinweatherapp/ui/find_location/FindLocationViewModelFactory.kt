package com.example.kotlinweatherapp.ui.find_location

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinweatherapp.ui.current_forecast.CurrentForecastViewModel

class FindLocationViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FindLocationViewModel::class.java)) {
            return FindLocationViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}