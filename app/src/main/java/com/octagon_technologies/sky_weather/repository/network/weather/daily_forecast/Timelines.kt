package com.octagon_technologies.sky_weather.repository.network.weather.daily_forecast


import com.squareup.moshi.Json

data class Timelines(
    @Json(name = "daily")
    val daily: List<Daily>
)