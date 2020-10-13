package com.example.kotlinweatherapp.ui.current_forecast

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinweatherapp.database.MainDataBase
import com.example.kotlinweatherapp.network.allergy_forecast.Allergy
import com.example.kotlinweatherapp.network.current_forecast.SingleForecast
import com.example.kotlinweatherapp.network.lunar_forecast.LunarForecast
import com.example.kotlinweatherapp.network.mockLat
import com.example.kotlinweatherapp.network.mockLon
import com.example.kotlinweatherapp.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.example.kotlinweatherapp.ui.find_location.Coordinates
import com.example.kotlinweatherapp.ui.weather_forecast_objects.MainAllergyForecastObject.getAllergyValueAsync
import com.example.kotlinweatherapp.ui.weather_forecast_objects.MainCurrentForecastObject.getCurrentForecastAsync
import com.example.kotlinweatherapp.ui.weather_forecast_objects.MainLocationObject
import com.example.kotlinweatherapp.ui.weather_forecast_objects.MainLunarForecastObject.getLunarForecastAsync
import kotlinx.coroutines.*
import timber.log.Timber

class CurrentForecastViewModel(context: Context) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val mainDataBase = MainDataBase.getInstance(context)

    private var _singleForecast = MutableLiveData<SingleForecast?>()
    val singleForecast: LiveData<SingleForecast?>
        get() = _singleForecast

    private var _allergyForecast = MutableLiveData<Allergy?>()
    val allergyForecast: LiveData<Allergy?>
        get() = _allergyForecast

    private var _lunarForecast = MutableLiveData<LunarForecast?>()
    val lunarForecast: LiveData<LunarForecast?>
        get() = _lunarForecast

    private var _shouldNavigate = MutableLiveData<Boolean>()
    val shouldNavigate: LiveData<Boolean>
        get() = _shouldNavigate

    var reverseLocation: ReverseGeoCodingLocation? = null

    init {
        _shouldNavigate.value = false
        uiScope.launch {
            reverseLocation = MainLocationObject.getLocalLocationAsync(mainDataBase)
            if(reverseLocation == null) _shouldNavigate.value = true
        }
    }

    fun loadData() {
        getLocalLocation()
    }

    private fun getLocalLocation() {
        uiScope.launch {
            val coordinates = Coordinates(reverseLocation?.lon?.toDouble() ?: mockLon, reverseLocation?.lat?.toDouble() ?: mockLat)
            Timber.d("End of getLocalLocation() with coordinates as $coordinates")

            getCurrentForecast(coordinates)
            getAllergyValue(coordinates)
            getLunarForecast(coordinates)
        }
    }

    private fun getLunarForecast(coordinates: Coordinates) {
        uiScope.launch {
            _lunarForecast.value = getLunarForecastAsync(mainDataBase, uiScope, coordinates)
        }
    }

    private fun getAllergyValue(coordinates: Coordinates) {
        uiScope.launch {
            _allergyForecast.value = getAllergyValueAsync(mainDataBase, uiScope, coordinates)
        }
    }

    private fun getCurrentForecast(coordinates: Coordinates) {
        uiScope.launch {
            Timber.d("Start of getCurrentForecast with coordinates as $coordinates")
            _singleForecast.value = getCurrentForecastAsync(uiScope, mainDataBase, coordinates)
        }
    }

}