package com.example.kotlinweatherapp.database

import androidx.room.TypeConverter
import com.example.kotlinweatherapp.network.allergy_forecast.Allergy
import com.example.kotlinweatherapp.network.current_forecast.SingleForecast
import com.example.kotlinweatherapp.network.hourly_forecast.EachHourlyForecast
import com.example.kotlinweatherapp.network.location.LocationItem
import com.example.kotlinweatherapp.network.lunar_forecast.LunarForecast
import com.example.kotlinweatherapp.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import timber.log.Timber

class GithubTypeConverters {
    private var moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @TypeConverter
    fun stringToSingleForecast(data: String?): SingleForecast? {
        val jsonAdapter = moshi.adapter(SingleForecast::class.java)
        return jsonAdapter.fromJson(data ?: return null)
    }
    @TypeConverter
    fun singleForecastToString(currentWeatherDataClass: SingleForecast): String {
        Timber.d(moshi.adapter(SingleForecast::class.java).toJson(currentWeatherDataClass))
        return moshi.adapter(SingleForecast::class.java).toJson(currentWeatherDataClass)
    }


    @TypeConverter
    fun stringToAllergy(data: String?): Allergy? {
        val jsonAdapter = moshi.adapter(Allergy::class.java)
        return jsonAdapter.fromJson(data ?: return null)
    }
    @TypeConverter
    fun allergyToString(allergy: Allergy?): String? {
        val jsonAdapter = moshi.adapter(Allergy::class.java)
        return jsonAdapter.toJson(allergy)
    }


    @TypeConverter
    fun stringToLunarForecast(data: String?): LunarForecast? {
        val jsonAdapter = moshi.adapter(LunarForecast::class.java)
        return jsonAdapter.fromJson(data ?: return null)
    }
    @TypeConverter
    fun lunarForecastToString(lunarForecast: LunarForecast?): String? {
        val jsonAdapter = moshi.adapter(LunarForecast::class.java)
        return jsonAdapter.toJson(lunarForecast ?: return null)
    }


    @TypeConverter
    fun stringToArrayOfEachHourlyForecast(data: String?): ArrayList<EachHourlyForecast>? {
        val type = Types.newParameterizedType(ArrayList::class.java, EachHourlyForecast::class.java)
        val jsonAdapter: JsonAdapter<ArrayList<EachHourlyForecast>> = moshi.adapter(type)
        Timber.d(jsonAdapter.fromJson(data ?: return null).toString())
        return jsonAdapter.fromJson(data)
    }
    @TypeConverter
    fun arrayOfEachHourlyForecastToString(arrayOfEachHourlyForecast: ArrayList<EachHourlyForecast>?): String? {
        val type = Types.newParameterizedType(ArrayList::class.java, EachHourlyForecast::class.java)
        val jsonAdapter: JsonAdapter<ArrayList<EachHourlyForecast>> = moshi.adapter(type)
        Timber.d(jsonAdapter.toJson(arrayOfEachHourlyForecast ?: return null))
        return jsonAdapter.toJson(arrayOfEachHourlyForecast)
    }


    @TypeConverter
    fun favouritesLocationToString(favouritesLocation: LocationItem?): String? {
        val jsonAdapter = moshi.adapter(LocationItem::class.java)
        return jsonAdapter.toJson(favouritesLocation ?: return null)
    }
    @TypeConverter
    fun stringToFavouritesLocation(data: String?): LocationItem? {
        val jsonAdapter = moshi.adapter(LocationItem::class.java)
        return jsonAdapter.fromJson(data ?: return null)
    }

    @TypeConverter
    fun stringToArrayOfFavouritesLocation(data: String?): ArrayList<LocationItem?>? {
        val typeToken = Types.newParameterizedType(ArrayList::class.java, LocationItem::class.java)
        val jsonAdapter: JsonAdapter<ArrayList<LocationItem?>?> = moshi.adapter(typeToken)
        return jsonAdapter.fromJson(data ?: return null)
    }
    @TypeConverter
    fun arrayOfFavouritesLocationOfString(arrayOfFavouritesLocations: ArrayList<LocationItem?>?): String? {
        val typeToken = Types.newParameterizedType(ArrayList::class.java, LocationItem::class.java)
        val jsonAdapter: JsonAdapter<ArrayList<LocationItem?>?> = moshi.adapter(typeToken)
        return jsonAdapter.toJson(arrayOfFavouritesLocations ?: return null)
    }

    @TypeConverter
    fun listOfStringToString(list: List<String?>?): String? {
        val typeToken = Types.newParameterizedType(List::class.java, String::class.java)
        val jsonAdapter: JsonAdapter<List<String?>> = moshi.adapter(typeToken)
        return jsonAdapter.toJson(list ?: return null)
    }
    @TypeConverter
    fun stringToListOfString(data: String?): List<String?>? {
        val typeToken = Types.newParameterizedType(List::class.java, String::class.java)
        val jsonAdapter: JsonAdapter<List<String?>> = moshi.adapter(typeToken)
        return jsonAdapter.fromJson(data ?: return null)
    }

    @TypeConverter
    fun stringToReverseGeoCodingLocation(data: String?): ReverseGeoCodingLocation? {
        val jsonAdapter = moshi.adapter(ReverseGeoCodingLocation::class.java)
        return jsonAdapter.fromJson(data ?: return null)
    }
    @TypeConverter
    fun reverseGeoCodingLocationToString(reversedLocation: ReverseGeoCodingLocation?): String? {
        val jsonAdapter = moshi.adapter(ReverseGeoCodingLocation::class.java)
        return jsonAdapter.toJson(reversedLocation ?: return null)
    }


}

