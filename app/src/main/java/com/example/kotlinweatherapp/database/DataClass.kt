package com.example.kotlinweatherapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kotlinweatherapp.network.allergy_forecast.Allergy
import com.example.kotlinweatherapp.network.current_forecast.SingleForecast
import com.example.kotlinweatherapp.network.hourly_forecast.EachHourlyForecast
import com.example.kotlinweatherapp.network.location.LocationItem
import com.example.kotlinweatherapp.network.lunar_forecast.LunarForecast
import com.example.kotlinweatherapp.network.reverse_geocoding_location.ReverseGeoCodingLocation

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

        @ColumnInfo
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

@Entity(tableName = "selectedSingleForecastDatabaseClass")
data class SelectedSingleForecastDatabaseClass(
        @PrimaryKey(autoGenerate = false)
        val selectedSingleForecastKey: Int = 30,

        @ColumnInfo
        val selectedSingleForecastForecast: SingleForecast
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
        val favouriteLocation: LocationItem
)