package com.octagontechnologies.sky_weather.data.local.room.location

import androidx.room.Dao
import androidx.room.Query
import com.octagontechnologies.sky_weather.data.local.room.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class LocationDao : BaseDao<LocalLocation> {
    @Query("SELECT * FROM localLocation")
    abstract fun getLocalLocation(): Flow<LocalLocation?>
}
