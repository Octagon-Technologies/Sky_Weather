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

@Dao
interface CurrentWeatherDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentDataClass(currentDatabaseClass: CurrentDatabaseClass)

    @Query("SELECT * FROM currentWeatherDataClass WHERE current_weather_id = :current_id")
    fun getCurrentDataClass(current_id: Int):CurrentDatabaseClass

}

@Dao
interface FutureWeatherDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFutureAllClass(allClass: FutureDatabaseClass)

    @Query("SELECT * FROM futureWeatherDataClass WHERE current_weather_id = :future_id")
    fun getFutureDataClass(future_id: Int): FutureDatabaseClass
}