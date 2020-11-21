package com.octagon_technologies.sky_weather.ui.current_forecast

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CurrentForecastViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrentForecastViewModel::class.java)) {
            return CurrentForecastViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}