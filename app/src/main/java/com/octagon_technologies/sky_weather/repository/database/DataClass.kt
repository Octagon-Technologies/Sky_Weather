package com.octagon_technologies.sky_weather.repository.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagon_technologies.sky_weather.repository.network.allergy_forecast.Allergy
import com.octagon_technologies.sky_weather.repository.network.daily_forecast.EachDailyForecast
import com.octagon_technologies.sky_weather.repository.network.hourly_forecast.EachHourlyForecast
import com.octagon_technologies.sky_weather.repository.network.location.Location
import com.octagon_technologies.sky_weather.repository.network.lunar_forecast.LunarForecast
import com.octagon_technologies.sky_weather.repository.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.repository.network.single_forecast.SingleForecast

// TODO - ColumnInfo without the name doesn't add any value

@Entity(tableName = "hourlyForecastDatabaseClass")
data class HourlyForecastDatabaseClass(
        @PrimaryKey(autoGenerate = false)
        val hourlyForecastId: Int = 2,

        @ColumnInfo
        val hourlyForecast: ArrayList<EachHourlyForecast>

)

@Entity(tableName = "currentForecastDatabaseClass")
data class CurrentForecastDatabaseClass(
        @PrimaryKey(autoGenerate = false)
        val currentForecastId: Int = 1,

        val currentForecast: SingleForecast
)

@Entity(tableName = "allergyDatabaseClass")
data class AllergyDatabaseClass(
        @PrimaryKey(autoGenerate = false)
        val allergyKey: Int = 10,

        @ColumnInfo
        val allergyForecast: Allergy
)

@Entity(tableName = "lunarDatabaseClass")
data class LunarDatabaseClass(
        @PrimaryKey(autoGenerate = false)
        val lunarKey: Int = 20,

        @ColumnInfo
        val lunarForecast: LunarForecast
)


@Entity(tableName = "locationDatabaseClass")
data class LocationDatabaseClass(
        @PrimaryKey(autoGenerate = false)
        val locationKey: Int = 40,

        @ColumnInfo
        val reversedLocation: ReverseGeoCodingLocation
)

@Entity(tableName = "favouriteLocationDatabaseClass")
data class FavouriteLocationDatabaseClass(
        @PrimaryKey(autoGenerate = false)
        val favouriteLocationKey: String,

        @ColumnInfo
        val favouriteLocation: Location
)

@Entity(tableName = "recentLocationDatabaseClass")
data class RecentLocationDatabaseClass(
        @PrimaryKey(autoGenerate = false)
        val recentLocationKey: String,

        @ColumnInfo
        val recentLocation: Location
)

@Entity(tableName = "dailyForecastDatabaseClass")
data class DailyForecastDatabaseClass(
        @PrimaryKey(autoGenerate = false)
        val dailyForecastId: Int = 2,

        @ColumnInfo
        val dailyForecast: ArrayList<EachDailyForecast>
)
