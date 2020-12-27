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
import com.octagon_technologies.sky_weather.SplashActivity
import com.octagon_technologies.sky_weather.repository.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.repository.network.single_forecast.SingleForecast
import com.octagon_technologies.sky_weather.utils.*
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

class CustomRemoteView(private val context: Context) {
    private val clickIntent = Intent(context, SplashActivity::class.java)
    private val clickPendingIntent: PendingIntent = PendingIntent.getActivity(context, 1234, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT)

    @SuppressLint("NewApi")
    fun getCustomRemoteView(
        singleForecast: SingleForecast?,
        reverseLocation: ReverseGeoCodingLocation?,
        timeFormat: TimeFormat?
    ) =
        RemoteViews(context.packageName, R.layout.custom_remote_view).apply {
            val date = DateTime(singleForecast?.observationTime?.value).toDate()
            val hourIn24HourSystem = SimpleDateFormat(
                "HH",
                Locale.getDefault()
            ).format(date.time)?.toInt() ?: 0

            setTextViewText(
                R.id.expanded_notification_temp,
                "${singleForecast?.temp?.value?.toInt()}°"
            )
            setTextViewText(
                R.id.expanded_notification_feelslike,
                "FeelsLike ${singleForecast?.feelsLike?.value?.toInt()}°"
            )
            setTextViewText(
                R.id.expanded_notification_unit_system,
                singleForecast?.temp?.units ?: "C"
            )
            setTextViewText(
                R.id.expanded_notification_weather_code,
                singleForecast?.weatherCode?.value?.capitalizeWordsWithUnderscore()
            )

            setTextViewText(
                R.id.expanded_notification_time_text,
                timeFormat.getAmOrPmBasedOnTime(hourIn24HourSystem, date)
            )

            setTextViewText(
                R.id.expanded_notification_place_name,
                reverseLocation.getDisplayLocation()
            )

            setImageViewResource(
                R.id.expanded_notification_weather_icon,
                singleForecast?.weatherCode.getDrawableFromWeatherCode(hourIn24HourSystem)
            )

            val drawable =
                singleForecast?.weatherCode.getDrawableFromWeatherCode(hourIn24HourSystem)
            val tint = ContextCompat.getColor(
                context,
                if (drawable == R.drawable.sun) R.color.dark_orange else R.color.dark_black
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