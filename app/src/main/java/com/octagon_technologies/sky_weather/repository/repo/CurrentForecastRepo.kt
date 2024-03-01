package com.octagon_technologies.sky_weather.repository.repo

import androidx.lifecycle.map
import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.repository.database.weather.current.CurrentForecastDao
import com.octagon_technologies.sky_weather.repository.database.weather.current.LocalCurrentForecast
import com.octagon_technologies.sky_weather.repository.network.WeatherApi
import javax.inject.Inject

class CurrentForecastRepo @Inject constructor(
    private val weatherApi: WeatherApi,
    private val currentForecastDao: CurrentForecastDao
) {

    val currentForecast = currentForecastDao.getLocalCurrentForecast().map { it?.currentForecast }

//    suspend fun setUpRefresh() {
//        locationRepo.location.asFlow().collectLatest { location ->
//            location?.let {
//                refreshCurrentForecast(location, settingsRepo.units.value)
//            }
//        }
//    }

    suspend fun refreshCurrentForecast(
        location: Location
    ) {
        val currentForecast = weatherApi.getCurrentForecast(
            lat = location.lat,
            lon = location.lon
        ).current.toSingleForecast()

        currentForecastDao.insertData(LocalCurrentForecast(currentForecast = currentForecast))
    }

}