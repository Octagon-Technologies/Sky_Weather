package com.octagon_technologies.sky_weather.repository.database.lunar

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LunarDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalLunar(localLunar: LocalLunar)

    @Query("SELECT * FROM localLunar")
    fun getLocalLunar(): LiveData<LocalLunar>
}