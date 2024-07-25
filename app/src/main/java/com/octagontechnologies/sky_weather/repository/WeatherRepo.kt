package com.octagontechnologies.sky_weather.repository

import androidx.lifecycle.asFlow
import com.octagontechnologies.sky_weather.domain.Location
import com.octagontechnologies.sky_weather.notification.CustomNotificationCompat
import com.octagontechnologies.sky_weather.repository.repo.CurrentForecastRepo
import com.octagontechnologies.sky_weather.repository.repo.DailyForecastRepo
import com.octagontechnologies.sky_weather.repository.repo.HourlyForecastRepo
import com.octagontechnologies.sky_weather.repository.repo.LocationRepo
import com.octagontechnologies.sky_weather.repository.repo.LunarRepo
import com.octagontechnologies.sky_weather.repository.repo.SettingsRepo
import com.octagontechnologies.sky_weather.utils.Units
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class WeatherRepo @Inject constructor(
    private val locationRepo: LocationRepo,
    private val settingsRepo: SettingsRepo,
    private val currentForecastRepo: CurrentForecastRepo,
    private val hourlyForecastRepo: HourlyForecastRepo,
    private val dailyForecastRepo: DailyForecastRepo,
    private val lunarRepo: LunarRepo,
    private val customNotificationCompat: CustomNotificationCompat
) {

    suspend fun refreshUrgentForecast() {
        locationRepo.location.asFlow().collectLatest { location ->
            location?.let {
                val units = settingsRepo.units.value

                currentForecastRepo.refreshCurrentForecast(location)
                hourlyForecastRepo.refreshHourlyForecast(location)

                updateNotification(units, location)
            }
        }
    }

    private suspend fun updateNotification(units: Units?, location: Location) {
        val isNotificationAllowed = settingsRepo.isNotificationAllowed.first()
        val currentForecast = currentForecastRepo.currentForecast.value

        if (isNotificationAllowed) {
            customNotificationCompat.createNotification(
                singleForecast = currentForecast,
                location = location,
                timeFormat = settingsRepo.timeFormat.value,
                units = units
            )
        }
    }

    suspend fun refreshAllData() {
        locationRepo.location.asFlow().collectLatest { location ->
            location?.let {
                val units = settingsRepo.units.value

//                    currentForecastRepo.refreshCurrentForecast(location, units)
//                    hourlyForecastRepo.refreshHourlyForecast(location, units)
                dailyForecastRepo.refreshDailyForecast(location)
                lunarRepo.refreshCurrentLunarForecast(location)

//                    updateNotification(units, location)
                Timber.d("Location flow called with settingsRepo.isNotificationAllowed.value is ${settingsRepo.isNotificationAllowed.first()}")
            }
        }
    }

}