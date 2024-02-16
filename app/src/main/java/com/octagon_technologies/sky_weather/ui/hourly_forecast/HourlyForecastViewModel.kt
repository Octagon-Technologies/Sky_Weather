package com.octagon_technologies.sky_weather.ui.hourly_forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.sky_weather.domain.SingleForecast
import com.octagon_technologies.sky_weather.repository.repo.HourlyForecastRepo
import com.octagon_technologies.sky_weather.repository.repo.LocationRepo
import com.octagon_technologies.sky_weather.repository.repo.SettingsRepo
import com.octagon_technologies.sky_weather.utils.StatusCode
import com.octagon_technologies.sky_weather.utils.catchNetworkErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HourlyForecastViewModel @Inject constructor(
    private val locationRepo: LocationRepo,
    private val settingsRepo: SettingsRepo,
    private val hourlyForecastRepo: HourlyForecastRepo
) : ViewModel() {

    val theme = settingsRepo.theme
    val units = settingsRepo.units
    val timeFormat = settingsRepo.timeFormat
    val windDirectionUnits = settingsRepo.windDirectionUnits

    val location = locationRepo.location
    val listOfHourlyForecast = hourlyForecastRepo.listOfHourlyForecast

    private val _selectedSingleForecast = MutableLiveData<SingleForecast>()
    val selectedSingleForecast: LiveData<SingleForecast> = _selectedSingleForecast

    private var _statusCode = MutableLiveData<StatusCode?>()
    val statusCode: LiveData<StatusCode?> = _statusCode

    init {
        viewModelScope.launch {
            listOfHourlyForecast.asFlow().collectLatest { listOfHourlyForecast ->
                Timber.d("listOfHourlyForecast.asFlow().collectLatest is ${listOfHourlyForecast?.firstOrNull()}")
                if (listOfHourlyForecast != null)
                    selectHourlyForecast(listOfHourlyForecast.first())
            }

            location.asFlow().collectLatest { location ->
                if (location != null)
                    _statusCode.catchNetworkErrors {
                        hourlyForecastRepo.refreshHourlyForecast(location, units.value)
                    }
            }
        }
    }

    fun selectHourlyForecast(hourlyForecast: SingleForecast) {
        _selectedSingleForecast.value = hourlyForecast
    }

    fun onStatusCodeDisplayed() {
        _statusCode.value = null
    }
}
