package com.octagon_technologies.sky_weather.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.domain.SingleForecast
import com.octagon_technologies.sky_weather.domain.getFormattedTemp
import com.octagon_technologies.sky_weather.main_activity.MainActivity
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
    private val clickIntent = Intent(context, MainActivity::class.java)
    private val clickPendingIntent: PendingIntent =
        PendingIntent.getActivity(context, 1234, clickIntent, PendingIntent.FLAG_IMMUTABLE)

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "1234"
        const val NOTIFICATION_CHANNEL_NAME = "Weather Forecast Notifications"

        const val NOTIFICATION_ID = 1234
    }

    private val notificationManagerCompat by lazy { NotificationManagerCompat.from(context) }
//    private val customRemoteView by lazy { CustomRemoteView(context) }


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
                vibrationPattern = null

                setShowBadge(false)
                setSound(null, null)
                importance = NotificationManager.IMPORTANCE_DEFAULT
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
        Timber.d("createNotification called")

        if (singleForecast.isNull() || location.isNull() || timeFormat.isNull() || units.isNull()) {
            Timber.d("createNotification failed because singleForecast is ${singleForecast.isNull()}, location is ${location.isNull()}, units is ${units.isNull()} and timeFormat is ${timeFormat.isNull()}")
            return
        }
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
            setSilent(true)
            setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
//            setContent(
//                customRemoteView.getCustomRemoteView(
//                    singleForecast,
//                    location,
//                    timeFormat,
//                    units
//                )
//            )
            setSmallIcon(R.drawable.cloudy_night)
            setShowWhen(false)
            setSound(null)

            setOngoing(true)
            setAutoCancel(false)

            setContentTitle(location?.displayName ?: "-----")
            setContentText(singleForecast?.getFormattedTemp() + units?.getUnitSymbol())
//            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
//                setStyle(NotificationCompat.DecoratedCustomViewStyle())
//                setCustomContentView(
//                    customRemoteView
//                        .getCustomRemoteView(singleForecast, location, timeFormat, units)
//                )
//                setCustomBigContentView(
//                    customRemoteView
//                        .getCustomRemoteView(singleForecast, location, timeFormat, units)
//                )
//            }
//            else {
//                setContentTitle(location?.displayName ?: "-----")
//                setContentText(singleForecast?.getFormattedTemp() + units?.getUnitSymbol())

//                setCustomBigContentView(
//                    customRemoteView
//                        .getCustomRemoteView(singleForecast, location, timeFormat, units))
//            }
            setCustomHeadsUpContentView(null)
            setContentIntent(clickPendingIntent)
        }.build()

        Timber.d("createNotification called with notification as $notification")
        notificationManagerCompat.cancelAll()


        // This check is kinda unnecessary since we've adequately checked for permission in the ViewModel... but I'm going to add
        // it just in case... Plus, why not? :)
        if (ActivityCompat
                .checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
        ) notificationManagerCompat.notify(NOTIFICATION_ID, notification)
    }


}