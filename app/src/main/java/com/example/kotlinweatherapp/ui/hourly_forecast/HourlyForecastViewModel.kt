package com.example.kotlinweatherapp.ui.hourly_forecast

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinweatherapp.database.MainDataBase
import com.example.kotlinweatherapp.network.current_forecast.ObservationTime
import com.example.kotlinweatherapp.network.current_forecast.SingleForecast
import com.example.kotlinweatherapp.network.hourly_forecast.EachHourlyForecast
import com.example.kotlinweatherapp.ui.weather_forecast_objects.MainHourlyForecastObject.getHourlyForecastAsync
import com.example.kotlinweatherapp.ui.weather_forecast_objects.MainSelectedHourlyForecastObject.getSelectedSingleForecastAsync
import kotlinx.coroutines.*
import java.util.ArrayList

class HourlyForecastViewModel(context: Context) : ViewModel() {
    private val mainDataBase = MainDataBase.getInstance(context)
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _hourlyForecast = MutableLiveData<ArrayList<EachHourlyForecast>>()
    val hourlyForecast: LiveData<ArrayList<EachHourlyForecast>>
        get() = _hourlyForecast

    private var _selectedSingleForecast = MutableLiveData<SingleForecast>()
    val selectedSingleForecast: LiveData<SingleForecast>
        get() = _selectedSingleForecast

    fun loadData() {
        getHourlyForecast()
    }

    fun getSelectedSingleForecast(observationTime: ObservationTime) {
        uiScope.launch {
            _selectedSingleForecast.value = getSelectedSingleForecastAsync(mainDataBase, observationTime.value.toString())
        }
    }

    private fun getHourlyForecast() {
        uiScope.launch {
            _hourlyForecast.value = getHourlyForecastAsync(mainDataBase)
        }
    }
}
