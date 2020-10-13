package com.example.kotlinweatherapp.ui.weather_forecast_objects

import com.example.kotlinweatherapp.database.CurrentForecastDatabaseClass
import com.example.kotlinweatherapp.database.MainDataBase
import com.example.kotlinweatherapp.network.CurrentForecastItem
import com.example.kotlinweatherapp.network.current_forecast.SingleForecast
import com.example.kotlinweatherapp.ui.find_location.Coordinates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

object MainCurrentForecastObject {

    suspend fun getCurrentForecastAsync(uiScope: CoroutineScope, mainDataBase: MainDataBase?, coordinates: Coordinates): SingleForecast? {
        val ioScope = CoroutineScope(Dispatchers.IO)
        return try {
            val remoteSingleForecast =
                CurrentForecastItem.currentRetrofitService.getCurrentForecastAsync(
                    lat = coordinates.lat,
                    lon = coordinates.lon
                ).await()

            uiScope.launch {
                insertSingleForecastToLocalStorage(mainDataBase, remoteSingleForecast)
            }

            remoteSingleForecast
        }
        catch (e: HttpException) {
            Timber.i("getCurrentForecastAsync caused HttpException ")
            Timber.e(e)
            getLocalSingleForecastAsync(mainDataBase, ioScope)
        }
        catch (e: UnknownHostException) {
            Timber.i("getCurrentForecastAsync caused UnknownHostException ")
            Timber.e(e)
            getLocalSingleForecastAsync(mainDataBase, ioScope)
        }
        catch (t: Throwable) {
            Timber.i("getCurrentForecastAsync caused Throwable ")
            Timber.e(t)
            throw t
        }
    }

    private suspend fun insertSingleForecastToLocalStorage(roomDatabase: MainDataBase?, remoteSingleForecast: SingleForecast) {
        withContext(Dispatchers.IO) {
            val currentForecastDatabaseClass = CurrentForecastDatabaseClass(currentForecast = remoteSingleForecast)
            roomDatabase?.currentDao?.insertCurrentForecastDataClass(currentForecastDatabaseClass)
        }
    }

    private suspend fun getLocalSingleForecastAsync(roomDatabase: MainDataBase?, ioScope: CoroutineScope): SingleForecast? {
        return withContext(ioScope.coroutineContext) {
            val localSingleForecast = roomDatabase?.currentDao?.getCurrentForecastDataClass()?.currentForecast
            Timber.d("localSingleForecast is $localSingleForecast")
            localSingleForecast
        }
    }
}