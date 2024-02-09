package com.octagon_technologies.sky_weather.repository.repo

import androidx.lifecycle.map
import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.repository.database.toLocalDailyForecast
import com.octagon_technologies.sky_weather.repository.database.weather.daily.DailyWeatherDao
import com.octagon_technologies.sky_weather.repository.network.weather.TomorrowApi
import com.octagon_technologies.sky_weather.utils.Units
import timber.log.Timber
import javax.inject.Inject

class DailyForecastRepo @Inject constructor(
    private val dailyWeatherDao: DailyWeatherDao,
    private val weatherApi: TomorrowApi
) {
    val listOfDailyForecast = dailyWeatherDao.getLocalDailyForecast().map { it?.listOfDailyForecast }

    suspend fun refreshDailyForecast(
        location: Location,
        units: Units?
    ) = try {
        val remoteDailyForecast =
            weatherApi.getDailyForecast(
                location = location.getCoordinates(),
                units = (units ?: Units.METRIC).getUrlParameter()
            )

        dailyWeatherDao.insertData(remoteDailyForecast.toLocalDailyForecast())
    } catch(e: Exception) {
        Timber.e(e)
    }

}