package com.octagon_technologies.sky_weather.repository.database.weather.current

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrentForecastDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalCurrentForecast(localCurrentForecast: LocalCurrentForecast)

    @Query("SELECT * FROM localCurrentForecast")
    fun getLocalCurrentForecast(): LiveData<LocalCurrentForecast>

}