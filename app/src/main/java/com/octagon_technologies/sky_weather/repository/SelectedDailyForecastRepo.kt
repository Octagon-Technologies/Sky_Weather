package com.octagon_technologies.sky_weather.repository

import com.octagon_technologies.sky_weather.utils.Units
import com.octagon_technologies.sky_weather.network.WeatherForecastRetrofitItem
import com.octagon_technologies.sky_weather.repository.network.selected_daily_forecast.SelectedDailyForecast
import com.octagon_technologies.sky_weather.ui.find_location.Coordinates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

object SelectedDailyForecastRepo {
    suspend fun getSelectedDailyForecastAsync(
        coordinates: Coordinates,
        timeInIso: String,
        units: Units?
    ): SelectedDailyForecast? {
        return try{
            withContext(Dispatchers.IO) {
                WeatherForecastRetrofitItem.weatherRetrofitService.getSelectedDailyForecastAsync(
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

