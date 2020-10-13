package com.example.kotlinweatherapp.ui.see_more_current

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinweatherapp.database.MainDataBase
import com.example.kotlinweatherapp.network.current_forecast.SingleForecast
import com.example.kotlinweatherapp.network.mockLat
import com.example.kotlinweatherapp.network.mockLon
import com.example.kotlinweatherapp.ui.find_location.Coordinates
import com.example.kotlinweatherapp.ui.weather_forecast_objects.MainCurrentForecastObject.getCurrentForecastAsync
import com.example.kotlinweatherapp.ui.weather_forecast_objects.MainLocationObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SeeMoreViewModel(context: Context) : ViewModel() {
    private val mainDataBase = MainDataBase.getInstance(context)
    private val uiScope = CoroutineScope(Dispatchers.Main)

    private var _currentForecast = MutableLiveData<SingleForecast>()
    val currentForecast: LiveData<SingleForecast>
        get() = _currentForecast

    fun loadData() {
        uiScope.launch {
            val location =
                MainLocationObject.getLocalLocationAsync(mainDataBase)
            val coordinates = Coordinates(location?.lon?.toDouble() ?: mockLon, location?.lat?.toDouble() ?: mockLat)
            _currentForecast.value = getCurrentForecastAsync(uiScope, mainDataBase, coordinates)
        }
    }
}