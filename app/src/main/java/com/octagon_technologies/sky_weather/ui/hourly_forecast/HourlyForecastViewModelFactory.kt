package com.octagon_technologies.sky_weather.ui.hourly_forecast

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HourlyForecastViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HourlyForecastViewModel::class.java)) {
            return HourlyForecastViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}