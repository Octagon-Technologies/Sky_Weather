package com.octagontechnologies.sky_weather.remote_views

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.widget.RemoteViews
import com.octagontechnologies.sky_weather.R
import com.octagontechnologies.sky_weather.domain.Location
import com.octagontechnologies.sky_weather.domain.SingleForecast
import com.octagontechnologies.sky_weather.domain.getWeatherIcon
import com.octagontechnologies.sky_weather.domain.getWeatherTitle
import com.octagontechnologies.sky_weather.main_activity.MainActivity
import com.octagontechnologies.sky_weather.utils.TimeFormat
import com.octagontechnologies.sky_weather.utils.Units
import com.octagontechnologies.sky_weather.utils.getHoursAndMins
import com.octagontechnologies.sky_weather.utils.getHoursOfDay

class CustomRemoteView(private val context: Context) {
    private val clickIntent = Intent(context, MainActivity::class.java)
    private val clickPendingIntent: PendingIntent =
        PendingIntent.getActivity(context, 1234, clickIntent, PendingIntent.FLAG_IMMUTABLE)

    @SuppressLint("NewApi")
    fun getCustomRemoteView(
        singleForecast: SingleForecast?,
        location: Location?,
        timeFormat: TimeFormat?,
        units: Units?
    ) =
        RemoteViews(context.packageName, R.layout.custom_remote_view).apply {
            val isDay = (singleForecast?.timeInEpochMillis?.getHoursOfDay() ?: 12) in 6..19

//            setTextViewText(
//                R.id.expanded_notification_temp, singleForecast.getFormattedTemp()
//            )
//            setTextViewText(
//                R.id.expanded_notification_feelslike, singleForecast.getFormattedFeelsLike()
//            )
            setTextViewText(
                R.id.expanded_notification_unit_system,
                if (units == Units.IMPERIAL) "F" else "C"
            )
            setTextViewText(
                R.id.expanded_notification_weather_code,
                singleForecast?.weatherCode.getWeatherTitle()
            )

            setTextViewText(
                R.id.expanded_notification_time_text,
                singleForecast?.timeInEpochMillis?.getHoursAndMins(timeFormat)
            )

            setTextViewText(
                R.id.expanded_notification_place_name,
                location?.displayName
            )

            setImageViewResource(
                R.id.expanded_notification_weather_icon,
                singleForecast?.weatherCode.getWeatherIcon()
            )

            val drawable =
                singleForecast?.weatherCode.getWeatherIcon()
            setImageViewIcon(R.id.expanded_notification_weather_icon, Icon.createWithResource(context, drawable))
//            val tint = ContextCompat.getColor(
//                context,
//                if (drawable == R.drawable.yellow_sun) R.color.dark_orange else R.color.dark_black
//            )
//
//
//            if (checkBuildVersionFrom(Build.VERSION_CODES.M)) {
//                setImageViewIcon(
//                    R.id.expanded_notification_weather_icon,
//                    Icon.createWithResource(context, drawable)
//                        .setTint(tint)
//                )
//            } else {
//                setImageViewBitmap(
//                    R.id.expanded_notification_weather_icon,
//                    BitmapFactory.decodeResource(context.resources, drawable)
//                        .also { it.eraseColor(tint) }
//                )
//            }

            setOnClickPendingIntent(R.id.base_layout, clickPendingIntent)
        }
}