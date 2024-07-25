package com.octagontechnologies.sky_weather.repository.database.allergy

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.octagontechnologies.sky_weather.repository.database.BaseDao

@Dao
abstract class AllergyDao: BaseDao<LocalAllergy> {
    @Query("SELECT * FROM localAllergy")
    abstract fun getLocalAllergy(): LiveData<LocalAllergy?>

}