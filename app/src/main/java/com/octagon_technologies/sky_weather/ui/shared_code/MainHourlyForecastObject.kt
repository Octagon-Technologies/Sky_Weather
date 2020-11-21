package com.octagon_technologies.sky_weather.ui.shared_code

import com.octagon_technologies.sky_weather.Units
import com.octagon_technologies.sky_weather.database.HourlyForecastDatabaseClass
import com.octagon_technologies.sky_weather.database.MainDataBase
import com.octagon_technologies.sky_weather.network.WeatherForecastRetrofitItem
import com.octagon_technologies.sky_weather.network.hourly_forecast.EachHourlyForecast
import com.octagon_technologies.sky_weather.ui.find_location.Coordinates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

object MainHourlyForecastObject {
    suspend fun getHourlyForecastAsync(
        mainDataBase: MainDataBase?,
        coordinates: Coordinates,
        units: Units?
    ): ArrayList<EachHourlyForecast>? {
        return try {
            val remoteHourlyForecast =
                WeatherForecastRetrofitItem.weatherRetrofitService.getHourlyForecastAsync(
                    lat = coordinates.lat,
                    lon = coordinates.lon,
                    unitSystem = units?.value ?: Units.METRIC.value
                ).await() as ArrayList<EachHourlyForecast>
            remoteHourlyForecast.removeAt(0)
            insertHourlyForecastToLocalStorage(mainDataBase, remoteHourlyForecast)

            remoteHourlyForecast
        } catch (e: HttpException) {
            Timber.e(e)
            getLocalHourlyForecastAsync(mainDataBase)
        } catch (e: UnknownHostException) {
            Timber.e(e)
            getLocalHourlyForecastAsync(mainDataBase)
        } catch (e: Exception) {
            throw e
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