package com.octagontechnologies.sky_weather.ui.daily_forecast.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.octagontechnologies.sky_weather.domain.Lunar
import com.octagontechnologies.sky_weather.domain.daily.DailyForecast
import com.octagontechnologies.sky_weather.domain.toLatLng
import com.octagontechnologies.sky_weather.repository.network.lunar.models.LunarForecastResponse
import com.octagontechnologies.sky_weather.repository.repo.DailyForecastRepo
import com.octagontechnologies.sky_weather.repository.repo.LocationRepo
import com.octagontechnologies.sky_weather.repository.repo.LunarRepo
import com.octagontechnologies.sky_weather.repository.repo.SettingsRepo
import com.octagontechnologies.sky_weather.utils.StatusCode
import com.octagontechnologies.sky_weather.utils.getFullMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyForecastViewModel @Inject constructor(
    private val locationRepo: LocationRepo,
    private val lunarRepo: LunarRepo,
    val settingsRepo: SettingsRepo,
    private val dailyForecastRepo: DailyForecastRepo
) :
    ViewModel() {
    val location =
        locationRepo.location.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val units = settingsRepo.units
    val windDirectionUnits = settingsRepo.windDirectionUnits
    val theme = settingsRepo.theme

    val listOfDailyForecast = dailyForecastRepo.listOfDailyForecast

    private var _selectedDailyForecast = MutableLiveData<DailyForecast>()
    val selectedDailyForecast: LiveData<DailyForecast> = _selectedDailyForecast

    private var _selectedLunarForecast = MutableLiveData<Lunar>()
    val selectedLunarForecast: LiveData<Lunar> = _selectedLunarForecast

    private var _lunarForecast = MutableLiveData<LunarForecastResponse>()
    val lunarForecast: LiveData<LunarForecastResponse>
        get() = _lunarForecast

    private var _statusCode = MutableLiveData<StatusCode?>()
    val statusCode: LiveData<StatusCode?> = _statusCode

    val currentMonth = System.currentTimeMillis().getFullMonth()


    init {
        viewModelScope.launch {
            launch {
                listOfDailyForecast.asFlow().collectLatest { listOfDailyForecast ->
                    if (listOfDailyForecast != null)
                        _selectedDailyForecast.value = listOfDailyForecast.first()
                }
            }

            launch {
                location.collectLatest { location ->
                    if (location != null)
                        dailyForecastRepo.refreshDailyForecast(location)
                }
            }
        }
    }

    fun selectDailyForecast(dailyForecast: DailyForecast) = viewModelScope.launch {
        _selectedDailyForecast.value = dailyForecast

        val location = location.value?.toLatLng()
        if (location != null) {
            val lunarForecast =
                lunarRepo.getSelectedLunarForecast(location, dailyForecast.timeInEpochSeconds).data
            _selectedLunarForecast.value = lunarForecast ?: return@launch
        }
    }
}