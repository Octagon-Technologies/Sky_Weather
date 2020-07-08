package com.example.kotlinweatherapp.database

import androidx.lifecycle.LiveData
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

@Dao
interface CurrentWeatherDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentDataClass(currentDatabaseClass: CurrentDatabaseClass)

    @Query("SELECT * FROM currentWeatherDataClass")
    fun getCurrentDataClass(): LiveData<CurrentDatabaseClass>

}

@Dao
interface FutureWeatherDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFutureAllClass(allClass: FutureDatabaseClass)

    @Query("SELECT * FROM futureWeatherDataClass")
    fun getFutureDataClass(): LiveData<FutureDatabaseClass>
}