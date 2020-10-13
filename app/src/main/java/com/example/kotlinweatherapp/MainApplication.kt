package com.example.kotlinweatherapp

import android.app.Application
import androidx.work.*
import com.example.kotlinweatherapp.work.RefreshDataWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MainApplication: Application() {
    private val  applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        delayInit()
    }

    private fun delayInit(){
        applicationScope.launch {
            setRecurringWork()
        }
    }

    private fun setRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(false)
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWork>(30, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWork.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )

    }
}