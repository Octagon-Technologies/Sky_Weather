package com.example.kotlinweatherapp.network

import androidx.room.TypeConverter
import com.example.kotlinweatherapp.network.currentweather.CurrentWeatherDataClass
import com.example.kotlinweatherapp.network.futureforecast.All
import com.example.kotlinweatherapp.network.futureforecast.FutureWeatherDataClass
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

/*
//fun CurrentWeatherDataClass.asCurrentDataBaseClass(it: CurrentWeatherDataClass): CurrentDatabaseClass{
//    val coord = it.coord
//    val main = it.main
//    val wind = it.wind
//    val sys = it.sys
//    val weather= it.weather[0]
//
//    return CurrentDatabaseClass(
//        current_weather_id = CURRENT_WEATHER_VALUE,
//        base = it.base,
//        clouds_all = it.clouds.all,
//        cod = it.cod,
//        lat = coord.lat,
//        lon = coord.lon,
//        dt = it.dt,
//        id = it.id,
//        feels_like = it.main.feels_like,
//        humidity = main.humidity,
//        pressure = main.pressure,
//        temp = main.temp,
//        temp_max = main.temp_max,
//        temp_min = main.temp_min,
//        name = it.name,
//        country = sys.country,
//        sys_id = sys.id,
//        sunrise = sys.sunrise,
//        sunset = sys.sunset,
//        type = sys.type,
//        timezone = it.timezone,
//        visibility = it.visibility,
//        description = weather.description,
//        icon = weather.icon,
//        weather_id = weather.id,
//        main = weather.main,
//        deg = wind.deg,
//        speed = wind.speed
//    )
//}
//
//
//
//fun FutureWeatherDataClass.asFutureDatabaseClass(it: FutureWeatherDataClass): FutureDatabaseClass{
//    val city = it.city
//    val coord = city.coord
//    val allList = it.list
//
//        val clouds_all = mutableListOf<Int>()
//
//        val dt = mutableListOf<Long>()
//        val dt_txt = mutableListOf<String>()
//
////        val main: Main,
//        val feels_like = mutableListOf<Double>()
//        val grnd_level = mutableListOf<Int>()
//        val humidity = mutableListOf<Int>()
//        val pressure = mutableListOf<Int>()
//        val sea_level = mutableListOf<Int>()
//        val temp = mutableListOf<Double>()
//        val temp_kf = mutableListOf<Double>()
//        val temp_max = mutableListOf<Double>()
//        val temp_min = mutableListOf<Double>()
//
////        val rain: Rain?,
////        @Json(name = "3h")
//        val after3h = mutableListOf<Double>()
//
////        val sys: Sys,
//        val pod= mutableListOf<String>()
//
////        val weather: List<Weather>,
//        val description= mutableListOf<List<String>>()
//        val icon= mutableListOf<List<String>>()
//        val weather_id= mutableListOf<List<Int>>()
//        val weather_main = mutableListOf<List<String>>()
//
////        val wind: Wind,
//        val deg = mutableListOf<Int>()
//        val speed = mutableListOf<Double>()
//
//    for (all in allList){
//        val main = all.main
//        val rain = all.rain
//        val weather = all.weather[0]
//        val wind = all.wind
//
//        clouds_all.add(all.clouds.all)
//        dt.add(all.dt)
//        dt_txt.add(all.dt_txt)
//        feels_like.add(all.main.feels_like)
//        grnd_level.add(main.grnd_level)
//        humidity.add(main.humidity)
//        pressure.add(main.pressure)
//        sea_level.add(main.sea_level)
//        temp.add(main.temp)
//        temp_kf.add(main.temp_kf)
//        temp_max.add(main.temp_max)
//        temp_min.add(main.temp_min)
//
//        if (rain != null) {
//            after3h.add(rain.after3h)
//        }
//
//        pod.add(all.sys.pod)
//
//        description.add(listOf(weather.description))
//        icon.add(listOf(weather.icon))
//        weather_id.add(listOf(weather.id))
//        weather_main.add(listOf(weather.main))
//
//        deg.add(wind.deg)
//        speed.add(wind.speed)
//    }
//
//    return FutureDatabaseClass(
//        current_weather_id = FUTURE_WEATHER_VALUE,
//        lat = coord?.lat!!,
//        lon = coord.lon,
//        country = city.country,
//        id = city.id,
//        name = city.name,
//        population = city.population,
//        sunrise = city.sunrise,
//        sunset = city.sunset,
//        timezone = city.timezone,
//        cnt = it.cnt,
//        cod = it.cod,
//        all = clouds_all,
//        dt = dt,
//        dt_txt = dt_txt,
//        feels_like = feels_like,
//        grnd_level = grnd_level,
//        humidity = humidity,
//        pressure = pressure,
//        sea_level = sea_level,
//        temp = temp,
//        temp_kf = temp_kf,
//        temp_min = temp_min,
//        temp_max = temp_max,
//        after3h = after3h,
//        pod = pod,
//        description = description,
//        icon = icon,
//        weather_id = weather_id,
//        weather_main = weather_main,
//        deg = deg,
//        speed = speed,
//        message = it.message
//
//    )
//}
//
//
// */

class GithubTypeConverters {
        var gson: Gson = Gson()

        @TypeConverter
        fun stringToAllList(data: String?): CurrentWeatherDataClass? {
            if (data == null) {
                return null
            }
            val listType: Type = object : TypeToken<CurrentWeatherDataClass?>() {}.type
            return gson.fromJson(data, listType)
        }

        @TypeConverter
        fun currentWeatherDataClassToString(currentWeatherDataClass: CurrentWeatherDataClass): String {
            return gson.toJson(currentWeatherDataClass)
        }

        @TypeConverter
        fun stringToFutureWeatherDataClass(data: String?): FutureWeatherDataClass? {
            if (data == null) {
                return null
            }
            val listType: Type = object : TypeToken<FutureWeatherDataClass?>() {}.type
            return gson.fromJson(data, listType)
        }

        @TypeConverter
        fun futureWeatherDataClassToString(futureWeatherDataClass: FutureWeatherDataClass): String {
            return gson.toJson(futureWeatherDataClass)
        }
    }

//        var gson: Gson = Gson()
//
//        @TypeConverter
//        fun stringToSomeObjectList(data: String?): List<All> {
//            if (data == null) {
//                return Collections.emptyList()
//            }
//            val listType: Type =
//                object : TypeToken<List<All?>?>() {}.type
//            return gson.fromJson(data, listType)
//        }
//
//        @TypeConverter
//        fun someObjectListToString(someObjects: List<All?>?): String {
//            return gson.toJson(someObjects)
//        }
