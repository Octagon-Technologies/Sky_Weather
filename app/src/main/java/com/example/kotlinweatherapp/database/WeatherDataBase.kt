package com.example.kotlinweatherapp.database

import android.content.Context
import androidx.room.*
import com.example.kotlinweatherapp.network.GithubTypeConverters

@Database(entities = [DataClass::class, CurrentDatabaseClass::class, FutureDatabaseClass::class], version = 7, exportSchema = false)
@TypeConverters(value = [(GithubTypeConverters::class)])
abstract class WeatherDataBase: RoomDatabase(){

    abstract val databaseDao: DatabaseDao
    abstract val currentDao: CurrentWeatherDao
    abstract val futureDao: FutureWeatherDao

    companion object{
        @Volatile
        var INSTANCE: WeatherDataBase? = null

        fun getInstance(context: Context): WeatherDataBase? {
            var instance = INSTANCE
            if (instance == null){
                instance = Room.databaseBuilder(context.applicationContext, WeatherDataBase::class.java, "namesDatabase")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}