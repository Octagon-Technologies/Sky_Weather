package com.octagontechnologies.sky_weather.repository.repo

import androidx.lifecycle.map
import com.octagontechnologies.sky_weather.domain.Location
import com.octagontechnologies.sky_weather.repository.database.weather.hourly.HourlyWeatherDao
import com.octagontechnologies.sky_weather.repository.database.weather.hourly.LocalHourlyForecast
import com.octagontechnologies.sky_weather.repository.network.WeatherApi
import timber.log.Timber
import javax.inject.Inject

class HourlyForecastRepo @Inject constructor(
    private val hourlyWeatherDao: HourlyWeatherDao,
    private val weatherApi: WeatherApi
) {
    val listOfHourlyForecast = hourlyWeatherDao.getLocalHourlyForecast().map { it?.listOfHourlyForecast }

    suspend fun refreshHourlyForecast(
        location: Location
    ) = try {
        val hourlyForecastResponse =
            weatherApi.getHourlyForecast(
                lat = location.lat,
                lon = location.lon
            ).hourly.toListOfSingleForecast()

        hourlyWeatherDao.insertData(
            LocalHourlyForecast(listOfHourlyForecast = hourlyForecastResponse)
        )
    }  catch(e: Exception) {
        Timber.e(e)
    }

}