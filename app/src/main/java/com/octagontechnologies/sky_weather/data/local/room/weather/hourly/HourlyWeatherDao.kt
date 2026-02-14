package com.octagontechnologies.sky_weather.data.local.room.weather.hourly

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.octagontechnologies.sky_weather.data.local.room.BaseDao

@Dao
abstract class HourlyWeatherDao : BaseDao<LocalHourlyForecast> {
    @Query("SELECT * FROM localHourlyForecast")
    abstract fun getLocalHourlyForecast(): LiveData<LocalHourlyForecast?>
}
