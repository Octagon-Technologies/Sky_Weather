package com.octagontechnologies.sky_weather.data.repository

import androidx.lifecycle.map
import com.octagontechnologies.sky_weather.data.local.room.weather.daily.DailyWeatherDao
import com.octagontechnologies.sky_weather.data.local.room.weather.daily.LocalDailyForecast
import com.octagontechnologies.sky_weather.data.remote.api.WeatherApi
import com.octagontechnologies.sky_weather.domain.model.Location
import timber.log.Timber
import javax.inject.Inject

class DailyForecastRepo
    @Inject
    constructor(
        private val dailyWeatherDao: DailyWeatherDao,
        private val weatherApi: WeatherApi,
    ) {
        val listOfDailyForecast =
            dailyWeatherDao.getLocalDailyForecast().map { it?.listOfDailyForecast }

        suspend fun refreshDailyForecast(location: Location) =
            try {
                val dailyForecast =
                    weatherApi
                        .getDailyForecast(
                            lat = location.lat,
                            lon = location.lon,
                        ).daily
                        .toListOfDailyForecast()

                dailyWeatherDao.insertData(LocalDailyForecast(listOfDailyForecast = dailyForecast))
                Result.success(true)
            } catch (e: Exception) {
                Timber.e(e)
                Result.failure(e)
            }
    }
