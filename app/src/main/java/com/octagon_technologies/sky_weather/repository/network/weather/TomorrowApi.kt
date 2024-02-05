package com.octagon_technologies.sky_weather.repository.network.weather

import com.octagon_technologies.sky_weather.repository.network.weather.current_forecast.CurrentForecastResponse
import com.octagon_technologies.sky_weather.repository.network.weather.daily_forecast.DailyForecastResponse
import com.octagon_technologies.sky_weather.repository.network.weather.hourly_forecast.HourlyForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

// https://api.tomorrow.io/v4/weather/realtime?location=nairobi&units=metric&apikey=vMWmIOL72NA4yWspZWrr8et2baXoLlM8
const val TomorrowBaseUrl = "https://api.tomorrow.io/v4/weather"
const val TomorrowApiKey = "vMWmIOL72NA4yWspZWrr8et2baXoLlM8"


interface TomorrowApi {
    //    Latitude and Longitude (Decimal degree) location=42.3478,-71.0466
    @GET("realtime")
    suspend fun getCurrentForecast(
        @Query("location") location: String,
        @Query("units") units: String,
        @Query("apikey") key: String = TomorrowApiKey
    ): CurrentForecastResponse

    //    https://api.tomorrow.io/v4/weather/forecast?location=nairobi&timesteps=1d&units=metric&apikey=vMWmIOL72NA4yWspZWrr8et2baXoLlM8
    @GET("forecast")
    suspend fun getHourlyForecast(
        @Query("location") location: String,
        @Query("units") units: String,
        @Query("timesteps") timesteps: String = "1h",
        @Query("apikey") key: String = TomorrowApiKey
    ): HourlyForecastResponse

    //    https://api.tomorrow.io/v4/weather/forecast?location=nairobi&timesteps=1d&units=metric&apikey=vMWmIOL72NA4yWspZWrr8et2baXoLlM8
    @GET("forecast")
    suspend fun getDailyForecast(
        @Query("location") location: String,
        @Query("units") units: String,
        @Query("timesteps") timesteps: String = "1d",
        @Query("apikey") key: String = TomorrowApiKey
    ): DailyForecastResponse
}