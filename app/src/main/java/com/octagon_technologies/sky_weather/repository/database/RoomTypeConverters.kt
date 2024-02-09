package com.octagon_technologies.sky_weather.repository.database

import androidx.room.TypeConverter
import com.octagon_technologies.sky_weather.domain.Allergy
import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.domain.Lunar
import com.octagon_technologies.sky_weather.domain.SingleForecast
import com.octagon_technologies.sky_weather.domain.daily.DailyForecast
import com.octagon_technologies.sky_weather.repository.database.location.favorites.LocalFavouriteLocation
import com.octagon_technologies.sky_weather.repository.database.location.recent.LocalRecentLocation
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
    private val lunarJsonAdapter = moshi.adapter(Lunar::class.java)
    private val locationJsonAdapter = moshi.adapter(Location::class.java)
    private val favouriteLocationJsonAdapter = moshi.adapter(LocalFavouriteLocation::class.java)
    private val recentLocationJsonAdapter = moshi.adapter(LocalRecentLocation::class.java)


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
    fun stringToAllergy(data: String): Allergy? = allergyJsonAdapter.fromJson(data)
    @TypeConverter
    fun allergyToString(allergy: Allergy?): String = allergyJsonAdapter.toJson(allergy)


    @TypeConverter
    fun stringToLunarForecast(data: String): Lunar? = lunarJsonAdapter.fromJson(data)
    @TypeConverter
    fun lunarForecastToString(lunarForecast: Lunar?): String =
        lunarJsonAdapter.toJson(lunarForecast)


    @TypeConverter
    fun stringToLocation(data: String): Location? = locationJsonAdapter.fromJson(data)
    @TypeConverter
    fun locationToString(location: Location?): String =
        locationJsonAdapter.toJson(location)

    @TypeConverter
    fun stringToRecentLocation(data: String): LocalRecentLocation? = recentLocationJsonAdapter.fromJson(data)
    @TypeConverter
    fun recentLocationToString(recentLocation: LocalRecentLocation?): String =
        recentLocationJsonAdapter.toJson(recentLocation)

    @TypeConverter
    fun stringToFavouriteLocation(data: String): LocalFavouriteLocation? =
        favouriteLocationJsonAdapter.fromJson(data)
    @TypeConverter
    fun favouriteLocationToString(favouriteLocation: LocalFavouriteLocation): String =
        favouriteLocationJsonAdapter.toJson(favouriteLocation)


    @TypeConverter
    fun stringToListOfHourlyForecast(data: String): List<SingleForecast>? =
        hourlyForecastJsonAdapter.fromJson(data)
    @TypeConverter
    fun listOfHourlyForecastToString(listOfSingleForecast: List<SingleForecast>?): String =
        hourlyForecastJsonAdapter.toJson(listOfSingleForecast?.toList())


    @TypeConverter
    fun stringToListOfEachDailyForecast(data: String): List<DailyForecast>? =
        dailyForecastJsonAdapter.fromJson(data)
    @TypeConverter
    fun listOfDailyForecastToString(listOfDailyForecast: List<DailyForecast>?): String =
        dailyForecastJsonAdapter.toJson(listOfDailyForecast?.toList())

}

