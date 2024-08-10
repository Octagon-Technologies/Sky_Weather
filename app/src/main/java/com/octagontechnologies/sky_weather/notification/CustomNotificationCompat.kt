package com.octagontechnologies.sky_weather.notification

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
import com.octagontechnologies.sky_weather.R
import com.octagontechnologies.sky_weather.domain.Location
import com.octagontechnologies.sky_weather.domain.SingleForecast
import com.octagontechnologies.sky_weather.domain.getFormattedTemp
import com.octagontechnologies.sky_weather.main_activity.MainActivity
import com.octagontechnologies.sky_weather.utils.Units
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



    fun clearNotification() { notificationManagerCompat.cancelAll() }

    @SuppressLint("NewApi")
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
        units: Units?
    ) {
        Timber.d("createNotification called: Part 1")

        if (singleForecast == null || location == null)
            return

        if (context.checkCallingOrSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
            return


        Timber.d("createNotification called: PArt 2")

        if (notificationManagerCompat.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null)
            createNotificationChannel()

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
            setSilent(true)
            setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
            setSmallIcon(R.drawable.sky_weather_icon)
            setShowWhen(false)
            setSound(null)

            setOngoing(true)
            setAutoCancel(false)

            setContentTitle(location.displayName ?: "-----")
            setContentText(singleForecast.getFormattedTemp(units) + (units ?: Units.getDefault()).getUnitSymbol())

            setCustomHeadsUpContentView(null)
            setContentIntent(clickPendingIntent)
        }.build()

        Timber.d("createNotification called with notification as $notification")
        notificationManagerCompat.cancelAll()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // This check is kinda unnecessary since we've adequately checked for permission in the ViewModel... but I'm going to add
            // it just in case... Plus, why not? :)
            if (ActivityCompat
                    .checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
            ) notificationManagerCompat.notify(NOTIFICATION_ID, notification)
        } else
            notificationManagerCompat.notify(NOTIFICATION_ID, notification)
    }


}