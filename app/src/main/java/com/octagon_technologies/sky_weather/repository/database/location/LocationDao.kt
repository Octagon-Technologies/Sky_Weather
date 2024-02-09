package com.octagon_technologies.sky_weather.repository.database.location

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.octagon_technologies.sky_weather.repository.database.BaseDao

@Dao
abstract class LocationDao : BaseDao<LocalLocation> {
    @Query("SELECT * FROM localLocation")
    abstract fun getLocalLocation(): LiveData<LocalLocation?>
}