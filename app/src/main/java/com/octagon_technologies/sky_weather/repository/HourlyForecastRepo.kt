package com.octagon_technologies.sky_weather.repository

import com.octagon_technologies.sky_weather.StatusCode
import com.octagon_technologies.sky_weather.Units
import com.octagon_technologies.sky_weather.network.WeatherForecastRetrofitItem
import com.octagon_technologies.sky_weather.repository.database.HourlyForecastDatabaseClass
import com.octagon_technologies.sky_weather.repository.database.MainDataBase
import com.octagon_technologies.sky_weather.repository.network.hourly_forecast.EachHourlyForecast
import com.octagon_technologies.sky_weather.ui.find_location.Coordinates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

object HourlyForecastRepo {
    suspend fun getHourlyForecastAsync(
        mainDataBase: MainDataBase?,
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
                    ).await() as ArrayList<EachHourlyForecast>
                remoteHourlyForecast.removeAt(0)
                insertHourlyForecastToLocalStorage(mainDataBase, remoteHourlyForecast)

                Pair(StatusCode.Success, remoteHourlyForecast)
            } catch (e: HttpException) {
                Timber.e(e)
                Pair(StatusCode.ApiLimitExceeded, getLocalHourlyForecastAsync(mainDataBase))
            } catch (e: UnknownHostException) {
                Timber.e(e)
                Pair(StatusCode.NoNetwork, getLocalHourlyForecastAsync(mainDataBase))
            }
        }
    }


    private suspend fun getLocalHourlyForecastAsync(mainDataBase: MainDataBase?): ArrayList<EachHourlyForecast>? {
        return withContext(Dispatchers.IO) {
            mainDataBase?.hourlyDao?.getHourlyForecastDatabaseClass()?.hourlyForecast
        }
    }

    private suspend fun insertHourlyForecastToLocalStorage(
        mainDataBase: MainDataBase?,
        remoteHourlyForecast: ArrayList<EachHourlyForecast>
    ) {
        withContext(Dispatchers.IO) {
            val hourlyForecastDatabaseClass =
                HourlyForecastDatabaseClass(hourlyForecast = remoteHourlyForecast)
            mainDataBase?.hourlyDao?.insertHourlyForecastDatabaseClass(hourlyForecastDatabaseClass)
        }
    }
}