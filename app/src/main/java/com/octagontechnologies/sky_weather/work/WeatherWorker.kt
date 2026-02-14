package com.octagontechnologies.sky_weather.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.octagontechnologies.sky_weather.domain.repository.WeatherRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class WeatherWorker
    @AssistedInject
    constructor(
        @Assisted appContext: Context,
        @Assisted params: WorkerParameters,
        private val weatherRepo: WeatherRepo,
    ) : CoroutineWorker(appContext, params) {
        companion object {
            const val WORK_NAME = "WeatherWorker"
        }

        override suspend fun doWork(): Result {
            try {
                val currentForecastResult = weatherRepo.refreshCurrentForecast()
                val hourlyForecastResult = weatherRepo.refreshHourlyForecast()
                val dailyForecastResult = weatherRepo.refreshDailyForecast()
                val lunarForecastResult = weatherRepo.refreshLunarForecast()

                val allResults =
                    listOf(
                        currentForecastResult,
                        hourlyForecastResult,
                        dailyForecastResult,
                        lunarForecastResult,
                    )

                allResults.forEach { result ->
                    val failure = result.exceptionOrNull()
                    if (failure != null) {
                        Timber.e("WeatherWorker: A repository refresh failed.")
                        Timber.e(failure)
                        return Result.failure()
                    }
                }

                return Result.success()
            } catch (e: Exception) {
                Timber.e(e)

                return when (e) {
                    is java.net.UnknownHostException -> {
                        Result.retry()
                    }

                    is java.net.SocketTimeoutException -> {
                        Result.retry()
                    }

                    is NullPointerException -> {
                        Result.failure()
                    }

                    else -> Result.failure()
                }
            }
        }
    }
