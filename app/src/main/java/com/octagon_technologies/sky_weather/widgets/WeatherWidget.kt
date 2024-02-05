package com.octagon_technologies.sky_weather.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [WidgetConfigureActivity]
 */
@AndroidEntryPoint
class WeatherWidget : AppWidgetProvider() {
    @Inject
    lateinit var widgetSettings: WidgetSettings

    @Inject
    lateinit var widgetRepo: WidgetRepo

    @OptIn(DelicateCoroutinesApi::class)
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        GlobalScope.launch {
            widgetRepo.updateAllWidgets(appWidgetIds) {
                Timber.d("In WeatherWidget, updateAllWidgets.isSuccess is ${it.isSuccess}")
            }
        }

        Timber.d("onUpdate() called in widget with appWidgetIds is ${appWidgetIds.asList()}")
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        GlobalScope.launch {
            appWidgetIds.forEach {
                widgetSettings.removeWidgetId(it)
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

//    private fun getInitWidgetSettings(context: Context) =
//        if (this::widgetSettings.isInitialized) widgetSettings
//        else {
//            widgetSettings = WidgetSettings(context)
//            widgetSettings
//        }

}
