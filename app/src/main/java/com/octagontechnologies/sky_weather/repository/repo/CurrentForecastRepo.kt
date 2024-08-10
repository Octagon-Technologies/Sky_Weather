package com.octagontechnologies.sky_weather.repository.repo

import com.octagontechnologies.sky_weather.domain.Location
import com.octagontechnologies.sky_weather.repository.database.weather.current.CurrentForecastDao
import com.octagontechnologies.sky_weather.repository.database.weather.current.LocalCurrentForecast
import com.octagontechnologies.sky_weather.repository.network.WeatherApi
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class CurrentForecastRepo @Inject constructor(
    private val weatherApi: WeatherApi,
    private val currentForecastDao: CurrentForecastDao
) {

    val currentForecast = currentForecastDao.getLocalCurrentForecast().map { it?.currentForecast }


    suspend fun refreshCurrentForecast(
        location: Location
    ) {
        try {
            Timber.d("refreshCurrentForecast: before")

            val currentForecast = weatherApi.getCurrentForecast(
                lat = location.lat,
                lon = location.lon
            ).current.toSingleForecast()

            Timber.d("refreshCurrentForecast: With currentforecast $currentForecast")
            currentForecastDao.insertData(LocalCurrentForecast(currentForecast = currentForecast))
        }
        catch (e: Exception) {
            Timber.e(e)
        }
    }

}