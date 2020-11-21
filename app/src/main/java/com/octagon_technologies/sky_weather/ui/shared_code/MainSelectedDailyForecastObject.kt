package com.octagon_technologies.sky_weather.ui.shared_code

import com.octagon_technologies.sky_weather.Units
import com.octagon_technologies.sky_weather.network.WeatherForecastRetrofitItem
import com.octagon_technologies.sky_weather.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.network.selected_daily_forecast.SelectedDailyForecast
import com.octagon_technologies.sky_weather.ui.find_location.Coordinates

object MainSelectedDailyForecastObject {
    suspend fun getSelectedDailyForecastAsync(
        coordinates: Coordinates,
        timeInIso: String,
        units: Units?
    ): SelectedDailyForecast? {
        return try {
            val remoteSelectedDailyForecast =
                WeatherForecastRetrofitItem.weatherRetrofitService.getSelectedDailyForecastAsync(
                    startTimeInISO = timeInIso,
                    endTimeInISO = timeInIso,
                    unitSystem = units?.value ?: Units.METRIC.value,
                    lon = coordinates.lon,
                    lat = coordinates.lat
                ).await() [0]


            remoteSelectedDailyForecast
        } catch (e: Exception) {
            throw e
        }
    }
}

