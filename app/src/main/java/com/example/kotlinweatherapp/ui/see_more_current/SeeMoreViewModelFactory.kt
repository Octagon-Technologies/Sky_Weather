package com.example.kotlinweatherapp.ui.see_more_current

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SeeMoreViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SeeMoreViewModel::class.java)) {
            return SeeMoreViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}