package com.octagon_technologies.sky_weather.repository.database.weather.hourly

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HourlyWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalHourlyForecast(arrayOfEachHourlyForecast: LocalHourlyForecast)

    @Query("SELECT * FROM localHourlyForecast")
    fun getLocalHourlyForecast(): LiveData<LocalHourlyForecast>
}


