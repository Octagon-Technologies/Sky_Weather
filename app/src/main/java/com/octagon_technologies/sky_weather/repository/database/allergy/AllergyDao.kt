package com.octagon_technologies.sky_weather.repository.database.allergy

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AllergyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalAllergy(localAllergy: LocalAllergy)

    @Query("SELECT * FROM localAllergy")
    fun getLocalAllergy(): LiveData<LocalAllergy>
}