package com.octagontechnologies.sky_weather.ui.hourly_forecast.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.octagontechnologies.sky_weather.domain.SingleForecast
import com.octagontechnologies.sky_weather.repository.repo.HourlyForecastRepo
import com.octagontechnologies.sky_weather.repository.repo.LocationRepo
import com.octagontechnologies.sky_weather.repository.repo.SettingsRepo
import com.octagontechnologies.sky_weather.utils.StatusCode
import com.octagontechnologies.sky_weather.utils.catchNetworkErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

//    val adRepo = AdRepo()

    val theme = settingsRepo.theme
    val units = settingsRepo.units

    val timeFormat = settingsRepo.timeFormat
    val windDirectionUnits = settingsRepo.windDirectionUnits

    val location = locationRepo.location
    val listOfHourlyForecast = hourlyForecastRepo.listOfHourlyForecast

    private val _selectedHourlyForecast = MutableLiveData<SingleForecast>()
    val selectedHourlyForecast: LiveData<SingleForecast> = _selectedHourlyForecast

    private val _titleDay = MutableStateFlow("")
    val titleDay: StateFlow<String> = _titleDay


    private var _statusCode = MutableLiveData<StatusCode?>()
    val statusCode: LiveData<StatusCode?> = _statusCode

    init {
        viewModelScope.launch {
            listOfHourlyForecast.asFlow().collectLatest { listOfHourlyForecast ->
                Timber.d("listOfHourlyForecast.asFlow().collectLatest is ${listOfHourlyForecast?.firstOrNull()}")
                if (listOfHourlyForecast != null)
                    selectHourlyForecast(listOfHourlyForecast.first())
            }

            location.collectLatest { location ->
                if (location != null)
                    _statusCode.catchNetworkErrors {
                        hourlyForecastRepo.refreshHourlyForecast(location)
                    }
            }
        }
    }

    fun selectHourlyForecast(hourlyForecast: SingleForecast) {
        _selectedHourlyForecast.value = hourlyForecast
    }


    fun updateTitleDay(newDay: String) {
        if (titleDay.value != newDay) {
            _titleDay.value = newDay
        }
    }


    fun onStatusCodeDisplayed() {
        _statusCode.value = null
    }
}
