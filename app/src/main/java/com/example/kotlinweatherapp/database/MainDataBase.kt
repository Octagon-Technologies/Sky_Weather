package com.example.kotlinweatherapp.database

import android.content.Context
import androidx.room.*

@Database(entities = [CurrentForecastDatabaseClass::class, LocationDatabaseClass::class, FavouriteLocationDatabaseClass::class, SelectedSingleForecastDatabaseClass::class, LunarDatabaseClass::class, AllergyDatabaseClass::class, HourlyForecastDatabaseClass::class], version = 2, exportSchema = false)
@TypeConverters(value = [(GithubTypeConverters::class)])
abstract class MainDataBase: RoomDatabase(){

    abstract val lunarForecastDao: LunarForecastDao
    abstract val allergyForecastDao: AllergyForecastDao
    abstract val currentDao: CurrentWeatherDao
    abstract val hourlyDao: HourlyWeatherDao
    abstract val selectedSingleForecastDao: SelectedSingleForecastDao
    abstract val locationDao: LocationDao
    abstract val favouriteLocationDao: FavouriteLocationDao

    companion object{
        @Volatile
        var INSTANCE: MainDataBase? = null

        fun getInstance(context: Context): MainDataBase? {
            var instance = INSTANCE
            if (instance == null){
                instance = Room.databaseBuilder(context.applicationContext, MainDataBase::class.java, "namesDatabase")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}