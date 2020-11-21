package com.octagon_technologies.sky_weather.ui.shared_code

import com.octagon_technologies.sky_weather.Units
import com.octagon_technologies.sky_weather.network.WeatherForecastRetrofitItem
import com.octagon_technologies.sky_weather.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.network.single_forecast.SingleForecast
import com.octagon_technologies.sky_weather.ui.find_location.Coordinates

object MainSelectedHourlyForecastObject {
    suspend fun getSelectedSingleForecastAsync(
        coordinates: Coordinates,
        timeInIso: String,
        units: Units?
    ): SingleForecast? {
        return try {
            val remoteSelectedSingleForecast =
                WeatherForecastRetrofitItem.weatherRetrofitService.getSelectedHourlyForecastAsync(
                    startTimeInISO = timeInIso,
                    endTimeInISO = timeInIso,
                    unitSystem = units?.value ?: Units.METRIC.value,
                    lon = coordinates.lon,
                    lat = coordinates.lat
                ).await()[0]

            remoteSelectedSingleForecast
        } catch (e: Exception) {
            throw e
        }

    }
}