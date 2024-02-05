package com.octagon_technologies.sky_weather.repository.repo

import androidx.lifecycle.map
import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.repository.database.toLocalHourlyForecast
import com.octagon_technologies.sky_weather.repository.database.weather.hourly.HourlyWeatherDao
import com.octagon_technologies.sky_weather.repository.network.weather.TomorrowApi
import com.octagon_technologies.sky_weather.utils.Units
import javax.inject.Inject

class HourlyForecastRepo @Inject constructor(
    private val hourlyWeatherDao: HourlyWeatherDao,
    private val tomorrowApi: TomorrowApi
) {
    val listOfHourlyForecast = hourlyWeatherDao.getLocalHourlyForecast().map { it.listOfHourlyForecast }

    suspend fun refreshHourlyForecast(
        location: Location,
        units: Units?
    ) {
        val hourlyForecastResponse =
            tomorrowApi.getHourlyForecast(
                location = location.getCoordinates(),
                units = units?.value ?: Units.METRIC.value
            )

        hourlyWeatherDao.insertLocalHourlyForecast(
            hourlyForecastResponse.toLocalHourlyForecast()
        )
    }

}