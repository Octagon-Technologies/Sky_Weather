package com.example.kotlinweatherapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertString(data: DataClass)

    @Query("SELECT * FROM dataClass ORDER BY name_id DESC LIMIT 1")
    fun getFormerString(): DataClass?
}