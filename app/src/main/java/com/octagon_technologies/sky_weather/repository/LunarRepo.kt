package com.octagon_technologies.sky_weather.repository

import com.octagon_technologies.sky_weather.repository.database.LunarDatabaseClass
import com.octagon_technologies.sky_weather.repository.database.WeatherDataBase
import com.octagon_technologies.sky_weather.network.LunarForecastRetrofitItem
import com.octagon_technologies.sky_weather.repository.network.lunar_forecast.LunarForecast
import com.octagon_technologies.sky_weather.models.Coordinates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*


object LunarRepo {

    suspend fun getLunarForecastAsync(
        weatherDataBase: WeatherDataBase?,
        coordinates: Coordinates,
        dateInMillis: Long = System.currentTimeMillis()
    ): LunarForecast? {
        return withContext(Dispatchers.IO) {
            try {
                val formattedDate =
                    SimpleDateFormat(
                        "yyyyMMdd",
                        Locale.getDefault()
                    ).format(dateInMillis)
                val timeZone =
                    (TimeZone.getDefault().getOffset(Date().time).toDouble() / 3_600_000.0).toInt()
                        .toString()
                Timber.d("Timezone is $timeZone")

                val remoteLunarForecast =
                    LunarForecastRetrofitItem.lunarRetrofitService.getLunarForecastAsync(
                        date = formattedDate,
                        lat = coordinates.lat,
                        lon = coordinates.lon,
                        timezone = timeZone
                    ).await()


                insertLunarForecastToLocalStorage(weatherDataBase, remoteLunarForecast)
                remoteLunarForecast
            } catch (e: HttpException) {
                Timber.e(e)
                getLocalLunarForecastAsync(weatherDataBase)
            } catch (e: UnknownHostException) {
                Timber.e(e)
                getLocalLunarForecastAsync(weatherDataBase)
            } catch (e: Exception) {
                throw e
            }
        }
    }


    private suspend fun insertLunarForecastToLocalStorage(
        weatherDataBase: WeatherDataBase?,
        lunarForecast: LunarForecast
    ) {
        withContext(Dispatchers.IO) {
            val lunarForecastDatabaseClass =
                LunarDatabaseClass(lunarForecast = lunarForecast)
            weatherDataBase?.lunarForecastDao?.insertLunarForecastDatabaseClass(
                lunarForecastDatabaseClass
            )
        }
    }

    private suspend fun getLocalLunarForecastAsync(weatherDataBase: WeatherDataBase?): LunarForecast? {
        return withContext(Dispatchers.IO) {
            weatherDataBase?.lunarForecastDao?.getLunarForecastDatabaseClass()?.lunarForecast
        }
    }
}