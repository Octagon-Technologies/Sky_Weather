package com.octagon_technologies.sky_weather.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.octagon_technologies.sky_weather.repository.WeatherRepo
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import retrofit2.HttpException
import timber.log.Timber

class RefreshDataWork(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface RefreshEntryPoint {
        fun weatherRepo(): WeatherRepo
    }

    private fun getWeatherRepo(): WeatherRepo {
        val hiltEntryPoint = EntryPoints.get(
            applicationContext,
            RefreshEntryPoint::class.java
        )
        return hiltEntryPoint.weatherRepo()
    }

    companion object {
        const val WORK_NAME = "RefreshWorkData"
    }

    override suspend fun doWork(): Result {
            val weatherRepo = getWeatherRepo()

            return try {
                weatherRepo.refreshData()
                Timber.d( "doWork: Work is done and successfully")
                Result.success()
            } catch (httpException: HttpException) {
                Timber.d( "doWork: Work failed")
                Result.retry()
        }
    }
}