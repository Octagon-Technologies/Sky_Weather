package com.example.kotlinweatherapp.ui.weather_forecast_objects

import com.example.kotlinweatherapp.database.MainDataBase
import com.example.kotlinweatherapp.database.SelectedSingleForecastDatabaseClass
import com.example.kotlinweatherapp.network.SelectedHourlyForecastItem
import com.example.kotlinweatherapp.network.current_forecast.SingleForecast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

object MainSelectedHourlyForecastObject {
    suspend fun getSelectedSingleForecastAsync(mainDataBase: MainDataBase?, timeInIso: String): SingleForecast? {
        return try {
//            val uiScope = CoroutineScope(Dispatchers.Main)
            val remoteLunarForecast =
                SelectedHourlyForecastItem.selectedHourlyRetrofitService.getSelectedHourlyForecastAsync(
                    startTimeInISO = timeInIso,
                    endTimeInISO = timeInIso
                )
                    .await()

//            uiScope.launch {
//                insertSelectedHourlyForecastToLocalStorage(mainDataBase, remoteLunarForecast)
//            }

            remoteLunarForecast
        }
        catch (e: HttpException) {
            Timber.e(e)
            null
        }
        catch (e: UnknownHostException) {
            Timber.e(e)
            null
            //getLocalSelectedHourlyForecastAsync(mainDataBase)
        }
        catch (e: Exception) {
            throw e
        }
    }


//    private suspend fun insertSelectedHourlyForecastToLocalStorage(mainDataBase: MainDataBase?, singleForecast: SingleForecast) {
//        withContext(Dispatchers.IO) {
//            val selectedSingleForecastDatabaseClass =
//                SelectedSingleForecastDatabaseClass(selectedSingleForecastForecast = singleForecast)
//            mainDataBase?.selectedSingleForecastDao?.insertSelectedSingleForecastDatabaseClass(selectedSingleForecastDatabaseClass)
//        }
//    }
//
//    private suspend fun getLocalSelectedHourlyForecastAsync(mainDataBase: MainDataBase?): SingleForecast? {
//        return withContext(Dispatchers.IO) {
//            mainDataBase?.selectedSingleForecastDao?.getSelectedSingleForecastDatabaseClass()?.selectedSingleForecastForecast
//        }
//    }
}