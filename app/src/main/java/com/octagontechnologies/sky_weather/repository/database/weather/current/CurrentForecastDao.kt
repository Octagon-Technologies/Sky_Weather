package com.octagontechnologies.sky_weather.repository.database.weather.current

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.octagontechnologies.sky_weather.repository.database.BaseDao

@Dao
abstract class CurrentForecastDao: BaseDao<LocalCurrentForecast> {
    @Query("SELECT * FROM localCurrentForecast")
    abstract fun getLocalCurrentForecast(): LiveData<LocalCurrentForecast?>

}