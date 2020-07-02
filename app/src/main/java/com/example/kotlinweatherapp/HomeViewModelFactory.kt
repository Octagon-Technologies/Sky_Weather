package com.example.kotlinweatherapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinweatherapp.database.DatabaseDao

class HomeViewModelFactory(
    private val cityDataSource: DatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(cityDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}