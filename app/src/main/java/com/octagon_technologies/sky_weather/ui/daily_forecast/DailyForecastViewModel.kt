package com.octagon_technologies.sky_weather.ui.daily_forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.sky_weather.domain.daily.DailyForecast
import com.octagon_technologies.sky_weather.repository.network.lunar.models.LunarForecastResponse
import com.octagon_technologies.sky_weather.repository.repo.DailyForecastRepo
import com.octagon_technologies.sky_weather.repository.repo.LocationRepo
import com.octagon_technologies.sky_weather.repository.repo.SettingsRepo
import com.octagon_technologies.sky_weather.utils.StatusCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyForecastViewModel @Inject constructor(
    private val locationRepo: LocationRepo,
    val settingsRepo: SettingsRepo,
    private val dailyForecastRepo: DailyForecastRepo
) :
    ViewModel() {
    val location = locationRepo.location
    val units = settingsRepo.units
    val theme = settingsRepo.theme
    val listOfDailyForecast = dailyForecastRepo.listOfDailyForecast

    private var _selectedDailyForecast = MutableLiveData<DailyForecast>()
    val selectedDailyForecast: LiveData<DailyForecast> = _selectedDailyForecast

    private var _lunarForecast = MutableLiveData<LunarForecastResponse>()
    val lunarForecast: LiveData<LunarForecastResponse>
        get() = _lunarForecast

    private var _statusCode = MutableLiveData<StatusCode?>()
    val statusCode: LiveData<StatusCode?> = _statusCode

    init {
        viewModelScope.launch {
            launch {
                listOfDailyForecast.asFlow().collectLatest { listOfDailyForecast ->
                    if (listOfDailyForecast != null)
                        _selectedDailyForecast.value = listOfDailyForecast.first()
                }
            }

            launch {
                location.asFlow().collectLatest { location ->
                    if (location != null)
                        dailyForecastRepo.refreshDailyForecast(location, units.value)
                }
            }
        }
    }

    fun selectDailyForecast(dailyForecast: DailyForecast) {
        _selectedDailyForecast.value = dailyForecast
    }

    fun onStatusCodeDisplayed() {
        _statusCode.value = null
    }
//    fun getSelectedDailyForecast(
//        coordinates: Coordinates,
//        eachDailyForecast: EachDailyForecast,
//        units: Units?
//    ) {
//        // 2020-11-18
//        viewModelScope.launch {
//            val date = SimpleDateFormat(
//                "yyyy-MM-dd",
//                Locale.ENGLISH
//            ).parse(eachDailyForecast.observationTime?.value ?: return@launch)
//            Timber.d("properFormattedDate is ${eachDailyForecast.observationTime.value}")
//
//            _selectedDailyForecast.value =
//                SelectedDailyForecastRepo.getSelectedDailyForecastAsync(
//                    coordinates,
//                    eachDailyForecast.observationTime.value.toString(),
//                    units
//                )
//            _lunarForecast.value =
//                LunarRepo.getLunarForecastAsync(weatherDataBase, coordinates, date?.time ?: 0)
//        }
//
//    }
}