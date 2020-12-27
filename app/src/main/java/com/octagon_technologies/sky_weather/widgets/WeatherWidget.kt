package com.octagon_technologies.sky_weather.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.remote_views.CustomRemoteView
import com.octagon_technologies.sky_weather.repository.network.single_forecast.SingleForecast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [WidgetConfigureActivity]
 */
class WeatherWidget : AppWidgetProvider() {
    lateinit var widgetSettings: WidgetSettings
    private lateinit var widgetRepo: WidgetRepo

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        widgetRepo = WidgetRepo(context)
        widgetRepo.updateAllWidgets(appWidgetIds)

        Timber.d("onUpdate() called in widget with appWidgetIds is ${appWidgetIds.asList()}")
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        GlobalScope.launch {
            appWidgetIds.forEach {
                getInitWidgetSettings(context).removeWidgetId(it)
            }
        }
        Timber.d("onDeleted() called in widget with appWidgetIds is ${appWidgetIds.asList()}")
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        Timber.d("onEnabled called")
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
        Timber.d("onDisabled called")
    }

    private fun getInitWidgetSettings(context: Context) =
        if (this::widgetSettings.isInitialized) widgetSettings
        else {
            widgetSettings = WidgetSettings(context)
            widgetSettings
        }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        widgetForecast: SingleForecast?,
        widgetData: WidgetData
    ) {
        GlobalScope.launch {
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
    }
}
