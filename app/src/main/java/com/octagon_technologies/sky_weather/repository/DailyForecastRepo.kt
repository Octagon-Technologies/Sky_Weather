package com.octagon_technologies.sky_weather.repository

import com.octagon_technologies.sky_weather.utils.StatusCode
import com.octagon_technologies.sky_weather.utils.Units
import com.octagon_technologies.sky_weather.network.WeatherForecastRetrofitItem
import com.octagon_technologies.sky_weather.repository.database.DailyForecastDatabaseClass
import com.octagon_technologies.sky_weather.repository.database.MainDataBase
import com.octagon_technologies.sky_weather.repository.network.daily_forecast.EachDailyForecast
import com.octagon_technologies.sky_weather.ui.find_location.Coordinates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

object DailyForecastRepo {
    suspend fun getDailyForecastAsync(
        mainDataBase: MainDataBase?,
        coordinates: Coordinates,
        units: Units?
    ): Pair<StatusCode, ArrayList<EachDailyForecast>?> {
        return withContext(Dispatchers.IO) {
            try {
                val remoteHourlyForecast =
                    (WeatherForecastRetrofitItem.weatherRetrofitService.getDailyForecastAsync(
                        lat = coordinates.lat,
                        lon = coordinates.lon,
                        unitSystem = units?.value ?: Units.METRIC.value
                    ).await() as ArrayList).apply {
                        removeLast()
                    }

                insertDailyForecastToLocalStorage(mainDataBase, remoteHourlyForecast)
                Pair(StatusCode.Success, remoteHourlyForecast)
            } catch (e: HttpException) {
                Timber.e(e)
                Pair(StatusCode.ApiLimitExceeded, getLocalDailyForecastAsync(mainDataBase))
            } catch (e: UnknownHostException) {
                Timber.e(e)
                Pair(StatusCode.NoNetwork, getLocalDailyForecastAsync(mainDataBase))
            }
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