package com.octagon_technologies.sky_weather.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        CurrentForecastDatabaseClass::class,
        DailyForecastDatabaseClass::class,
        RecentLocationDatabaseClass::class,
        LocationDatabaseClass::class,
        FavouriteLocationDatabaseClass::class,
        LunarDatabaseClass::class,
        AllergyDatabaseClass::class,
        HourlyForecastDatabaseClass::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(value = [(GithubTypeConverters::class)])
abstract class MainDataBase : RoomDatabase() {

    abstract val lunarForecastDao: LunarForecastDao
    abstract val allergyForecastDao: AllergyForecastDao
    abstract val currentDao: CurrentWeatherDao
    abstract val hourlyDao: HourlyWeatherDao
    abstract val locationDao: LocationDao
    abstract val favouriteLocationDao: FavouriteLocationDao
    abstract val recentLocationDao: RecentLocationDao
    abstract val dailyDao: DailyWeatherDao

    companion object {
        @Volatile
        var INSTANCE: MainDataBase? = null

        fun getInstance(context: Context): MainDataBase? {
            var instance = INSTANCE
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDataBase::class.java,
                    "namesDatabase"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}