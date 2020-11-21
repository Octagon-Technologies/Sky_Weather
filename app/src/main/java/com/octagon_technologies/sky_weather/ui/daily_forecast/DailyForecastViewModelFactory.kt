package com.octagon_technologies.sky_weather.ui.daily_forecast

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DailyForecastViewModelFactory (private val context: Context) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DailyForecastViewModel::class.java)) {
            return DailyForecastViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}