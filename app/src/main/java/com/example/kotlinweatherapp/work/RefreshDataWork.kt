package com.example.kotlinweatherapp.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.kotlinweatherapp.database.WeatherDataBase
import com.example.kotlinweatherapp.repository.WeatherRepo
import retrofit2.HttpException

class RefreshDataWork(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshWorkData"
    }

    override suspend fun doWork(): Result {
            val dataBase = WeatherDataBase.getInstance(applicationContext)
            val videoRepo = WeatherRepo(dataBase!!)


            return try {
                Log.i("RefreshWork", "doWork: Work is done and successfully")
                videoRepo.refreshData()
                Result.success()
            } catch (httpException: HttpException) {
                Log.i("RefreshWork", "doWork: Work is done and unsuccessful")
                Result.retry()
        }
    }
}