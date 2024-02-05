package com.octagon_technologies.sky_weather.repository.network.weather.hourly_forecast


import com.squareup.moshi.Json

data class Hourly(
    @Json(name = "time")
    val time: String,
    @Json(name = "values")
    val values: Values
)