package com.octagontechnologies.sky_weather.feature.current.presentation

// import com.octagontechnologies.sky_weather.ads.AdRepo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.octagontechnologies.sky_weather.data.repository.CurrentForecastRepo
import com.octagontechnologies.sky_weather.data.repository.DailyForecastRepo
import com.octagontechnologies.sky_weather.data.repository.HourlyForecastRepo
import com.octagontechnologies.sky_weather.data.repository.LocationRepo
import com.octagontechnologies.sky_weather.data.repository.LunarRepo
import com.octagontechnologies.sky_weather.data.repository.SettingsRepo
import com.octagontechnologies.sky_weather.domain.model.Location
import com.octagontechnologies.sky_weather.core.notifications.CustomNotificationCompat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CurrentForecastViewModel
    @Inject
    constructor(
        private val currentForecastRepo: CurrentForecastRepo,
        private val hourlyForecastRepo: HourlyForecastRepo,
        private val dailyForecastRepo: DailyForecastRepo,
        private val lunarRepo: LunarRepo,
        private val settingsRepo: SettingsRepo,
        private val locationRepo: LocationRepo,
        private val customNotificationCompat: CustomNotificationCompat,
    ) : ViewModel() {
        val theme = settingsRepo.theme
        val units = settingsRepo.units
        val windDirectionUnits = settingsRepo.windDirectionUnits
        val timeFormat = settingsRepo.timeFormat

        val location = locationRepo.location.stateIn(viewModelScope, SharingStarted.Eagerly, null)

        val currentForecast =
            currentForecastRepo.currentForecast.stateIn(viewModelScope, SharingStarted.Eagerly, null)

        val lunarForecast = lunarRepo.currentLunar

        val predictions =
            hourlyForecastRepo.listOfHourlyForecast.map {
                it?.filterIndexed { index, _ ->
                    index in listOf(0, 1, 2)
                }
            }

        init {
            viewModelScope.launch {
                location.collectLatest { location ->
                    Timber.d("Location in location.collectLatest is $location")

                    if (location != null) {
                        currentForecastRepo.refreshCurrentForecast(location)
                        lunarRepo.refreshCurrentLunarForecast(location)

                        hourlyForecastRepo.refreshHourlyForecast(location)
                        dailyForecastRepo.refreshDailyForecast(location)
                    }
                }

                currentForecast.collectLatest { currentForecast ->
                    val location = location.value

                    if (currentForecast != null && location != null) {
                        updateNotification(location)
                    }
                }
            }
        }

        private fun updateNotification(location: Location) {
            val isNotificationAllowed = settingsRepo.isNotificationAllowed.value
            Timber.d("updateNotification called: isNotificationAllowed is $isNotificationAllowed")

            if (isNotificationAllowed) {
                customNotificationCompat.createNotification(
                    singleForecast = currentForecast.value,
                    location = location,
                    units = units.value,
                )
            }
        }
    }
