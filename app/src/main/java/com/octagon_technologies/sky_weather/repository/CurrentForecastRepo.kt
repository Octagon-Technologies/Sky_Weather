package com.octagon_technologies.sky_weather.repository

import com.octagon_technologies.sky_weather.utils.StatusCode
import com.octagon_technologies.sky_weather.utils.Units
import com.octagon_technologies.sky_weather.network.WeatherForecastRetrofitItem
import com.octagon_technologies.sky_weather.repository.database.CurrentForecastDatabaseClass
import com.octagon_technologies.sky_weather.repository.database.MainDataBase
import com.octagon_technologies.sky_weather.repository.network.single_forecast.SingleForecast
import com.octagon_technologies.sky_weather.ui.find_location.Coordinates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

object CurrentForecastRepo {

    suspend fun getCurrentForecastAsync(
        mainDataBase: MainDataBase?,
        coordinates: Coordinates,
        units: Units?
    ): Pair<StatusCode, SingleForecast?> {
        return withContext(Dispatchers.IO) {
            try {
                val remoteSingleForecast =
                    WeatherForecastRetrofitItem.weatherRetrofitService.getCurrentForecastAsync(
                        lat = coordinates.lat,
                        lon = coordinates.lon,
                        unitSystem = units?.value ?: Units.METRIC.value
                    ).await()

                insertSingleForecastToLocalStorage(mainDataBase, remoteSingleForecast)

                Pair(StatusCode.Success, remoteSingleForecast)
            } catch (e: HttpException) {
                Timber.i("getCurrentForecastAsync caused HttpException ")
                Pair(StatusCode.ApiLimitExceeded, getLocalSingleForecastAsync(mainDataBase))
            } catch (e: UnknownHostException) {
                Timber.e(e, "getCurrentForecastAsync caused UnknownHostException ")
                Pair(StatusCode.NoNetwork, getLocalSingleForecastAsync(mainDataBase))
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
        roomDatabase: MainDataBase?
    ): SingleForecast? {
        return withContext(Dispatchers.IO) {
            val localSingleForecast =
                roomDatabase?.currentDao?.getCurrentForecastDataClass()?.currentForecast
            Timber.d("localSingleForecast is $localSingleForecast")
            localSingleForecast
        }
    }
}