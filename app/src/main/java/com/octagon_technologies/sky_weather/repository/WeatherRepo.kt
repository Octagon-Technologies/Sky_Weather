package com.octagon_technologies.sky_weather.repository

import android.content.Context
import com.octagon_technologies.sky_weather.notification.CustomNotificationCompat
import com.octagon_technologies.sky_weather.repository.database.WeatherDataBase
import com.octagon_technologies.sky_weather.models.Coordinates
import timber.log.Timber

class WeatherRepo(context: Context) {
    private val mainDataBase by lazy { WeatherDataBase.getInstance(context) }
    private val mainSettings by lazy { SettingsRepo(context) }
    private val customNotificationCompat by lazy { CustomNotificationCompat(context) }

    suspend fun refreshData() {
        val location =
            LocationRepo.getLocalLocationAsync(mainDataBase).apply {
                Timber.d("location in work is $this")
            }
        val units = mainSettings.getUnits()
        val timeFormat = mainSettings.getTimeFormat()
        val coordinates =
            Coordinates(location?.lon?.toDouble() ?: return, location.lat?.toDouble() ?: return)

        Timber.d("Retrieval of location suceeded with it as $location")
        LunarRepo.getLunarForecastAsync(mainDataBase, coordinates)
        AllergyRepo.getAllergyValueAsync(mainDataBase, coordinates)
        HourlyForecastRepo.getHourlyForecastAsync(mainDataBase, coordinates, units)
        DailyForecastRepo.getDailyForecastAsync(mainDataBase, coordinates, units)

        CurrentForecastRepo.getCurrentForecastAsync(
            mainDataBase,
            coordinates,
            units
        ).second?.apply {
            if (mainSettings.getNotificationAllowed())
                customNotificationCompat.createNotification(this, location, timeFormat)
        }
    }

}