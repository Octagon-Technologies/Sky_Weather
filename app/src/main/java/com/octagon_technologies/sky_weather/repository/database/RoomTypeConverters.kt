package com.octagon_technologies.sky_weather.repository.database

import androidx.room.TypeConverter
import com.octagon_technologies.sky_weather.domain.SingleForecast
import com.octagon_technologies.sky_weather.domain.daily.DailyForecast
import com.octagon_technologies.sky_weather.repository.network.allergy.models.AllergyResponse
import com.octagon_technologies.sky_weather.repository.network.location.models.SearchLocationResponse
import com.octagon_technologies.sky_weather.repository.network.lunar.models.LunarForecastResponse
import com.octagon_technologies.sky_weather.repository.network.location.reverse_geocoding_location.ReverseGeoCodingLocation
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
    private val allergyJsonAdapter = moshi.adapter(AllergyResponse::class.java)
    private val lunarJsonAdapter = moshi.adapter(LunarForecastResponse::class.java)
    private val searchLocationResponseJsonAdapter = moshi.adapter(SearchLocationResponse::class.java)
    private val reversedLocationJsonAdapter = moshi.adapter(ReverseGeoCodingLocation::class.java)

    private val hourlyForecastType: ParameterizedType =
        Types.newParameterizedType(List::class.java, SingleForecast::class.java)
    private val hourlyForecastJsonAdapter: JsonAdapter<List<SingleForecast>> =
        moshi.adapter(hourlyForecastType)

    private val dailyForecastType: ParameterizedType =
        Types.newParameterizedType(List::class.java, DailyForecast::class.java)
    private val dailyForecastJsonAdapter: JsonAdapter<List<DailyForecast>> =
        moshi.adapter(dailyForecastType)

    @TypeConverter
    fun stringToSingleForecast(data: String): SingleForecast? =
        singleForecastJsonAdapter.fromJson(data)

    @TypeConverter
    fun singleForecastToString(currentWeatherDataClass: SingleForecast): String =
        singleForecastJsonAdapter.toJson(currentWeatherDataClass)

    @TypeConverter
    fun stringToAllergy(data: String): AllergyResponse? = allergyJsonAdapter.fromJson(data)

    @TypeConverter
    fun allergyToString(allergy: AllergyResponse?): String = allergyJsonAdapter.toJson(allergy)

    @TypeConverter
    fun stringToLunarForecast(data: String): LunarForecastResponse? = lunarJsonAdapter.fromJson(data)

    @TypeConverter
    fun lunarForecastToString(lunarForecast: LunarForecastResponse?): String =
        lunarJsonAdapter.toJson(lunarForecast)

    @TypeConverter
    fun stringToArrayOfEachHourlyForecast(data: String): List<SingleForecast>? =
        hourlyForecastJsonAdapter.fromJson(data)

    @TypeConverter
    fun arrayOfEachHourlyForecastToString(listOfSingleForecast: List<SingleForecast>?): String =
        hourlyForecastJsonAdapter.toJson(listOfSingleForecast?.toList())

    @TypeConverter
    fun stringToArrayOfEachDailyForecast(data: String): List<DailyForecast>? =
        dailyForecastJsonAdapter.fromJson(data)

    @TypeConverter
    fun arrayOfEachDailyForecastToString(listOfDailyForecast: List<DailyForecast>?): String =
        dailyForecastJsonAdapter.toJson(listOfDailyForecast?.toList())

    @TypeConverter
    fun favouritesLocationToString(favouritesSearchLocationResponse: SearchLocationResponse?): String =
        searchLocationResponseJsonAdapter.toJson(favouritesSearchLocationResponse)

    @TypeConverter
    fun stringToFavouritesLocation(data: String): SearchLocationResponse? = searchLocationResponseJsonAdapter.fromJson(data)

    @TypeConverter
    fun stringToReverseGeoCodingLocation(data: String): ReverseGeoCodingLocation? =
        reversedLocationJsonAdapter.fromJson(data)

    @TypeConverter
    fun reverseGeoCodingLocationToString(reversedLocation: ReverseGeoCodingLocation?): String =
        reversedLocationJsonAdapter.toJson(reversedLocation)
}

