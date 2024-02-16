package com.octagon_technologies.sky_weather.repository.database.location.current

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.octagon_technologies.sky_weather.repository.database.BaseDao

// Only the user's actual current location
@Dao
abstract class CurrentLocationDao: BaseDao<LocalCurrentLocation> {

    @Query("SELECT * FROM localCurrentLocation")
    abstract fun getUserCurrentLocation(): LiveData<LocalCurrentLocation?>

}