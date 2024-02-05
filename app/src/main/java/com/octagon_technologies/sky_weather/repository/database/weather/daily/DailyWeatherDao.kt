package com.octagon_technologies.sky_weather.repository.database.weather.daily

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DailyWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalDailyForecast(localDailyForecast: LocalDailyForecast)

    @Query("SELECT * FROM localDailyForecast")
    fun getLocalDailyForecast(): LiveData<LocalDailyForecast>
}
