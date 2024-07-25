package com.octagontechnologies.sky_weather.repository.repo

import androidx.lifecycle.map
import com.octagontechnologies.sky_weather.domain.Location
import com.octagontechnologies.sky_weather.repository.database.weather.daily.DailyWeatherDao
import com.octagontechnologies.sky_weather.repository.database.weather.daily.LocalDailyForecast
import com.octagontechnologies.sky_weather.repository.network.WeatherApi
import timber.log.Timber
import javax.inject.Inject

class DailyForecastRepo @Inject constructor(
    private val dailyWeatherDao: DailyWeatherDao,
    private val weatherApi: WeatherApi
) {
    val listOfDailyForecast =
        dailyWeatherDao.getLocalDailyForecast().map { it?.listOfDailyForecast }

    suspend fun refreshDailyForecast(
        location: Location
    ) = try {
        val dailyForecast =
            weatherApi.getDailyForecast(
                lat = location.lat,
                lon = location.lon
            ).daily.toListOfDailyForecast()

        dailyWeatherDao.insertData(LocalDailyForecast(listOfDailyForecast = dailyForecast))
    } catch (e: Exception) {
        Timber.e(e)
    }

}