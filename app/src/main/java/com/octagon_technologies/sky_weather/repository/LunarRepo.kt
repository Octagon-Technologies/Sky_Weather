package com.octagon_technologies.sky_weather.repository

import com.octagon_technologies.sky_weather.repository.database.LunarDatabaseClass
import com.octagon_technologies.sky_weather.repository.database.MainDataBase
import com.octagon_technologies.sky_weather.network.LunarForecastRetrofitItem
import com.octagon_technologies.sky_weather.repository.network.lunar_forecast.LunarForecast
import com.octagon_technologies.sky_weather.ui.find_location.Coordinates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*


object LunarRepo {

    suspend fun getLunarForecastAsync(
        mainDataBase: MainDataBase?,
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


                insertLunarForecastToLocalStorage(mainDataBase, remoteLunarForecast)
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