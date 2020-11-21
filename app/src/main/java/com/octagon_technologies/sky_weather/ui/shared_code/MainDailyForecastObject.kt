package com.octagon_technologies.sky_weather.ui.shared_code

import com.octagon_technologies.sky_weather.Units
import com.octagon_technologies.sky_weather.database.DailyForecastDatabaseClass
import com.octagon_technologies.sky_weather.database.MainDataBase
import com.octagon_technologies.sky_weather.network.WeatherForecastRetrofitItem
import com.octagon_technologies.sky_weather.network.daily_forecast.EachDailyForecast
import com.octagon_technologies.sky_weather.network.hourly_forecast.EachHourlyForecast
import com.octagon_technologies.sky_weather.ui.find_location.Coordinates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

object MainDailyForecastObject {
    suspend fun getDailyForecastAsync(
        mainDataBase: MainDataBase?,
        coordinates: Coordinates,
        units: Units?
    ): ArrayList<EachDailyForecast>? {
        return try {
            val remoteHourlyForecast =
                (WeatherForecastRetrofitItem.weatherRetrofitService.getDailyForecastAsync(
                    lat = coordinates.lat,
                    lon = coordinates.lon,
                    unitSystem = units?.value ?: Units.METRIC.value
                ).await() as ArrayList).apply {
                    removeLast()
                }

            insertDailyForecastToLocalStorage(mainDataBase, remoteHourlyForecast)
            remoteHourlyForecast
        } catch (e: HttpException) {
            Timber.e(e)
            getLocalDailyForecastAsync(mainDataBase)
        } catch (e: UnknownHostException) {
            Timber.e(e)
            getLocalDailyForecastAsync(mainDataBase)
        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun getLocalDailyForecastAsync(mainDataBase: MainDataBase?): ArrayList<EachDailyForecast>? {
        return withContext(Dispatchers.IO) {
            mainDataBase?.dailyDao?.getDailyForecastDatabaseClass()?.dailyForecast
        }
    }

    private suspend fun insertDailyForecastToLocalStorage(
        mainDataBase: MainDataBase?,
        remoteDailyForecast: ArrayList<EachDailyForecast>
    ) {
        withContext(Dispatchers.IO) {
            val dailyForecastDatabaseClass =
                DailyForecastDatabaseClass(dailyForecast = remoteDailyForecast)
            mainDataBase?.dailyDao?.insertDailyForecastDatabaseClass(dailyForecastDatabaseClass)
        }
    }
}