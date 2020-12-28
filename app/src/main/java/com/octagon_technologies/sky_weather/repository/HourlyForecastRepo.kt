package com.octagon_technologies.sky_weather.repository

import com.octagon_technologies.sky_weather.models.Coordinates
import com.octagon_technologies.sky_weather.repository.database.HourlyForecastDatabaseClass
import com.octagon_technologies.sky_weather.repository.database.WeatherDataBase
import com.octagon_technologies.sky_weather.repository.network.WeatherForecastRetrofitItem
import com.octagon_technologies.sky_weather.repository.network.hourly_forecast.EachHourlyForecast
import com.octagon_technologies.sky_weather.utils.StatusCode
import com.octagon_technologies.sky_weather.utils.Units
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

object HourlyForecastRepo {
    suspend fun getHourlyForecastAsync(
        weatherDataBase: WeatherDataBase?,
        coordinates: Coordinates,
        units: Units?
    ): Pair<StatusCode, ArrayList<EachHourlyForecast>?> {
        return withContext(Dispatchers.IO) {
            try {
                val remoteHourlyForecast =
                    WeatherForecastRetrofitItem.weatherRetrofitService.getHourlyForecastAsync(
                        lat = coordinates.lat,
                        lon = coordinates.lon,
                        unitSystem = units?.value ?: Units.METRIC.value
                    ) as ArrayList<EachHourlyForecast>
                remoteHourlyForecast.removeAt(0)
                insertHourlyForecastToLocalStorage(weatherDataBase, remoteHourlyForecast)

                Pair(StatusCode.Success, remoteHourlyForecast)
            } catch (e: HttpException) {
                Timber.e(e)
                Pair(StatusCode.ApiLimitExceeded, getLocalHourlyForecastAsync(weatherDataBase))
            } catch (e: UnknownHostException) {
                Timber.e(e)
                Pair(StatusCode.NoNetwork, getLocalHourlyForecastAsync(weatherDataBase))
            }
        }
    }


    private suspend fun getLocalHourlyForecastAsync(weatherDataBase: WeatherDataBase?): ArrayList<EachHourlyForecast>? {
        return withContext(Dispatchers.IO) {
            weatherDataBase?.hourlyDao?.getHourlyForecastDatabaseClass()?.hourlyForecast
        }
    }

    private suspend fun insertHourlyForecastToLocalStorage(
        weatherDataBase: WeatherDataBase?,
        remoteHourlyForecast: ArrayList<EachHourlyForecast>
    ) {
        withContext(Dispatchers.IO) {
            val hourlyForecastDatabaseClass =
                HourlyForecastDatabaseClass(hourlyForecast = remoteHourlyForecast)
            weatherDataBase?.hourlyDao?.insertHourlyForecastDatabaseClass(hourlyForecastDatabaseClass)
        }
    }
}