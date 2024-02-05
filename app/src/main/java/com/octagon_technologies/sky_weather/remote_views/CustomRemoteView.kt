package com.octagon_technologies.sky_weather.remote_views

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.os.Build
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.domain.SingleForecast
import com.octagon_technologies.sky_weather.domain.getFormattedFeelsLike
import com.octagon_technologies.sky_weather.domain.getFormattedTemp
import com.octagon_technologies.sky_weather.main_activity.MainActivity
import com.octagon_technologies.sky_weather.utils.*
import java.util.*

class CustomRemoteView(private val context: Context) {
    private val clickIntent = Intent(context, MainActivity::class.java)
    private val clickPendingIntent: PendingIntent =
        PendingIntent.getActivity(context, 1234, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT)

    @SuppressLint("NewApi")
    fun getCustomRemoteView(
        singleForecast: SingleForecast?,
        location: Location?,
        timeFormat: TimeFormat?,
        units: Units?
    ) =
        RemoteViews(context.packageName, R.layout.custom_remote_view).apply {
            val hourOfDay = singleForecast?.timeInMillis?.getHoursOfDay() ?: 12

            setTextViewText(
                R.id.expanded_notification_temp, singleForecast.getFormattedTemp()
            )
            setTextViewText(
                R.id.expanded_notification_feelslike, singleForecast.getFormattedFeelsLike()
            )
            setTextViewText(
                R.id.expanded_notification_unit_system,
                units?.value ?: "C"
            )
            setTextViewText(
                R.id.expanded_notification_weather_code,
                singleForecast?.weatherCode?.getWeatherTitle()
            )

            setTextViewText(
                R.id.expanded_notification_time_text,
                singleForecast?.timeInMillis?.getHoursAndMins(timeFormat)
            )

            setTextViewText(
                R.id.expanded_notification_place_name,
                location?.displayName
            )

            setImageViewResource(
                R.id.expanded_notification_weather_icon,
                singleForecast?.weatherCode?.getWeatherIcon(hourOfDay) ?: R.drawable.yellow_sun
            )

            val drawable =
                singleForecast?.weatherCode?.getWeatherIcon(hourOfDay) ?: R.drawable.yellow_sun
            val tint = ContextCompat.getColor(
                context,
                if (drawable == R.drawable.yellow_sun) R.color.dark_orange else R.color.dark_black
            )


            if ((Build.VERSION_CODES.M).checkBuildVersion()) {
                setImageViewIcon(
                    R.id.expanded_notification_weather_icon,
                    Icon.createWithResource(context, drawable)
                        .setTint(tint)
                )
            } else {
                setImageViewBitmap(
                    R.id.expanded_notification_weather_icon,
                    BitmapFactory.decodeResource(context.resources, drawable)
                        .also { it.eraseColor(tint) }
                )
            }

            setOnClickPendingIntent(R.id.base_layout, clickPendingIntent)
        }
}