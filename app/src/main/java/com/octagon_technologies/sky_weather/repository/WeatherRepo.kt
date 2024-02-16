package com.octagon_technologies.sky_weather.repository

import androidx.lifecycle.asFlow
import com.octagon_technologies.sky_weather.notification.CustomNotificationCompat
import com.octagon_technologies.sky_weather.repository.repo.CurrentForecastRepo
import com.octagon_technologies.sky_weather.repository.repo.DailyForecastRepo
import com.octagon_technologies.sky_weather.repository.repo.HourlyForecastRepo
import com.octagon_technologies.sky_weather.repository.repo.LocationRepo
import com.octagon_technologies.sky_weather.repository.repo.LunarRepo
import com.octagon_technologies.sky_weather.repository.repo.SettingsRepo
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.single
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

    suspend fun refreshData() {
        coroutineScope {
            locationRepo.location.asFlow().collectLatest { location ->
                location?.let {
                    val units = settingsRepo.units.value

                    currentForecastRepo.refreshCurrentForecast(location, units)
                    hourlyForecastRepo.refreshHourlyForecast(location, units)
                    dailyForecastRepo.refreshDailyForecast(location, units)
                    lunarRepo.refreshCurrentLunarForecast(location)

                    Timber.d("Location flow called with settingsRepo.isNotificationAllowed.value is ${settingsRepo.isNotificationAllowed.first()}")
                }
            }
        }
        coroutineScope {
            currentForecastRepo.currentForecast.asFlow().collect { singleForecast ->
                val location = locationRepo.location.value

                if (location != null && singleForecast != null) {
                    val units = settingsRepo.units.value

                    if (settingsRepo.isNotificationAllowed.first()) {
                        Timber.d("currentForecastRepo.currentForecast.asFlow() called with single forecast as $singleForecast")
                        customNotificationCompat.createNotification(
                            singleForecast = singleForecast,
                            location = location,
                            timeFormat = settingsRepo.timeFormat.value,
                            units = units
                        )
                    }
                }
            }
        }
    }

}