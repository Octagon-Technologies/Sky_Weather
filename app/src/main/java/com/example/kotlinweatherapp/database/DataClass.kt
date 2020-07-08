package com.example.kotlinweatherapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kotlinweatherapp.network.currentweather.*
import com.example.kotlinweatherapp.network.currentweather.CurrentWeatherDataClass
import com.example.kotlinweatherapp.network.futureforecast.*
import com.example.kotlinweatherapp.network.futureforecast.Clouds
import com.example.kotlinweatherapp.network.futureforecast.Coord
import com.example.kotlinweatherapp.network.futureforecast.Main
import com.example.kotlinweatherapp.network.futureforecast.Sys
import com.example.kotlinweatherapp.network.futureforecast.Weather
import com.example.kotlinweatherapp.network.futureforecast.Wind
import com.squareup.moshi.Json

// TODO: Add type converter for entities

const val MAIN_VALUE = 1
const val CURRENT_WEATHER_VALUE = 10
const val FUTURE_WEATHER_VALUE = 5

@Entity(tableName = "dataClass")
data class DataClass(
        @PrimaryKey(autoGenerate = false)
        val name_id: Int = MAIN_VALUE,

        @ColumnInfo
        val name_text: String?,

        @ColumnInfo
        val useDeviceLocation: Boolean?
)

@Entity(tableName = "futureWeatherDataClass")
data class FutureDatabaseClass(
        @PrimaryKey(autoGenerate = false)
        val current_weather_id: Int = FUTURE_WEATHER_VALUE,

        val future_weather: FutureWeatherDataClass

////        val city: City,
////        val coord: Coord?,
//        val lat: Double,
//        val lon: Double,
//        val country: String?,
//        val id: Int?,
//        val name: String?,
//        val population: Int?,
//        val sunrise: Int?,
//        val sunset: Int?,
//        val timezone: Int?,
//
//        val cnt: Int,
//        val cod: String,
//
////        val list: List<All>,
////        val clouds: Clouds,
//        val all: List<Int>,
//
//        val dt: List<Long>,
//        val dt_txt: List<String>,
//
////        val main: Main,
//        val feels_like: List<Double>,
//        val grnd_level: List<Int>,
//        val humidity: List<Int>,
//        val pressure: List<Int>,
//        val sea_level: List<Int>,
//        val temp: List<Double>,
//        val temp_kf: List<Double>,
//        val temp_max: List<Double>,
//        val temp_min: List<Double>,
//
////        val rain: Rain?,
////        @Json(name = "3h")
//        val after3h : MutableList<Double>,
//
////        val sys: Sys,
//        val pod: MutableList<String>,
//
////        val weather: List<Weather>,
//        val description: MutableList<List<String>>,
//        val icon: MutableList<List<String>>,
//        val weather_id: MutableList<List<Int>>,
//        val weather_main: MutableList<List<String>>,
//
////        val wind: Wind,
//        val deg: MutableList<Int>,
//        val speed: MutableList<Double>,
//
//        val message: Int
)

@Entity(tableName = "currentWeatherDataClass")
data class CurrentDatabaseClass(
        @PrimaryKey(autoGenerate = false)
        val current_weather_id: Int = CURRENT_WEATHER_VALUE,

        val current_weather: CurrentWeatherDataClass
//
//        val base: String,
//
////        val clouds: Clouds,
//        val clouds_all: Int,
//        val cod: Int,
//
////        val coord: Coord,
//        val lat: Double,
//        val lon: Double,
//
//        val dt: Int,
//        val id: Int,
//
////        val main: Main,
//        val feels_like: Double,
//        val humidity: Int,
//        val pressure: Int,
//        val temp: Double,
//        val temp_max: Double,
//        val temp_min: Double,
//
//        val name: String,
//
////        val sys: Sys,
//        val country: String?,
//        val sys_id: Int?,
//        val sunrise: Int?,
//        val sunset: Int?,
//        val type: Int?,
//
//        val timezone: Int,
//        val visibility: Int,
//
////        val weather: List<Weather>,
//        val description: String,
//        val icon: String,
//        val weather_id: Int,
//        val main: String,
//
////        val wind: Wind
//        val deg: Int,
//        val speed: Double
)



/*
data class CurrentWeatherDataClass(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)
 */