package com.octagon_technologies.sky_weather.ui.shared_code

import com.octagon_technologies.sky_weather.Units
import com.octagon_technologies.sky_weather.database.CurrentForecastDatabaseClass
import com.octagon_technologies.sky_weather.database.MainDataBase
import com.octagon_technologies.sky_weather.network.WeatherForecastRetrofitItem
import com.octagon_technologies.sky_weather.network.single_forecast.SingleForecast
import com.octagon_technologies.sky_weather.ui.find_location.Coordinates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

object MainCurrentForecastObject {

    suspend fun getCurrentForecastAsync(
        mainDataBase: MainDataBase?,
        coordinates: Coordinates,
        units: Units?
    ): SingleForecast? {
        val ioScope = CoroutineScope(Dispatchers.IO)
        return withContext(Dispatchers.Default) {
            try {
                val remoteSingleForecast =
                    WeatherForecastRetrofitItem.weatherRetrofitService.getCurrentForecastAsync(
                        lat = coordinates.lat,
                        lon = coordinates.lon,
                        unitSystem = units?.value ?: Units.METRIC.value
                    ).await()

                insertSingleForecastToLocalStorage(mainDataBase, remoteSingleForecast)

                remoteSingleForecast
            } catch (e: HttpException) {
                Timber.i("getCurrentForecastAsync caused HttpException ")
                Timber.e(e)
                getLocalSingleForecastAsync(mainDataBase, ioScope)
            } catch (e: UnknownHostException) {
                Timber.i("getCurrentForecastAsync caused UnknownHostException ")
                Timber.e(e)
                getLocalSingleForecastAsync(mainDataBase, ioScope)
            } catch (t: Throwable) {
                Timber.i("getCurrentForecastAsync caused Throwable ")
                Timber.e(t)
                throw t
            }
        }
    }

    private suspend fun insertSingleForecastToLocalStorage(
        roomDatabase: MainDataBase?,
        remoteSingleForecast: SingleForecast
    ) {
        withContext(Dispatchers.IO) {
            val currentForecastDatabaseClass =
                CurrentForecastDatabaseClass(currentForecast = remoteSingleForecast)
            roomDatabase?.currentDao?.insertCurrentForecastDataClass(currentForecastDatabaseClass)
        }
    }

    private suspend fun getLocalSingleForecastAsync(
        roomDatabase: MainDataBase?,
        ioScope: CoroutineScope
    ): SingleForecast? {
        return withContext(ioScope.coroutineContext) {
            val localSingleForecast =
                roomDatabase?.currentDao?.getCurrentForecastDataClass()?.currentForecast
            Timber.d("localSingleForecast is $localSingleForecast")
            localSingleForecast
        }
    }
}