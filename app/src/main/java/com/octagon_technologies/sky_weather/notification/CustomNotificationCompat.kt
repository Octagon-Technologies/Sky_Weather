package com.octagon_technologies.sky_weather.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.BitmapCompat
import com.octagon_technologies.sky_weather.*
import com.octagon_technologies.sky_weather.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.network.single_forecast.SingleForecast
import com.octagon_technologies.sky_weather.ui.shared_code.getDrawableFromWeatherCode
import org.joda.time.DateTime
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


class CustomNotificationCompat(private val context: Context) {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "1234"
        const val NOTIFICATION_CHANNEL_NAME = "Weather Forecast Notifications"

        const val PENDING_INTENT_REQUEST_CODE = 0
        const val NOTIFICATION_ID = 1234
    }

    private val intent by lazy { Intent(context, SplashActivity::class.java) }
    private val pendingIntent: PendingIntent by lazy {
        PendingIntent.getActivity(
            context,
            PENDING_INTENT_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private val notificationManagerCompat by lazy { NotificationManagerCompat.from(context) }

    private val customRemoteView = RemoteViews(context.packageName, R.layout.custom_remote_view)

    @SuppressLint("NewApi")
    private fun getCustomExpandedRemoteView(
        singleForecast: SingleForecast?,
        reverseLocation: ReverseGeoCodingLocation?,
        timeFormat: TimeFormat?
    ) =
        RemoteViews(context.packageName, R.layout.custom_expanded_remote_view).apply {
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
                    BitmapFactory.decodeResource(context.resources, drawable).also { it.eraseColor(tint) }
                )
            }
        }

    init {
        createNotificationChannel()
    }

    @SuppressLint("NewApi")
    fun createNotificationChannel() {
        if ((Build.VERSION_CODES.O).checkBuildVersion()) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Allows weather updates in notifications."
            }

            notificationManagerCompat.createNotificationChannel(notificationChannel)
        }
    }

    fun createNotification(
        singleForecast: SingleForecast?,
        reverseLocation: ReverseGeoCodingLocation?,
        timeFormat: TimeFormat?
    ) {
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
            setNotificationSilent()
            priority = NotificationCompat.PRIORITY_MAX
            setContent(getCustomExpandedRemoteView(singleForecast, reverseLocation, timeFormat))
            setSmallIcon(R.drawable.cloudy_night)
            setShowWhen(false)
            setSound(null)
            setContentIntent(pendingIntent)
            setCustomContentView(
                getCustomExpandedRemoteView(
                    singleForecast,
                    reverseLocation,
                    timeFormat
                )
            )
            setCustomBigContentView(
                getCustomExpandedRemoteView(
                    singleForecast,
                    reverseLocation,
                    timeFormat
                )
            )
            setCustomHeadsUpContentView(null)
            setOngoing(true)
        }.build()

        Timber.d("createNotification called with notification as $notification")
        notificationManagerCompat.cancelAll()
        notificationManagerCompat.notify(NOTIFICATION_ID, notification)
    }
}