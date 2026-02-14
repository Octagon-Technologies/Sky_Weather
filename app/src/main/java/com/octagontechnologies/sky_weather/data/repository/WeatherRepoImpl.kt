package com.octagontechnologies.sky_weather.data.repository

import com.octagontechnologies.sky_weather.domain.repository.WeatherRepo
import com.octagontechnologies.sky_weather.core.notifications.CustomNotificationCompat
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class WeatherRepoImpl
    @Inject
    constructor(
        private val locationRepo: LocationRepo,
        private val settingsRepo: SettingsRepo,
        private val currentForecastRepo: CurrentForecastRepo,
        private val hourlyForecastRepo: HourlyForecastRepo,
        private val dailyForecastRepo: DailyForecastRepo,
        private val lunarRepo: LunarRepo,
        private val customNotificationCompat: CustomNotificationCompat,
    ) : WeatherRepo {
        suspend fun getLocation() = locationRepo.location.firstOrNull()

        override suspend fun refreshCurrentForecast(): Result<Boolean> {
            val location =
                getLocation()
                    ?: return Result.failure(NullPointerException("Location null"))
            return currentForecastRepo.refreshCurrentForecast(location)
        }

        override suspend fun refreshHourlyForecast(): Result<Boolean> {
            val location =
                getLocation()
                    ?: return Result.failure(NullPointerException("Location null"))
            return hourlyForecastRepo.refreshHourlyForecast(location)
        }

        override suspend fun refreshDailyForecast(): Result<Boolean> {
            val location =
                getLocation()
                    ?: return Result.failure(NullPointerException("Location null"))
            return dailyForecastRepo.refreshDailyForecast(location)
        }

        override suspend fun refreshLunarForecast(): Result<Boolean> {
            val location =
                getLocation()
                    ?: return Result.failure(NullPointerException("Location null"))
            return lunarRepo.refreshCurrentLunarForecast(location)
        }
    }
