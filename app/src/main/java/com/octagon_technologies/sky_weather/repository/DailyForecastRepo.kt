package com.octagon_technologies.sky_weather.repository

import com.octagon_technologies.sky_weather.models.Coordinates
import com.octagon_technologies.sky_weather.repository.database.DailyForecastDatabaseClass
import com.octagon_technologies.sky_weather.repository.database.WeatherDataBase
import com.octagon_technologies.sky_weather.repository.network.WeatherForecastRetrofitItem
import com.octagon_technologies.sky_weather.repository.network.daily_forecast.EachDailyForecast
import com.octagon_technologies.sky_weather.utils.StatusCode
import com.octagon_technologies.sky_weather.utils.Units
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

object DailyForecastRepo {
    suspend fun getDailyForecastAsync(
        weatherDataBase: WeatherDataBase?,
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
                    ) as ArrayList).apply {
                        removeLast()
                    }

                insertDailyForecastToLocalStorage(weatherDataBase, remoteHourlyForecast)
                Pair(StatusCode.Success, remoteHourlyForecast)
            } catch (e: HttpException) {
                Timber.e(e)
                Pair(StatusCode.ApiLimitExceeded, getLocalDailyForecastAsync(weatherDataBase))
            } catch (e: UnknownHostException) {
                Timber.e(e)
                Pair(StatusCode.NoNetwork, getLocalDailyForecastAsync(weatherDataBase))
            }
        }
    }

    private suspend fun getLocalDailyForecastAsync(weatherDataBase: WeatherDataBase?): ArrayList<EachDailyForecast>? {
        return withContext(Dispatchers.IO) {
            weatherDataBase?.dailyDao?.getDailyForecastDatabaseClass()?.dailyForecast
        }
    }

    private suspend fun insertDailyForecastToLocalStorage(
        weatherDataBase: WeatherDataBase?,
        remoteDailyForecast: ArrayList<EachDailyForecast>
    ) {
        withContext(Dispatchers.IO) {
            val dailyForecastDatabaseClass =
                DailyForecastDatabaseClass(dailyForecast = remoteDailyForecast)
            weatherDataBase?.dailyDao?.insertDailyForecastDatabaseClass(dailyForecastDatabaseClass)
        }
    }
}