package com.example.kotlinweatherapp.repository

import android.content.Context
import com.example.kotlinweatherapp.database.AllergyDatabaseClass
import com.example.kotlinweatherapp.database.CurrentForecastDatabaseClass
import com.example.kotlinweatherapp.database.LunarDatabaseClass
import com.example.kotlinweatherapp.database.MainDataBase
import com.example.kotlinweatherapp.network.*
import com.example.kotlinweatherapp.ui.find_location.Coordinates
import com.example.kotlinweatherapp.ui.weather_forecast_objects.MainLocationObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class WeatherRepo(context: Context) {
    private var formattedDate: String = SimpleDateFormat("YYYYMMdd", Locale.getDefault()).format(System.currentTimeMillis())
    private val mainDataBase = MainDataBase.getInstance(context)
    private lateinit var coordinates: Coordinates
    private val uiScope = CoroutineScope(Dispatchers.Main)

    init {
        uiScope.launch {
            getCoordinates()
        }
    }

    private suspend fun getCoordinates() {
        val location =
            MainLocationObject.getLocalLocationAsync(mainDataBase)
        coordinates = Coordinates(location?.lon?.toDouble() ?: mockLon, location?.lat?.toDouble() ?: mockLat)
    }

    suspend fun refreshData() {
        val remoteAllergyForecast = AllergyForecastItem.allergyRetrofitService.getAllergyAsync(
            lat = coordinates.lat,
            lon = coordinates.lon
        ).await()
        val remoteLunarForecast = LunarForecastItem.lunarRetrofitService.getLunarForecastAsync(
            date = formattedDate,
            lat = coordinates.lat,
            lon = coordinates.lon
            ).await()
        val remoteCurrentForecast = CurrentForecastItem.currentRetrofitService.getCurrentForecastAsync(
            lat = coordinates.lat,
            lon = coordinates.lon
        ).await()

        mainDataBase?.lunarForecastDao?.insertLunarForecastDatabaseClass(LunarDatabaseClass(lunarForecast = remoteLunarForecast))
        mainDataBase?.allergyForecastDao?.insertAllergyForecastDatabaseClass(AllergyDatabaseClass(allergyForecast = remoteAllergyForecast))
        mainDataBase?.currentDao?.insertCurrentForecastDataClass(CurrentForecastDatabaseClass(currentForecast = remoteCurrentForecast))
    }

}