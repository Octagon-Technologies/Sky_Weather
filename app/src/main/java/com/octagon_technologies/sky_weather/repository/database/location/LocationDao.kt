package com.octagon_technologies.sky_weather.repository.database.location

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalLocation(localLocation: LocalLocation)

    @Query("SELECT * FROM localLocation")
    fun getLocalLocation(): LiveData<LocalLocation>
}