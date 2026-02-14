package com.octagontechnologies.sky_weather.data.repository

import com.octagontechnologies.sky_weather.core.model.preferences.Units
import com.octagontechnologies.sky_weather.core.notifications.CustomNotificationCompat
import com.octagontechnologies.sky_weather.domain.model.Location
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

// TODO: Change this to something more sensible
class AllMasterWeatherRepo
    @Inject
    constructor(
        private val locationRepo: LocationRepo,
        private val settingsRepo: SettingsRepo,
        private val currentForecastRepo: CurrentForecastRepo,
        private val hourlyForecastRepo: HourlyForecastRepo,
        private val dailyForecastRepo: DailyForecastRepo,
        private val lunarRepo: LunarRepo,
        private val customNotificationCompat: CustomNotificationCompat,
    ) {
        @OptIn(DelicateCoroutinesApi::class)
        val currentForecast =
            currentForecastRepo.currentForecast.stateIn(GlobalScope, SharingStarted.Companion.Eagerly, null)

        suspend fun refreshCurrentAndHourlyForecast() {
            locationRepo.location.collectLatest { location ->
                location?.let {
                    val units = settingsRepo.units.value

                    currentForecastRepo.refreshCurrentForecast(location)
                    hourlyForecastRepo.refreshHourlyForecast(location)

                    updateNotification(units, location)
                }
            }
        }

        private fun updateNotification(
            units: Units?,
            location: Location,
        ) {
            val isNotificationAllowed = settingsRepo.isNotificationAllowed.value

            if (isNotificationAllowed) {
                customNotificationCompat.createNotification(
                    singleForecast = currentForecast.value,
                    location = location,
                    units = units,
                )
            }
        }

        suspend fun refreshAllData() {
            locationRepo.location.collectLatest { location ->
                location?.let {
                    dailyForecastRepo.refreshDailyForecast(location)
                    lunarRepo.refreshCurrentLunarForecast(location)
                }
            }
        }
    }
