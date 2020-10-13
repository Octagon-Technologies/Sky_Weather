package com.example.kotlinweatherapp.ui.weather_forecast_objects

import com.example.kotlinweatherapp.database.AllergyDatabaseClass
import com.example.kotlinweatherapp.database.MainDataBase
import com.example.kotlinweatherapp.network.AllergyForecastItem
import com.example.kotlinweatherapp.network.allergy_forecast.Allergy
import com.example.kotlinweatherapp.ui.find_location.Coordinates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

object MainAllergyForecastObject {

    suspend fun getAllergyValueAsync(mainDataBase: MainDataBase?, uiScope: CoroutineScope, coordinates: Coordinates): Allergy? {
        return try {
            val remoteAllergyForecast = AllergyForecastItem.allergyRetrofitService.getAllergyAsync(
                lat = coordinates.lat,
                lon = coordinates.lon
            ).await()

            uiScope.launch {
                insertAllergyForecastToLocalStorage(mainDataBase, remoteAllergyForecast)
            }

            remoteAllergyForecast
        }
        catch (e: HttpException) {
            Timber.e(e)
            getLocalAllergyForecastAsync(mainDataBase)
        }
        catch (e: UnknownHostException) {
            Timber.e(e)
            getLocalAllergyForecastAsync(mainDataBase)
        }
        catch (e: Exception) {
            throw e
        }
    }

    private suspend fun insertAllergyForecastToLocalStorage(mainDataBase: MainDataBase?, allergyForecast: Allergy) {
        withContext(Dispatchers.IO) {
            val allergyForecastDatabaseClass =
                AllergyDatabaseClass(allergyForecast = allergyForecast)
            mainDataBase?.allergyForecastDao?.insertAllergyForecastDatabaseClass(allergyForecastDatabaseClass)
        }
    }

    private suspend fun getLocalAllergyForecastAsync(mainDataBase: MainDataBase?): Allergy? {
        return withContext(Dispatchers.IO) {
            mainDataBase?.allergyForecastDao?.getAllergyForecastDatabaseClass()?.allergyForecast
        }
    }
}