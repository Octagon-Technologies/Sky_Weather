package com.octagontechnologies.sky_weather.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.octagontechnologies.sky_weather.repository.WeatherRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import timber.log.Timber

@HiltWorker
class RefreshDataWork @AssistedInject constructor(
    private val weatherRepo: WeatherRepo,
    @Assisted appContext : Context,
    @Assisted params : WorkerParameters
) : CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshWorkData"
    }

    override suspend fun doWork(): Result {
        return try {
            weatherRepo.refreshAllData()
            Timber.d("doWork: Work is done and successfully")
            Result.success()
        } catch (httpException: HttpException) {
            Timber.d("doWork: Work failed")
            Result.retry()
        } catch (e: Exception) {
            Timber.e(e)
            Result.failure()
        }
    }
}