package com.octagon_technologies.sky_weather.ui.hourly_forecast

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.sky_weather.models.Coordinates
import com.octagon_technologies.sky_weather.repository.HourlyForecastRepo
import com.octagon_technologies.sky_weather.repository.SelectedHourlyForecastRepo.getSelectedSingleForecastAsync
import com.octagon_technologies.sky_weather.repository.database.WeatherDataBase
import com.octagon_technologies.sky_weather.repository.network.hourly_forecast.EachHourlyForecast
import com.octagon_technologies.sky_weather.repository.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.repository.network.single_forecast.ObservationTime
import com.octagon_technologies.sky_weather.repository.network.single_forecast.SingleForecast
import com.octagon_technologies.sky_weather.utils.StatusCode
import com.octagon_technologies.sky_weather.utils.Units
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


// TODO - Check nullable types
// TODO - CHECK IDE WARNINGS
class HourlyForecastViewModel @ViewModelInject constructor(private val weatherDataBase: WeatherDataBase) : ViewModel() {

    private var _hourlyForecast = MutableLiveData<ArrayList<EachHourlyForecast>>()
    val hourlyForecast: LiveData<ArrayList<EachHourlyForecast>>
        get() = _hourlyForecast

    private var _selectedSingleForecast = MutableLiveData<SingleForecast>()
    val selectedSingleForecast: LiveData<SingleForecast>
        get() = _selectedSingleForecast

    private var _statusCode = MutableLiveData<StatusCode>()
    val statusCode: LiveData<StatusCode> = _statusCode

    var isDay = MutableLiveData<Boolean>()

    fun getHourlyForecastAsync(
        location: ReverseGeoCodingLocation?,
        units: Units?,
        shouldLoadSelectedHourlyForecast: Boolean = true
    ) {
        val coordinates = Coordinates(
            location?.lon?.toDouble() ?: return,
            location.lat?.toDouble() ?: return
        )

        viewModelScope.launch {
            val result = HourlyForecastRepo.getHourlyForecastAsync(this@HourlyForecastViewModel.weatherDataBase, coordinates, units)

            _statusCode.value = result.first
            _hourlyForecast.value = result.second

            if (shouldLoadSelectedHourlyForecast) {
                getSelectedSingleForecast(
                    _hourlyForecast.value?.get(0)?.observationTime, units, coordinates
                )
            }
        }
    }

    fun getSelectedSingleForecast(
        observationTime: ObservationTime?,
        units: Units?,
        coordinates: Coordinates
    ) {
        viewModelScope.launch {
            Timber.d("observation time is $observationTime")
            observationTime?.let {
                _selectedSingleForecast.value =
                    getSelectedSingleForecastAsync(
                        coordinates,
                        observationTime.value.toString(),
                        units
                    )?.apply {
                        val hours = SimpleDateFormat("HH", Locale.ENGLISH)
                            .format(DateTime(observationTime.value).toDate().time)?.toInt()

                        isDay.value = hours in 8..19
                    }
            }
        }
    }

}
