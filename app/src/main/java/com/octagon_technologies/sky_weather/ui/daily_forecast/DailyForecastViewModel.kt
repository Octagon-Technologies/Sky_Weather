package com.octagon_technologies.sky_weather.ui.daily_forecast

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.sky_weather.models.Coordinates
import com.octagon_technologies.sky_weather.repository.DailyForecastRepo
import com.octagon_technologies.sky_weather.repository.LunarRepo
import com.octagon_technologies.sky_weather.repository.SelectedDailyForecastRepo
import com.octagon_technologies.sky_weather.repository.database.WeatherDataBase
import com.octagon_technologies.sky_weather.repository.network.daily_forecast.EachDailyForecast
import com.octagon_technologies.sky_weather.repository.network.lunar_forecast.LunarForecast
import com.octagon_technologies.sky_weather.repository.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.repository.network.selected_daily_forecast.SelectedDailyForecast
import com.octagon_technologies.sky_weather.utils.StatusCode
import com.octagon_technologies.sky_weather.utils.Units
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class DailyForecastViewModel @ViewModelInject constructor(private val weatherDataBase: WeatherDataBase) :
    ViewModel() {
    private var _dailyForecast = MutableLiveData<ArrayList<EachDailyForecast>>()
    val dailyForecast: LiveData<ArrayList<EachDailyForecast>>
        get() = _dailyForecast

    private var _selectedDailyForecast = MutableLiveData<SelectedDailyForecast>()
    val selectedDailyForecast: LiveData<SelectedDailyForecast>
        get() = _selectedDailyForecast

    private var _lunarForecast = MutableLiveData<LunarForecast>()
    val lunarForecast: LiveData<LunarForecast>
        get() = _lunarForecast

    private var _statusCode = MutableLiveData<StatusCode>()
    val statusCode: LiveData<StatusCode> = _statusCode

    fun getDailyForecastAsync(
        location: ReverseGeoCodingLocation?,
        units: Units?
    ) {
        val coordinates = Coordinates(
            location?.lon?.toDouble() ?: return,
            location.lat?.toDouble() ?: return
        )

        viewModelScope.launch {
            val result =
                DailyForecastRepo.getDailyForecastAsync(weatherDataBase, coordinates, units)

            _statusCode.value = result.first
            _dailyForecast.value = result.second?.apply {
                getSelectedDailyForecast(coordinates, this[0], units)
            }
        }
    }

    fun getSelectedDailyForecast(
        coordinates: Coordinates,
        eachDailyForecast: EachDailyForecast,
        units: Units?
    ) {
        // 2020-11-18
        viewModelScope.launch {
            val date = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.ENGLISH
            ).parse(eachDailyForecast.observationTime?.value ?: return@launch)
            Timber.d("properFormattedDate is ${eachDailyForecast.observationTime.value}")

            _selectedDailyForecast.value =
                SelectedDailyForecastRepo.getSelectedDailyForecastAsync(
                    coordinates,
                    eachDailyForecast.observationTime.value.toString(),
                    units
                )
            _lunarForecast.value =
                LunarRepo.getLunarForecastAsync(weatherDataBase, coordinates, date?.time ?: 0)
        }

    }
}