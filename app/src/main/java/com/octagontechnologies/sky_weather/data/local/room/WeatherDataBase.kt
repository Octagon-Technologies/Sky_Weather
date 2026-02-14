package com.octagontechnologies.sky_weather.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.octagontechnologies.sky_weather.data.local.room.location.LocalLocation
import com.octagontechnologies.sky_weather.data.local.room.location.LocationDao
import com.octagontechnologies.sky_weather.data.local.room.location.current.CurrentLocation
import com.octagontechnologies.sky_weather.data.local.room.location.current.CurrentLocationDao
import com.octagontechnologies.sky_weather.data.local.room.location.favorites.FavouriteLocationDao
import com.octagontechnologies.sky_weather.data.local.room.location.favorites.LocalFavouriteLocation
import com.octagontechnologies.sky_weather.data.local.room.location.recent.LocalRecentLocation
import com.octagontechnologies.sky_weather.data.local.room.location.recent.RecentLocationDao
import com.octagontechnologies.sky_weather.data.local.room.lunar.LocalLunar
import com.octagontechnologies.sky_weather.data.local.room.lunar.LunarDao
import com.octagontechnologies.sky_weather.data.local.room.weather.current.CurrentForecastDao
import com.octagontechnologies.sky_weather.data.local.room.weather.current.LocalCurrentForecast
import com.octagontechnologies.sky_weather.data.local.room.weather.daily.DailyWeatherDao
import com.octagontechnologies.sky_weather.data.local.room.weather.daily.LocalDailyForecast
import com.octagontechnologies.sky_weather.data.local.room.weather.hourly.HourlyWeatherDao
import com.octagontechnologies.sky_weather.data.local.room.weather.hourly.LocalHourlyForecast

@Database(
    entities = [
        LocalCurrentForecast::class,
        LocalDailyForecast::class,
        LocalRecentLocation::class,
        LocalLocation::class,
        CurrentLocation::class,
        LocalFavouriteLocation::class,
        LocalLunar::class,
        LocalHourlyForecast::class,
    ],
    version = 5,
    exportSchema = false,
)
@TypeConverters(RoomTypeConverters::class)
abstract class WeatherDataBase : RoomDatabase() {
    abstract val lunarDao: LunarDao

    abstract val currentForecastDao: CurrentForecastDao
    abstract val hourlyDao: HourlyWeatherDao
    abstract val dailyDao: DailyWeatherDao

    abstract val locationDao: LocationDao
    abstract val currentLocationDao: CurrentLocationDao
    abstract val favouriteLocationDao: FavouriteLocationDao
    abstract val recentLocationDao: RecentLocationDao
}
