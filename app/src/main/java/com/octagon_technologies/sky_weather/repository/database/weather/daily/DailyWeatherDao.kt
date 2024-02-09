package com.octagon_technologies.sky_weather.repository.database.weather.daily

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.octagon_technologies.sky_weather.repository.database.BaseDao

@Dao
abstract class DailyWeatherDao: BaseDao<LocalDailyForecast> {
    @Query("SELECT * FROM localDailyForecast")
    abstract fun getLocalDailyForecast(): LiveData<LocalDailyForecast?>
}
