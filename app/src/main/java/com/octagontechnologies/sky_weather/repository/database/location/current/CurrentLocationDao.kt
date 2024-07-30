package com.octagontechnologies.sky_weather.repository.database.location.current

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.octagontechnologies.sky_weather.repository.database.BaseDao

// Only the user's actual current location
@Dao
abstract class CurrentLocationDao: BaseDao<CurrentLocation> {

    @Query("SELECT * FROM localCurrentLocation")
    abstract fun getUserCurrentLocation(): LiveData<CurrentLocation?>

}