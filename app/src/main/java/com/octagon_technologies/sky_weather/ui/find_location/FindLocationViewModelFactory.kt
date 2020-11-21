package com.octagon_technologies.sky_weather.ui.find_location

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FindLocationViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FindLocationViewModel::class.java)) {
            return FindLocationViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}