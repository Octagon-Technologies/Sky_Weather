package com.octagon_technologies.sky_weather.ui.shared_code

import com.octagon_technologies.sky_weather.database.AllergyDatabaseClass
import com.octagon_technologies.sky_weather.database.MainDataBase
import com.octagon_technologies.sky_weather.network.AllergyForecastRetrofitItem
import com.octagon_technologies.sky_weather.network.allergy_forecast.Allergy
import com.octagon_technologies.sky_weather.ui.find_location.Coordinates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

object MainAllergyForecastObject {

    suspend fun getAllergyValueAsync(
        mainDataBase: MainDataBase?,
        coordinates: Coordinates
    ): Allergy? {
        return withContext(Dispatchers.IO) {
            try {
                val remoteAllergyForecast =
                    AllergyForecastRetrofitItem.allergyRetrofitService.getAllergyAsync(
                        lat = coordinates.lat,
                        lon = coordinates.lon
                    ).await()

                insertAllergyForecastToLocalStorage(mainDataBase, remoteAllergyForecast)

                remoteAllergyForecast
            } catch (e: HttpException) {
                Timber.e(e)
                getLocalAllergyForecastAsync(mainDataBase)
            } catch (e: UnknownHostException) {
                Timber.e(e)
                getLocalAllergyForecastAsync(mainDataBase)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    private suspend fun insertAllergyForecastToLocalStorage(
        mainDataBase: MainDataBase?,
        allergyForecast: Allergy
    ) {
        withContext(Dispatchers.IO) {
            val allergyForecastDatabaseClass =
                AllergyDatabaseClass(allergyForecast = allergyForecast)
            mainDataBase?.allergyForecastDao?.insertAllergyForecastDatabaseClass(
                allergyForecastDatabaseClass
            )
        }
    }

    private suspend fun getLocalAllergyForecastAsync(mainDataBase: MainDataBase?): Allergy? {
        return withContext(Dispatchers.IO) {
            mainDataBase?.allergyForecastDao?.getAllergyForecastDatabaseClass()?.allergyForecast
        }
    }
}