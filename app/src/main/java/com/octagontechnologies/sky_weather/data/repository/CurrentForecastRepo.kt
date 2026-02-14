package com.octagontechnologies.sky_weather.data.repository

import com.octagontechnologies.sky_weather.data.local.room.weather.current.CurrentForecastDao
import com.octagontechnologies.sky_weather.data.local.room.weather.current.LocalCurrentForecast
import com.octagontechnologies.sky_weather.data.remote.api.WeatherApi
import com.octagontechnologies.sky_weather.domain.model.Location
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class CurrentForecastRepo
    @Inject
    constructor(
        private val weatherApi: WeatherApi,
        private val currentForecastDao: CurrentForecastDao,
    ) {
        val currentForecast = currentForecastDao.getLocalCurrentForecast().map { it?.currentForecast }

        suspend fun refreshCurrentForecast(location: Location): Result<Boolean> {
            try {
                Timber.d("refreshCurrentForecast: before")

                val currentForecast =
                    weatherApi
                        .getCurrentForecast(
                            lat = location.lat,
                            lon = location.lon,
                        ).current
                        .toSingleForecast()

                Timber.d("refreshCurrentForecast: With currentforecast $currentForecast")
                currentForecastDao.insertData(LocalCurrentForecast(currentForecast = currentForecast))

                return Result.success(true)
            } catch (e: Exception) {
                Timber.e(e)
                return Result.failure(e)
            }
        }
    }
