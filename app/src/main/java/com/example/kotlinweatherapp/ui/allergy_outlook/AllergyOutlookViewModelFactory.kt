package com.example.kotlinweatherapp.ui.allergy_outlook

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AllergyOutlookViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllergyOutlookViewModel::class.java)) {
            return AllergyOutlookViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}