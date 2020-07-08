package com.example.kotlinweatherapp.network

import com.example.kotlinweatherapp.network.currentweather.CurrentWeatherDataClass
import com.example.kotlinweatherapp.network.futureforecast.FutureWeatherDataClass
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
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

//future forecast full url = https://api.openweathermap.org/data/2.5/forecast?q=Nairobi&appid=b482773d742c4ab710d2b62820ae3184
//future forecast =  ?q={cityName}&appid=b482773d742c4ab710d2b62820ae3184

interface FutureWeatherApiService {
    @GET("forecast")
    fun getFutureWeatherAsync(
        @Query("q") name: String,
        @Query("appid") key: String = KEY_VALUE
    ): Deferred<FutureWeatherDataClass>
}

interface CurrentWeatherApiService {
    @GET("weather")
    fun getCurrentWeatherAsync(
        @Query("q") name: String,
        @Query("appid") key: String = KEY_VALUE
    ): Deferred<CurrentWeatherDataClass>
}


// current forecast full url = http://api.openweathermap.org/data/2.5/weather?q=Nairobi&appid=b482773d742c4ab710d2b62820ae3184
// current forecast = ?q=Nairobi&appid=b482773d742c4ab710d2b62820ae3184

object FutureWeatherItem{
    val futureRetrofitService: FutureWeatherApiService by lazy {
        retrofit.create(FutureWeatherApiService::class.java)
    }
}

object CurrentWeatherItem{
    val currentRetrofitService: CurrentWeatherApiService by lazy {
        retrofit.create(CurrentWeatherApiService::class.java)
    }
}

