package com.octagontechnologies.sky_weather.repository.database.location

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.octagontechnologies.sky_weather.repository.database.BaseDao

@Dao
abstract class LocationDao : BaseDao<LocalLocation> {
    @Query("SELECT * FROM localLocation")
    abstract fun getLocalLocation(): LiveData<LocalLocation?>
}