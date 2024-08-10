package com.octagontechnologies.sky_weather.repository.database.lunar

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.octagontechnologies.sky_weather.repository.database.BaseDao

@Dao
abstract class LunarDao: BaseDao<LocalLunar> {
    @Query("SELECT * FROM localLunar")
    abstract fun getLocalLunar(): LiveData<LocalLunar?>
}