package com.octagontechnologies.sky_weather.data.local.room.weather.current

import androidx.room.Dao
import androidx.room.Query
import com.octagontechnologies.sky_weather.data.local.room.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CurrentForecastDao : BaseDao<LocalCurrentForecast> {
    @Query("SELECT * FROM localCurrentForecast")
    abstract fun getLocalCurrentForecast(): Flow<LocalCurrentForecast?>
}
