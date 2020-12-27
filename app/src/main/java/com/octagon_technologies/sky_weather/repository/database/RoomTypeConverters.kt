package com.octagon_technologies.sky_weather.repository.database

import androidx.room.TypeConverter
import com.octagon_technologies.sky_weather.repository.network.allergy_forecast.Allergy
import com.octagon_technologies.sky_weather.repository.network.daily_forecast.EachDailyForecast
import com.octagon_technologies.sky_weather.repository.network.hourly_forecast.EachHourlyForecast
import com.octagon_technologies.sky_weather.repository.network.location.Location
import com.octagon_technologies.sky_weather.repository.network.lunar_forecast.LunarForecast
import com.octagon_technologies.sky_weather.repository.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.repository.network.single_forecast.SingleForecast
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.ParameterizedType

class RoomTypeConverters {
    private var moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val singleForecastJsonAdapter = moshi.adapter(SingleForecast::class.java)
    private val allergyJsonAdapter = moshi.adapter(Allergy::class.java)
    private val lunarJsonAdapter = moshi.adapter(LunarForecast::class.java)
    private val locationJsonAdapter = moshi.adapter(Location::class.java)
    private val reversedLocationJsonAdapter = moshi.adapter(ReverseGeoCodingLocation::class.java)

    private val hourlyForecastType: ParameterizedType =
        Types.newParameterizedType(List::class.java, EachHourlyForecast::class.java)
    private val hourlyForecastJsonAdapter: JsonAdapter<List<EachHourlyForecast>> =
        moshi.adapter(hourlyForecastType)

    private val dailyForecastType: ParameterizedType =
        Types.newParameterizedType(List::class.java, EachDailyForecast::class.java)
    private val dailyForecastJsonAdapter: JsonAdapter<List<EachDailyForecast>> =
        moshi.adapter(dailyForecastType)

    @TypeConverter
    fun stringToSingleForecast(data: String): SingleForecast? =
        singleForecastJsonAdapter.fromJson(data)

    @TypeConverter
    fun singleForecastToString(currentWeatherDataClass: SingleForecast): String =
        singleForecastJsonAdapter.toJson(currentWeatherDataClass)

    @TypeConverter
    fun stringToAllergy(data: String): Allergy? = allergyJsonAdapter.fromJson(data)

    @TypeConverter
    fun allergyToString(allergy: Allergy?): String = allergyJsonAdapter.toJson(allergy)

    @TypeConverter
    fun stringToLunarForecast(data: String): LunarForecast? = lunarJsonAdapter.fromJson(data)

    @TypeConverter
    fun lunarForecastToString(lunarForecast: LunarForecast?): String =
        lunarJsonAdapter.toJson(lunarForecast)

    @TypeConverter
    fun stringToArrayOfEachHourlyForecast(data: String): ArrayList<EachHourlyForecast>? =
        hourlyForecastJsonAdapter.fromJson(data) as ArrayList<EachHourlyForecast>?

    @TypeConverter
    fun arrayOfEachHourlyForecastToString(arrayOfEachHourlyForecast: ArrayList<EachHourlyForecast>?): String =
        hourlyForecastJsonAdapter.toJson(arrayOfEachHourlyForecast?.toList())

    @TypeConverter
    fun stringToArrayOfEachDailyForecast(data: String): ArrayList<EachDailyForecast>? =
        dailyForecastJsonAdapter.fromJson(data) as ArrayList<EachDailyForecast>?

    @TypeConverter
    fun arrayOfEachDailyForecastToString(arrayOfEachDailyForecast: ArrayList<EachDailyForecast>?): String =
        dailyForecastJsonAdapter.toJson(arrayOfEachDailyForecast?.toList())

    @TypeConverter
    fun favouritesLocationToString(favouritesLocation: Location?): String =
        locationJsonAdapter.toJson(favouritesLocation)

    @TypeConverter
    fun stringToFavouritesLocation(data: String): Location? = locationJsonAdapter.fromJson(data)

    @TypeConverter
    fun stringToReverseGeoCodingLocation(data: String): ReverseGeoCodingLocation? =
        reversedLocationJsonAdapter.fromJson(data)

    @TypeConverter
    fun reverseGeoCodingLocationToString(reversedLocation: ReverseGeoCodingLocation?): String =
        reversedLocationJsonAdapter.toJson(reversedLocation)
}

