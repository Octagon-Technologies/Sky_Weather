package com.example.kotlinweatherapp.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
const val KEY_VALUE = "b482773d742c4ab710d2b62820ae3184"

val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

//future forecast =  ?q={cityName}&appid=b482773d742c4ab710d2b62820ae3184

interface WeatherApiService {
    @GET("forecast")
    fun getWeatherAsync(
        @Query("q") name: String,
        @Query("appid") key: String = KEY_VALUE
    ): Deferred<WeatherDataClass>
}

object WeatherItem{
    val retrofitService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}