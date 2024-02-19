package com.octagon_technologies.sky_weather.ui.current_forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.notification.CustomNotificationCompat
import com.octagon_technologies.sky_weather.repository.repo.AllergyRepo
import com.octagon_technologies.sky_weather.repository.repo.CurrentForecastRepo
import com.octagon_technologies.sky_weather.repository.repo.HourlyForecastRepo
import com.octagon_technologies.sky_weather.repository.repo.LocationRepo
import com.octagon_technologies.sky_weather.repository.repo.LunarRepo
import com.octagon_technologies.sky_weather.repository.repo.SettingsRepo
import com.octagon_technologies.sky_weather.utils.StatusCode
import com.octagon_technologies.sky_weather.utils.catchNetworkErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class CurrentForecastViewModel @Inject constructor(
    private val currentForecastRepo: CurrentForecastRepo,
    private val hourlyForecastRepo: HourlyForecastRepo,
    private val allergyRepo: AllergyRepo,
    private val lunarRepo: LunarRepo,
    private val settingsRepo: SettingsRepo,
    private val locationRepo: LocationRepo,
    private val customNotificationCompat: CustomNotificationCompat
) : ViewModel() {

    val theme = settingsRepo.theme
    val units = settingsRepo.units
    val windDirectionUnits = settingsRepo.windDirectionUnits
    val timeFormat = settingsRepo.timeFormat

    val location = locationRepo.location
    val currentForecast = currentForecastRepo.currentForecast

    //    val allergyForecast = allergyRepo.allergy
    val lunarForecast = lunarRepo.currentLunar

    val oneHourForecast = hourlyForecastRepo.listOfHourlyForecast.map { it?.getOrNull(0) }
    val sixHourForecast = hourlyForecastRepo.listOfHourlyForecast.map { it?.getOrNull(5) }
    val twentyFourHourForecast = hourlyForecastRepo.listOfHourlyForecast.map { it?.getOrNull(23) }

    private var _statusCode = MutableLiveData<StatusCode?>()
    val statusCode: LiveData<StatusCode?> = _statusCode

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    init {
        viewModelScope.launch {
            launch {
                location.asFlow().collectLatest { location ->
                    try {
                        _statusCode.catchNetworkErrors {
                            if (location != null) {
                                currentForecastRepo.refreshCurrentForecast(location, units.value)
                                hourlyForecastRepo.refreshHourlyForecast(location, units.value)
                                lunarRepo.refreshCurrentLunarForecast(location)
//                    allergyRepo.refreshAllergyForecast(location)

//                        updateNotification(location)
                            }
                        }
                    }  finally {
                        _isRefreshing.value = false
                    }
//            refreshWeatherForecast()
                }
            }

            launch {
                currentForecast.asFlow().collectLatest { currentForecast ->
                    currentForecast?.let {
                        // If a forecast was fetched, it means we definitely have the location
                        location.value?.let {
                            updateNotification(it)
                        }
                    }
                }
            }
        }
    }

    fun refreshWeatherForecast() {
        if (isRefreshing.value == true) return

        viewModelScope.launch {
            location.value?.let {
                _isRefreshing.value = true
                try {
                    currentForecastRepo.refreshCurrentForecast(it, settingsRepo.units.value)
                    hourlyForecastRepo.refreshHourlyForecast(it, settingsRepo.units.value)
                    allergyRepo.refreshAllergyForecast(it)
                    lunarRepo.refreshCurrentLunarForecast(it)

//                    updateNotification(it)
                } catch (http: HttpException) {
                    _statusCode.value = StatusCode.ApiLimitExceeded
                } catch (unknown: UnknownHostException) {
                    _statusCode.value = StatusCode.NoNetwork
                } finally {
                    _isRefreshing.value = false
                }
            }
        }
    }

    private suspend fun updateNotification(it: Location) {
        Timber.d("updateNotification called")
        Timber.d("with settingsRepo.isNotificationAllowed.value as ${settingsRepo.isNotificationAllowedFlow.first()}")

        if (settingsRepo.isNotificationAllowedFlow.first()) {
            Timber.d("updateNotification called in if statement")

            customNotificationCompat.createNotification(
                singleForecast = currentForecast.value,
                location = it,
                timeFormat = timeFormat.value,
                units = units.value
            )
        }
    }

    fun onStatusCodeDisplayed() {
        _statusCode.value = null
    }
}