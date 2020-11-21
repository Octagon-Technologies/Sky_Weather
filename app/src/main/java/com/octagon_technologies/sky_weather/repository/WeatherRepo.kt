package com.octagon_technologies.sky_weather.repository

import android.content.Context
import com.octagon_technologies.sky_weather.database.MainDataBase
import com.octagon_technologies.sky_weather.ui.find_location.Coordinates
import com.octagon_technologies.sky_weather.ui.shared_code.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class WeatherRepo(context: Context) {
    private val mainDataBase: MainDataBase? by lazy { MainDataBase.getInstance(context) }
    private val mainSettings: MainSettings by lazy { MainSettings(context) }

    suspend fun refreshData() {
        val location =
            MainLocationObject.getLocalLocationAsync(mainDataBase).apply {
                Timber.d("location in work is $this")
            }
        val units = mainSettings.getUnits()
        val coordinates =
            Coordinates(location?.lon?.toDouble() ?: return, location.lat?.toDouble() ?: return)

        Timber.d("Retrieval of location suceeded with it as $location")
        MainLunarForecastObject.getLunarForecastAsync(mainDataBase, coordinates)
        MainAllergyForecastObject.getAllergyValueAsync(mainDataBase, coordinates)
        MainCurrentForecastObject.getCurrentForecastAsync(mainDataBase, coordinates, units)
        MainHourlyForecastObject.getHourlyForecastAsync(mainDataBase, coordinates, units)
        MainDailyForecastObject.getDailyForecastAsync(mainDataBase, coordinates, units)
    }

}