package com.octagon_technologies.sky_weather.ui.current_forecast

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.sky_weather.repository.AllergyRepo.getAllergyValueAsync
import com.octagon_technologies.sky_weather.repository.CurrentForecastRepo.getCurrentForecastAsync
import com.octagon_technologies.sky_weather.repository.LunarRepo.getLunarForecastAsync
import com.octagon_technologies.sky_weather.repository.database.WeatherDataBase
import com.octagon_technologies.sky_weather.repository.network.allergy_forecast.Allergy
import com.octagon_technologies.sky_weather.repository.network.lunar_forecast.LunarForecast
import com.octagon_technologies.sky_weather.repository.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.repository.network.single_forecast.SingleForecast
import com.octagon_technologies.sky_weather.utils.StatusCode
import com.octagon_technologies.sky_weather.utils.Units
import com.octagon_technologies.sky_weather.utils.getCoordinates
import kotlinx.coroutines.launch

class CurrentForecastViewModel @ViewModelInject constructor(private val weatherDataBase: WeatherDataBase) :
    ViewModel() {

    private var _singleForecast = MutableLiveData<SingleForecast?>()
    val singleForecast: LiveData<SingleForecast?>
        get() = _singleForecast

    private var _allergyForecast = MutableLiveData<Allergy?>()
    val allergyForecast: LiveData<Allergy?>
        get() = _allergyForecast

    private var _lunarForecast = MutableLiveData<LunarForecast?>()
    val lunarForecast: LiveData<LunarForecast?>
        get() = _lunarForecast

    private var _statusCode = MutableLiveData<StatusCode>()
    val statusCode: LiveData<StatusCode> = _statusCode

    fun getLocalLocation(units: Units?, location: ReverseGeoCodingLocation?) {
        val coordinates = location?.getCoordinates() ?: return

        viewModelScope.launch {
            val result = getCurrentForecastAsync(weatherDataBase, coordinates, units)

            _statusCode.value = result.first
            _singleForecast.value = result.second
            _allergyForecast.value = getAllergyValueAsync(weatherDataBase, coordinates)
            _lunarForecast.value = getLunarForecastAsync(weatherDataBase, coordinates)
        }
    }
}