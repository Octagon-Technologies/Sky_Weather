package com.example.kotlinweatherapp.network.hourly_forecast

import com.example.kotlinweatherapp.network.current_forecast.FeelsLike
import com.example.kotlinweatherapp.network.current_forecast.Humidity
import com.example.kotlinweatherapp.network.current_forecast.ObservationTime
import com.example.kotlinweatherapp.network.current_forecast.Temp
import com.squareup.moshi.Json

data class EachHourlyForecast (
    @Json(name = "feels_like")
    val feelsLike: FeelsLike?,

    @Json(name = "observation_time")
    val observationTime: ObservationTime?,

    val lat: Double?,
    val lon: Double?,
    val humidity: Humidity?,
    val temp: Temp?
)