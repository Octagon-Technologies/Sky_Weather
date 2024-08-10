package com.octagontechnologies.sky_weather.repository.network.lunar

import com.octagontechnologies.sky_weather.repository.network.lunar.models.LunarForecastResponse
import retrofit2.http.GET
import retrofit2.http.Path

const val LunarBaseUrl = "https://api.solunar.org/"

interface LunarForecastApi {
    @GET("solunar/{lat},{lon},{date},{timezone}")
    suspend fun getLunarForecast(
        @Path("lat") lat: Double,
        @Path("lon") lon: Double,
        @Path("date") date: String,
        @Path("timezone") timezone: String = "0"
    ): LunarForecastResponse
}