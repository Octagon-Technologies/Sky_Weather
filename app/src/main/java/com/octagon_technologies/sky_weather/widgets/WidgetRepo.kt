package com.octagon_technologies.sky_weather.widgets

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.remote_views.CustomRemoteView
import com.octagon_technologies.sky_weather.repository.SettingsRepo
import com.octagon_technologies.sky_weather.repository.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.repository.network.single_forecast.SingleForecast
import com.octagon_technologies.sky_weather.utils.getCoordinates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class WidgetRepo(private val context: Context) {
    private val settingsRepo by lazy { SettingsRepo(context) }
    private val widgetSettings by lazy { WidgetSettings(context) }
    private val appWidgetManager: AppWidgetManager = AppWidgetManager.getInstance(context)

    fun createWidget(widgetId: Int, transparencyOutOf255: Int, reverseGeoCodingLocation: ReverseGeoCodingLocation?, onComplete: (Boolean) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val location = reverseGeoCodingLocation ?: run {
                Timber.d("reverseGeoCodingLocation.value is $reverseGeoCodingLocation.value in addWidget()")
                onComplete(false)
                return@launch
            }

            val widgetData = WidgetData(
                widgetId = widgetId,
                reverseGeoCodingLocation = location,
                transparencyOutOf255 = transparencyOutOf255,
                timeFormat = settingsRepo.getTimeFormat(),
                units = settingsRepo.getUnits()
            )
            widgetSettings.addWidgetId(widgetData)
            updateSingleWidget(widgetData)
            onComplete(true)
        }
    }

    fun updateAllWidgets(paramAppWidgetIds: IntArray? = null) {
        val cn = ComponentName(
            "com.octagon_technologies.sky_weather",
            "com.octagon_technologies.sky_weather.widgets.WeatherWidget"
        )
        val appWidgetIds = (paramAppWidgetIds ?: appWidgetManager.getAppWidgetIds(cn)).asList()
        Timber.d("appWidgetIds is $appWidgetIds")

        GlobalScope.launch(Dispatchers.IO) {
            val widgetsToUpdate = widgetSettings.getAllWidgets()
                .filter { it.widgetId in appWidgetIds }
            Timber.d("widgetsToUpdate is $widgetsToUpdate")

            widgetsToUpdate.forEach {
                updateSingleWidget(it)
            }
        }
    }

    private suspend fun updateSingleWidget(widgetData: WidgetData) {
        Timber.d("Updating widget in updateSingleWidget with widgetData as $widgetData")
        val widgetForecast = widgetSettings.getWeatherForecastFromWidgetData(
            widgetData.reverseGeoCodingLocation?.getCoordinates(),
            widgetData.units
        )

        // Construct the RemoteViews object
        val widgetRemoteView = CustomRemoteView(context).getCustomRemoteView(
            widgetForecast,
            widgetData.reverseGeoCodingLocation,
            widgetData.timeFormat
        )

        widgetRemoteView.setInt(
            R.id.base_layout, "setBackgroundColor", widgetData.transparencyOutOf255
        )

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(widgetData.widgetId, widgetRemoteView)
    }

    suspend fun getWeatherForecastFromWidgetData(reverseGeoCodingLocation: ReverseGeoCodingLocation?): Result<SingleForecast> {
        val coordinates = reverseGeoCodingLocation?.getCoordinates() ?: run {
            Timber.d("coordinates is null in loadDataForWidget")
            return Result.failure(NullPointerException("Location has not been selected"))
        }

        val widgetWeatherForecast =
            widgetSettings.getWeatherForecastFromWidgetData(coordinates, settingsRepo.getUnits())
                ?: run {
                    return Result.failure(NullPointerException("Getting weather result failed"))
                }

        Timber.d("widgetWeatherForecast is $widgetWeatherForecast in getWeatherForecastFromWidgetData()")
        return Result.success(widgetWeatherForecast)
    }

}