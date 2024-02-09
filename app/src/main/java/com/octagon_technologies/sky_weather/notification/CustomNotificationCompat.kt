package com.octagon_technologies.sky_weather.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.BasePermissionListener
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.domain.SingleForecast
import com.octagon_technologies.sky_weather.remote_views.CustomRemoteView
import com.octagon_technologies.sky_weather.utils.TimeFormat
import com.octagon_technologies.sky_weather.utils.Units
import com.octagon_technologies.sky_weather.utils.checkBuildVersionFrom
import com.octagon_technologies.sky_weather.utils.isNull
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject


class CustomNotificationCompat @Inject constructor(
    @ApplicationContext private val context: Context
) {
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

    @SuppressLint("NewApi")
    fun createNotificationChannel() {
        if (checkBuildVersionFrom(Build.VERSION_CODES.O)) {
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
        location: Location?,
        timeFormat: TimeFormat?,
        units: Units?
    ) {
        if (singleForecast.isNull() || location.isNull() || timeFormat.isNull() || units.isNull()) {
            Timber.d("createNotification failed because singleForecast is ${singleForecast.isNull()}, reverseLocation is ${location.isNull()}, units is ${units.isNull()} and timeFormat is ${timeFormat.isNull()}")
            return
        }
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
            setSilent(true)
            priority = NotificationCompat.PRIORITY_HIGH
            setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
            setContent(customRemoteView.getCustomRemoteView(singleForecast, location, timeFormat, units))
            setSmallIcon(R.drawable.cloudy_night)
            setShowWhen(false)
            setSound(null)
            setCustomContentView(
                customRemoteView
                    .getCustomRemoteView(singleForecast, location, timeFormat, units)
            )
            setCustomBigContentView(
                customRemoteView
                    .getCustomRemoteView(singleForecast, location, timeFormat, units)
            )
            setCustomHeadsUpContentView(null)
            setOngoing(true)
        }.build()

        Timber.d("createNotification called with notification as $notification")
        notificationManagerCompat.cancelAll()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withContext(context).withPermission(Manifest.permission.POST_NOTIFICATIONS)
                .withListener(object : BasePermissionListener() {
                    @SuppressLint("MissingPermission")
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        super.onPermissionGranted(p0)
                        notificationManagerCompat.notify(NOTIFICATION_ID, notification)
                    }
                })
        }
    }



}