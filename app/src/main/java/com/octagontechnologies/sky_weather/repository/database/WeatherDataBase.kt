package com.octagontechnologies.sky_weather.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.octagontechnologies.sky_weather.repository.database.location.LocalLocation
import com.octagontechnologies.sky_weather.repository.database.location.LocationDao
import com.octagontechnologies.sky_weather.repository.database.location.current.CurrentLocation
import com.octagontechnologies.sky_weather.repository.database.location.current.CurrentLocationDao
import com.octagontechnologies.sky_weather.repository.database.location.favorites.FavouriteLocationDao
import com.octagontechnologies.sky_weather.repository.database.location.favorites.LocalFavouriteLocation
import com.octagontechnologies.sky_weather.repository.database.location.recent.LocalRecentLocation
import com.octagontechnologies.sky_weather.repository.database.location.recent.RecentLocationDao
import com.octagontechnologies.sky_weather.repository.database.lunar.LocalLunar
import com.octagontechnologies.sky_weather.repository.database.lunar.LunarDao
import com.octagontechnologies.sky_weather.repository.database.weather.current.CurrentForecastDao
import com.octagontechnologies.sky_weather.repository.database.weather.current.LocalCurrentForecast
import com.octagontechnologies.sky_weather.repository.database.weather.daily.DailyWeatherDao
import com.octagontechnologies.sky_weather.repository.database.weather.daily.LocalDailyForecast
import com.octagontechnologies.sky_weather.repository.database.weather.hourly.HourlyWeatherDao
import com.octagontechnologies.sky_weather.repository.database.weather.hourly.LocalHourlyForecast

@Database(
    entities = [
        LocalCurrentForecast::class,
        LocalDailyForecast::class,
        LocalRecentLocation::class,
        LocalLocation::class,
        CurrentLocation::class,
        LocalFavouriteLocation::class,
        LocalLunar::class,
        LocalHourlyForecast::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(value = [(RoomTypeConverters::class)])
abstract class WeatherDataBase : RoomDatabase() {

    abstract val lunarDao: LunarDao

    abstract val currentForecastDao: CurrentForecastDao
    abstract val hourlyDao: HourlyWeatherDao
    abstract val dailyDao: DailyWeatherDao

    abstract val locationDao: LocationDao
    abstract val currentLocationDao: CurrentLocationDao
    abstract val favouriteLocationDao: FavouriteLocationDao
    abstract val recentLocationDao: RecentLocationDao

    companion object {
        @Volatile
        var INSTANCE: WeatherDataBase? = null

        fun getInstance(context: Context): WeatherDataBase {
            var instance = INSTANCE
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDataBase::class.java,
                    "weatherDatabase"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}