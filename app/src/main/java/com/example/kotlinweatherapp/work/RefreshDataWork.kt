package com.example.kotlinweatherapp.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.kotlinweatherapp.repository.WeatherRepo
import retrofit2.HttpException
import timber.log.Timber

class RefreshDataWork(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshWorkData"
    }

    override suspend fun doWork(): Result {
            val repo = WeatherRepo(applicationContext)

            return try {
                repo.refreshData()
                Timber.d( "doWork: Work is done and successfully")
                Result.success()
            } catch (httpException: HttpException) {
                Timber.d( "doWork: Work failed")
                Result.retry()
        }
    }
}