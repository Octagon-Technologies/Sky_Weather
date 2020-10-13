package com.example.kotlinweatherapp.ui.weather_forecast_objects

import com.example.kotlinweatherapp.database.LunarDatabaseClass
import com.example.kotlinweatherapp.database.MainDataBase
import com.example.kotlinweatherapp.network.LunarForecastItem
import com.example.kotlinweatherapp.network.lunar_forecast.LunarForecast
import com.example.kotlinweatherapp.ui.find_location.Coordinates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*


object MainLunarForecastObject {

    suspend fun getLunarForecastAsync(
        mainDataBase: MainDataBase?,
        uiScope: CoroutineScope,
        coordinates: Coordinates
    ): LunarForecast? {
        return try {
            val formattedDate =
                SimpleDateFormat("YYYYMMdd", Locale.getDefault()).format(System.currentTimeMillis())
            val remoteLunarForecast =
                LunarForecastItem.lunarRetrofitService.getLunarForecastAsync(
                    date = formattedDate,
                    lat = coordinates.lat,
                    lon = coordinates.lon
                )
                    .await()

            uiScope.launch {
                insertLunarForecastToLocalStorage(mainDataBase, remoteLunarForecast)
            }

            remoteLunarForecast
        } catch (e: HttpException) {
            Timber.e(e)
            getLocalLunarForecastAsync(mainDataBase)
        } catch (e: UnknownHostException) {
            Timber.e(e)
            getLocalLunarForecastAsync(mainDataBase)
        } catch (e: Exception) {
            throw e
        }
    }


    private suspend fun insertLunarForecastToLocalStorage(
        mainDataBase: MainDataBase?,
        lunarForecast: LunarForecast
    ) {
        withContext(Dispatchers.IO) {
            val lunarForecastDatabaseClass =
                LunarDatabaseClass(lunarForecast = lunarForecast)
            mainDataBase?.lunarForecastDao?.insertLunarForecastDatabaseClass(
                lunarForecastDatabaseClass
            )
        }
    }

    private suspend fun getLocalLunarForecastAsync(mainDataBase: MainDataBase?): LunarForecast? {
        return withContext(Dispatchers.IO) {
            mainDataBase?.lunarForecastDao?.getLunarForecastDatabaseClass()?.lunarForecast
        }
    }
}