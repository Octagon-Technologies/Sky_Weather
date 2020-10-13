package com.example.kotlinweatherapp.ui.weather_forecast_objects

import com.example.kotlinweatherapp.database.HourlyForecastDatabaseClass
import com.example.kotlinweatherapp.database.MainDataBase
import com.example.kotlinweatherapp.network.HourlyForecastItem
import com.example.kotlinweatherapp.network.SelectedHourlyForecastItem
import com.example.kotlinweatherapp.network.current_forecast.SingleForecast
import com.example.kotlinweatherapp.network.hourly_forecast.EachHourlyForecast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

object MainHourlyForecastObject {
    suspend fun getHourlyForecastAsync(mainDataBase: MainDataBase?): ArrayList<EachHourlyForecast>? {
        return try {
            val remoteHourlyForecast = HourlyForecastItem.hourlyRetrofitService.getHourlyForecastAsync().await() as ArrayList<EachHourlyForecast>
            insertHourlyForecastToLocalStorage(mainDataBase, remoteHourlyForecast)

            remoteHourlyForecast
        }
        catch (e: HttpException) {
            Timber.e(e)
            getLocalHourlyForecastAsync(mainDataBase)
        }
        catch (e: UnknownHostException) {
            Timber.e(e)
            getLocalHourlyForecastAsync(mainDataBase)
        }
        catch (e: Exception) {
            throw e
        }
    }

    private suspend fun getLocalHourlyForecastAsync(mainDataBase: MainDataBase?) : ArrayList<EachHourlyForecast>?  {
        return withContext(Dispatchers.IO) {
            mainDataBase?.hourlyDao?.getHourlyForecastDatabaseClass()?.hourlyForecast
        }
    }

    private suspend fun insertHourlyForecastToLocalStorage(mainDataBase: MainDataBase?, remoteHourlyForecast: ArrayList<EachHourlyForecast>) {
        withContext(Dispatchers.IO) {
            val hourlyForecastDatabaseClass = HourlyForecastDatabaseClass(hourlyForecast = remoteHourlyForecast)
            mainDataBase?.hourlyDao?.insertHourlyForecastDatabaseClass(hourlyForecastDatabaseClass)
        }
    }
}