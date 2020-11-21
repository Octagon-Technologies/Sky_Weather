package com.octagon_technologies.sky_weather.ui.current_forecast

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.octagon_technologies.sky_weather.Units
import com.octagon_technologies.sky_weather.database.MainDataBase
import com.octagon_technologies.sky_weather.getCoordinates
import com.octagon_technologies.sky_weather.network.allergy_forecast.Allergy
import com.octagon_technologies.sky_weather.network.single_forecast.SingleForecast
import com.octagon_technologies.sky_weather.network.lunar_forecast.LunarForecast
import com.octagon_technologies.sky_weather.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.ui.shared_code.MainAllergyForecastObject.getAllergyValueAsync
import com.octagon_technologies.sky_weather.ui.shared_code.MainCurrentForecastObject.getCurrentForecastAsync
import com.octagon_technologies.sky_weather.ui.shared_code.MainLunarForecastObject.getLunarForecastAsync
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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


    fun getLocalLocation(units: Units?, location: ReverseGeoCodingLocation?) {
        val coordinates = location?.getCoordinates()!!

        uiScope.launch {
            _singleForecast.value =
                getCurrentForecastAsync(mainDataBase, coordinates, units)
            _allergyForecast.value = getAllergyValueAsync(mainDataBase, coordinates)
            _lunarForecast.value = getLunarForecastAsync(mainDataBase, coordinates)
        }
    }
}