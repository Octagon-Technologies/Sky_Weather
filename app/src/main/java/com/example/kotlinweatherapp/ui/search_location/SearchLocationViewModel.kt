package com.example.kotlinweatherapp.ui.search_location

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinweatherapp.network.location.Location
import com.example.kotlinweatherapp.network.location.LocationItem
import com.example.kotlinweatherapp.ui.weather_forecast_objects.MainLocationObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchLocationViewModel(val context: Context) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _locationSuggestions = MutableLiveData<ArrayList<Location>>()
    val locationSuggestions: LiveData<ArrayList<Location>>
        get() = _locationSuggestions

    fun getLocationSuggestions(query: String) {
        if (query.isEmpty()) return

        uiScope.launch {
            _locationSuggestions.value = MainLocationObject.getLocationSuggestionsFromQuery(query)
        }
    }
}