package com.octagon_technologies.sky_weather.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.octagon_technologies.sky_weather.Units
import com.octagon_technologies.sky_weather.network.allergy_forecast.Allergy
import com.octagon_technologies.sky_weather.network.daily_forecast.EachDailyForecast
import com.octagon_technologies.sky_weather.network.hourly_forecast.EachHourlyForecast
import com.octagon_technologies.sky_weather.network.location.LocationItem
import com.octagon_technologies.sky_weather.network.lunar_forecast.LunarForecast
import com.octagon_technologies.sky_weather.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.network.selected_daily_forecast.SelectedDailyForecast
import com.octagon_technologies.sky_weather.network.single_forecast.SingleForecast
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

const val MAIN_BASE = "https://api.climacell.co/v3/weather/"

const val ALLERGY_BASE = "https://api.ambeedata.com/"
const val LUNAR_BASE = "https://api.solunar.org/"
const val LOCATION_BASE = "https://us1.locationiq.com/v1/"
const val LOCATION_KEY = "2a13f417c6d3f3"


val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

val okHttp: OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    .build()

val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(MAIN_BASE)
    .client(okHttp)
    .build()

// Allergy: curl -X GET "https://api.ambeedata.com/latest/pollen/by-lat-lng?lat=25.2048&lng=55.2708" -H "accept: application/json" -H "x-api-key: oWJChLwX5MM5MqUopRh1Qr3QLpEkQw6gExN5QQa0"
val allergyRetrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(okHttp)
    .baseUrl(ALLERGY_BASE)
    .build()

// Solar & Lunar rise and set: https://api.solunar.org/solunar/-1.2921,36.8219,20201002,3
val lunarRetrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(okHttp)
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(LUNAR_BASE)
    .build()

val locationRetrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(okHttp)
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(LOCATION_BASE)
    .build()


interface LocationApiService {
    //  https://api.locationiq.com/v1/autocomplete.php?key=2a13f417c6d3f3&q=Miller%20Estate&limit=10
    @GET("autocomplete.php")
    fun getLocationSuggestionsAsync(
        @Query("key") key: String = LOCATION_KEY,
        @Query("q") query: String,
        @Query("limit") limit: Int = 10
    ): Deferred<List<LocationItem>>

    //  https://us1.locationiq.com/v1/reverse.php?key=2a13f417c6d3f3&lat=-1.3135887888876425&lon=36.81903851535387&zoom=16&format=json
    @GET("reverse.php")
    fun getLocationNameFromCoordinatesAsync(
        @Query("key") key: String = LOCATION_KEY,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("zoom") zoom: Int = 16,
        @Query("format") format: String = "json"
    ): Deferred<ReverseGeoCodingLocation>
}

interface LunarForecastApiService {
    @GET("solunar/{lat},{lon},{date},{timezone}")
    fun getLunarForecastAsync(
        @Path("lat") lat: Double,
        @Path("lon") lon: Double,
        @Path("date") date: String,
        @Path("timezone") timezone: String = "0"
    ): Deferred<LunarForecast>
}

interface AllergyApiService {
    @Headers(
        "Content-Type: application/json",
        "x-api-key: oWJChLwX5MM5MqUopRh1Qr3QLpEkQw6gExN5QQa0"
    )
    @GET("latest/pollen/by-lat-lng")
    fun getAllergyAsync(
        @Query("lat") lat: Double,
        @Query("lng") lon: Double
    ): Deferred<Allergy>
}

interface WeatherForecastApiService {
    @GET("realtime")
    fun getCurrentForecastAsync(
        @Query("apikey") apiKey: String = MAIN_API_KEY,
        @Query("unit_system") unitSystem: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("fields", encoded = true) fields: String = fullAttrsString
    ): Deferred<SingleForecast>

    @GET("forecast/hourly")
    fun getHourlyForecastAsync(
        @Query("apikey") apiKey: String = MAIN_API_KEY,
        @Query("unit_system") unitSystem: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("start_time") startTimeInISO: String = "now",
        @Query("fields", encoded = true) fields: String = hourlyForecastAttrsString
    ): Deferred<List<EachHourlyForecast>>

    @GET("forecast/hourly")
    fun getSelectedHourlyForecastAsync(
        @Query("apikey") apiKey: String = MAIN_API_KEY,
        @Query("unit_system") unitSystem: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("start_time") startTimeInISO: String,
        @Query("end_time") endTimeInISO: String,
        @Query("fields", encoded = true) fields: String = fullAttrsString
    ): Deferred<List<SingleForecast>>

    @GET("forecast/daily")
    fun getDailyForecastAsync(
        @Query("apikey") apiKey: String = MAIN_API_KEY,
        @Query("unit_system") unitSystem: String = Units.METRIC.value,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("start_time") startTimeInISO: String = "now",
        @Query("fields", encoded = true) fields: String = basicDailyForecastAttrsString
    ): Deferred<List<EachDailyForecast>>


    // TODO: Problem is here !!! (Was using forecast/hourly instead of forecast/daily)
    @GET("forecast/daily")
    fun getSelectedDailyForecastAsync(
        @Query("apikey") apiKey: String = MAIN_API_KEY,
        @Query("unit_system") unitSystem: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("start_time") startTimeInISO: String,
        @Query("end_time") endTimeInISO: String,
        @Query("fields", encoded = true) fields: String = selectedDailyForecastAttrsString
    ): Deferred<List<SelectedDailyForecast>>
}

object WeatherForecastRetrofitItem {
    val weatherRetrofitService: WeatherForecastApiService by lazy {
        retrofit.create(WeatherForecastApiService::class.java)
    }
}

object AllergyForecastRetrofitItem {
    val allergyRetrofitService: AllergyApiService by lazy {
        allergyRetrofit.create(AllergyApiService::class.java)
    }
}

object LunarForecastRetrofitItem {
    val lunarRetrofitService: LunarForecastApiService by lazy {
        lunarRetrofit.create(LunarForecastApiService::class.java)
    }
}

object LocationRetrofitItem {
    val locationRetrofitService: LocationApiService by lazy {
        locationRetrofit.create(LocationApiService::class.java)
    }
}

