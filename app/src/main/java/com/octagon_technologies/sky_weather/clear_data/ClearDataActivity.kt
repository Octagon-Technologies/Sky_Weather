package com.octagon_technologies.sky_weather.clear_data

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.Bundle
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.databinding.ActivityClearDataBinding
import timber.log.Timber

// TODO: See how we can bring this back to the app
class ClearDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityClearDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.clearDataBtn.setOnClickListener { clearData() }
    }

    private fun clearData() {
        try {
            // clearing app data
            val runtime = Runtime.getRuntime()
            runtime.exec("pm clear com.octagon_technologies.sky_weather")

            removeAllNotifications()
            removeAllWidgets()
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun removeAllWidgets() {
        val cn = ComponentName(
            application.packageName,
            "com.octagon_technologies.sky_weather.widgets.WeatherWidget"
        )
        val appWidgetManagerCompat = AppWidgetManager.getInstance(applicationContext)
        val appWidgetIds = appWidgetManagerCompat.getAppWidgetIds(cn)
        val outOfSyncRemoteView = RemoteViews(application.packageName, R.layout.out_of_sync_widget_layout)

        appWidgetManagerCompat.updateAppWidget(appWidgetIds, outOfSyncRemoteView)

        val appWidgetHost = AppWidgetHost(applicationContext, 0)
        appWidgetIds.forEach {
            appWidgetHost.deleteAppWidgetId(it)
        }
    }

    private fun removeAllNotifications() {
        val notificationManagerCompat = NotificationManagerCompat.from(applicationContext)
        notificationManagerCompat.cancelAll()
    }
}