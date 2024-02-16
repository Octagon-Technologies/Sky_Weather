package com.octagon_technologies.sky_weather

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.ads.MobileAds
import com.octagon_technologies.sky_weather.work.RefreshDataWork
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

/*
TODO:
1) Add widgets
2) Fix notification
3) Save isGpsAllowed on DataStore to auto-update current location       -------- DONE
4) Add try catch in search location and display a No Internet toast/snackbar   ------ DONE
5) Look for the perfect grey blue to re-design Current Forecast, Hourly Forecast and ------- DONE
6) Change Humidity text to Precipitation ------ DONE
 */
@HiltAndroidApp
class App : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)


    override fun onCreate() {
        super.onCreate()
//        MobileAds.initialize(applicationContext)
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        applicationScope.launch {
            setRecurringWork()
        }
    }

    private fun setRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(false)
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWork>(2, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        try {
            WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
                RefreshDataWork.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingRequest
            )
        } catch (e: IllegalStateException) {
            Timber.e(e, "Work manager wasn't initialized")
        }

//        widgetRepo.updateAllWidgets {
//            Timber.d("In App, updateAllWidgets.isSuccess is ${it.isSuccess}")
//        }
    }
}