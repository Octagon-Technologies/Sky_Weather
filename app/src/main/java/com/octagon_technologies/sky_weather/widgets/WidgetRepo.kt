package com.octagon_technologies.sky_weather.widgets

import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.domain.SingleForecast
import com.octagon_technologies.sky_weather.models.WidgetData
import com.octagon_technologies.sky_weather.remote_views.CustomRemoteView
import com.octagon_technologies.sky_weather.repository.repo.SettingsRepo
import com.octagon_technologies.sky_weather.utils.TimeFormat
import com.octagon_technologies.sky_weather.utils.Units
import timber.log.Timber
import javax.inject.Inject

class WidgetRepo @Inject constructor(
    private val application: Application,
    private val widgetSettings: WidgetSettings,
    private val settingsRepo: SettingsRepo
) {
    private val appWidgetManager: AppWidgetManager =
        AppWidgetManager.getInstance(application.applicationContext)

    suspend fun createWidget(
        widgetId: Int,
        transparencyOutOf255: Int,
        location: Location?,
        onComplete: (Result<SingleForecast?>) -> Unit
    ) {
        location ?: run {
            Timber.d("location.value is $location.value in addWidget()")
            onComplete(Result.failure(Throwable("Location has not been selected")))
            return
        }

        val widgetData = WidgetData(
            widgetId = widgetId,
            location = location,
            transparencyOutOf255 = transparencyOutOf255,
            timeFormat = settingsRepo.timeFormat.value ?: TimeFormat.FULL_DAY,
            units = settingsRepo.units.value ?: Units.METRIC
        )

        widgetSettings.addWidgetId(widgetData)
        updateSingleWidget(widgetData, onComplete)
    }

    suspend fun updateAllWidgets(
        paramAppWidgetIds: IntArray? = null,
        onComplete: (Result<SingleForecast?>) -> Unit
    ) {
        val cn = ComponentName(
            "com.octagon_technologies.sky_weather",
            "com.octagon_technologies.sky_weather.widgets.WeatherWidget"
        )
        val appWidgetIds = (paramAppWidgetIds ?: appWidgetManager.getAppWidgetIds(cn)).asList()
        Timber.d("appWidgetIds is $appWidgetIds")

        val widgetsToUpdate = widgetSettings.getAllWidgets()
            .filter { it.widgetId in appWidgetIds }
        Timber.d("widgetsToUpdate is $widgetsToUpdate")

        widgetsToUpdate.forEach {
            updateSingleWidget(it, onComplete)
        }
    }

    private suspend fun updateSingleWidget(
        widgetData: WidgetData,
        onComplete: (Result<SingleForecast?>) -> Unit
    ) {
        Timber.d("Updating widget in updateSingleWidget with widgetData as ${widgetData.widgetId}")

        val widgetResult = getWeatherForecastFromWidgetData(widgetData.location)
        onComplete(widgetResult)

        val widgetForecast = widgetResult.getOrNull()

        // Construct the RemoteViews object
        val widgetRemoteView = CustomRemoteView(application.applicationContext).getCustomRemoteView(
            widgetForecast,
            widgetData.location,
            widgetData.timeFormat,
            widgetData.units
        )

        widgetRemoteView.setInt(
            R.id.base_layout, "setBackgroundColor", widgetData.transparencyOutOf255
        )

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(widgetData.widgetId, widgetRemoteView)
    }

    private suspend fun getWeatherForecastFromWidgetData(location: Location?): Result<SingleForecast?> {
        location?.let {
            val widgetWeatherForecast =
                widgetSettings.getWeatherForecastFromWidgetData(location, settingsRepo.units.value)
                    ?: run {
                        return Result.failure(NullPointerException("Getting weather result failed"))
                    }

            Timber.d("widgetWeatherForecast is $widgetWeatherForecast in getWeatherForecastFromWidgetData()")
            return Result.success(widgetWeatherForecast)
        } ?: return Result.failure(NullPointerException("location is $location"))
    }

}