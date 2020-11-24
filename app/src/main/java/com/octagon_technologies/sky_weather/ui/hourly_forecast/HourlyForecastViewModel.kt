package com.octagon_technologies.sky_weather.ui.hourly_forecast

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.octagon_technologies.sky_weather.Units
import com.octagon_technologies.sky_weather.database.MainDataBase
import com.octagon_technologies.sky_weather.network.hourly_forecast.EachHourlyForecast
import com.octagon_technologies.sky_weather.network.mockLat
import com.octagon_technologies.sky_weather.network.mockLon
import com.octagon_technologies.sky_weather.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.network.single_forecast.ObservationTime
import com.octagon_technologies.sky_weather.network.single_forecast.SingleForecast
import com.octagon_technologies.sky_weather.ui.find_location.Coordinates
import com.octagon_technologies.sky_weather.ui.shared_code.MainHourlyForecastObject
import com.octagon_technologies.sky_weather.ui.shared_code.MainSelectedHourlyForecastObject.getSelectedSingleForecastAsync
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

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

    var isDay = MutableLiveData<Boolean>()

    fun getHourlyForecastAsync(
        location: ReverseGeoCodingLocation?,
        units: Units?,
        shouldLoadSelectedHourlyForecast: Boolean = true
    ) {
        val coordinates = Coordinates(
            location?.lon?.toDouble() ?: mockLon,
            location?.lat?.toDouble() ?: mockLat
        )

        uiScope.launch {
            _hourlyForecast.value = MainHourlyForecastObject.getHourlyForecastAsync(mainDataBase, coordinates, units)
            if (shouldLoadSelectedHourlyForecast) {
                getSelectedSingleForecast(
                    _hourlyForecast.value?.get(0)?.observationTime, units, coordinates
                )
            }
        }
    }

    fun getSelectedSingleForecast(observationTime: ObservationTime?, units: Units?, coordinates: Coordinates) {
        uiScope.launch {
            Timber.d("observation time is $observationTime")
            observationTime?.let {
                _selectedSingleForecast.value =
                    getSelectedSingleForecastAsync(coordinates, observationTime.value.toString(), units)
            }
        }
    }

}
