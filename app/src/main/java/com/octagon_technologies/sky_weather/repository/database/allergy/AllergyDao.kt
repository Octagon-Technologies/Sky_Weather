package com.octagon_technologies.sky_weather.repository.database.allergy

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.octagon_technologies.sky_weather.repository.database.BaseDao

@Dao
abstract class AllergyDao: BaseDao<LocalAllergy> {
    @Query("SELECT * FROM localAllergy")
    abstract fun getLocalAllergy(): LiveData<LocalAllergy?>

}