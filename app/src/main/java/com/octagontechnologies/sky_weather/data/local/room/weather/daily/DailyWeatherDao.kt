package com.octagontechnologies.sky_weather.data.local.room.weather.daily

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.octagontechnologies.sky_weather.data.local.room.BaseDao

@Dao
abstract class DailyWeatherDao : BaseDao<LocalDailyForecast> {
    @Query("SELECT * FROM localDailyForecast")
    abstract fun getLocalDailyForecast(): LiveData<LocalDailyForecast?>
}
