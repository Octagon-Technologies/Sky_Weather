package com.octagontechnologies.sky_weather

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.octagontechnologies.sky_weather.work.RefreshDataWork
import com.octagontechnologies.sky_weather.work.UrgentDataWork
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/*
TODO:
1) Fix status bar colors -------- DONE
2) Loaded them in advance ----- DONE
3) Add Black theme  --- DONE

1) Add widgets
2) Fix notification ----- DONE
3) Save isGpsAllowed on DataStore to auto-update current location       -------- DONE
4) Add try catch in search location and display a No Internet toast/snackbar   ------ DONE
5) Look for the perfect grey blue to re-design Current Forecast, Hourly Forecast and ------- DONE
6) Change Humidity text to Precipitation ------ DONE
 */
@HiltAndroidApp
class App : Application(), Configuration.Provider {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
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

        val urgentRequest = PeriodicWorkRequestBuilder<UrgentDataWork>(20, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        val longerRequest = PeriodicWorkRequestBuilder<RefreshDataWork>(6, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            UrgentDataWork.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            urgentRequest
        )

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            RefreshDataWork.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            longerRequest
        )
    }
}