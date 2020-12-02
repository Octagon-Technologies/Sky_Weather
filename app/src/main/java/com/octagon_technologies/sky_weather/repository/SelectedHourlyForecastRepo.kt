package com.octagon_technologies.sky_weather.repository

import com.octagon_technologies.sky_weather.Units
import com.octagon_technologies.sky_weather.network.WeatherForecastRetrofitItem
import com.octagon_technologies.sky_weather.repository.network.single_forecast.SingleForecast
import com.octagon_technologies.sky_weather.ui.find_location.Coordinates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

object SelectedHourlyForecastRepo {
    suspend fun getSelectedSingleForecastAsync(
        coordinates: Coordinates,
        timeInIso: String,
        units: Units?
    ): SingleForecast? {
        return try {
            withContext(Dispatchers.IO) {
                WeatherForecastRetrofitItem.weatherRetrofitService.getSelectedHourlyForecastAsync(
                    startTimeInISO = timeInIso,
                    endTimeInISO = timeInIso,
                    unitSystem = units?.value ?: Units.METRIC.value,
                    lon = coordinates.lon,
                    lat = coordinates.lat
                ).await()[0]
            }
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }
}
