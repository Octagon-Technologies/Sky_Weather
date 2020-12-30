package com.octagon_technologies.sky_weather.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.remote_views.CustomRemoteView
import com.octagon_technologies.sky_weather.repository.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.repository.network.single_forecast.SingleForecast
import com.octagon_technologies.sky_weather.utils.TimeFormat
import com.octagon_technologies.sky_weather.utils.checkBuildVersion
import com.octagon_technologies.sky_weather.utils.isNull
import timber.log.Timber


class CustomNotificationCompat(private val context: Context) {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "1234"
        const val NOTIFICATION_CHANNEL_NAME = "Weather Forecast Notifications"

        const val NOTIFICATION_ID = 1234
    }

    private val notificationManagerCompat by lazy { NotificationManagerCompat.from(context) }
    private val customRemoteView by lazy { CustomRemoteView(context) }


    init {
        createNotificationChannel()
    }

    // TODO - Replace extension function with a simple if check - If used just once
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
        if (singleForecast.isNull() || reverseLocation.isNull() || timeFormat.isNull()) {
            Timber.d("createNotification failed because singleForecast is ${singleForecast.isNull()}, reverseLocation is ${reverseLocation.isNull()} and timeFormat is ${timeFormat.isNull()}")
            return
        }
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
            setNotificationSilent()
            priority = NotificationCompat.PRIORITY_HIGH
            setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
            setContent(customRemoteView.getCustomRemoteView(singleForecast, reverseLocation, timeFormat))
            setSmallIcon(R.drawable.cloudy_night)
            setShowWhen(false)
            setSound(null)
            setCustomContentView(
                customRemoteView.getCustomRemoteView(
                    singleForecast,
                    reverseLocation,
                    timeFormat
                )
            )
            setCustomBigContentView(
                customRemoteView.getCustomRemoteView(
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