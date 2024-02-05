package com.octagon_technologies.sky_weather.repository.repo

import androidx.lifecycle.map
import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.repository.database.toLocalDailyForecast
import com.octagon_technologies.sky_weather.repository.database.weather.daily.DailyWeatherDao
import com.octagon_technologies.sky_weather.repository.network.weather.TomorrowApi
import com.octagon_technologies.sky_weather.utils.Units
import javax.inject.Inject

class DailyForecastRepo @Inject constructor(
    private val dailyWeatherDao: DailyWeatherDao,
    private val weatherApi: TomorrowApi
) {
    val listOfDailyForecast = dailyWeatherDao.getLocalDailyForecast().map { it.listOfDailyForecast }

    suspend fun getDailyForecastAsync(
        location: Location,
        units: Units?
    ) {
        val remoteDailyForecast =
            weatherApi.getDailyForecast(
                location = location.getCoordinates(),
                units = units?.value!!
            )

        dailyWeatherDao.insertLocalDailyForecast(remoteDailyForecast.toLocalDailyForecast())
    }

}