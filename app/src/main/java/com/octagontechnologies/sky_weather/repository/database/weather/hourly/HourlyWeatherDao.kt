package com.octagontechnologies.sky_weather.repository.database.weather.hourly

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.octagontechnologies.sky_weather.repository.database.BaseDao

@Dao
abstract class HourlyWeatherDao: BaseDao<LocalHourlyForecast> {
    @Query("SELECT * FROM localHourlyForecast")
    abstract fun getLocalHourlyForecast(): LiveData<LocalHourlyForecast?>
}


