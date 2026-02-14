package com.octagontechnologies.sky_weather.data.repository

import androidx.lifecycle.map
import com.octagontechnologies.sky_weather.data.local.room.weather.hourly.HourlyWeatherDao
import com.octagontechnologies.sky_weather.data.local.room.weather.hourly.LocalHourlyForecast
import com.octagontechnologies.sky_weather.data.remote.api.WeatherApi
import com.octagontechnologies.sky_weather.domain.model.Location
import timber.log.Timber
import javax.inject.Inject

class HourlyForecastRepo
    @Inject
    constructor(
        private val hourlyWeatherDao: HourlyWeatherDao,
        private val weatherApi: WeatherApi,
    ) {
        val listOfHourlyForecast = hourlyWeatherDao.getLocalHourlyForecast().map { it?.listOfHourlyForecast }

        suspend fun refreshHourlyForecast(location: Location) =
            try {
                val hourlyForecastResponse =
                    weatherApi
                        .getHourlyForecast(
                            lat = location.lat,
                            lon = location.lon,
                        ).hourly
                        .toListOfSingleForecast()

                hourlyWeatherDao.insertData(
                    LocalHourlyForecast(listOfHourlyForecast = hourlyForecastResponse),
                )

                Result.success(true)
            } catch (e: Exception) {
                Timber.e(e)
                Result.failure(e)
            }
    }
