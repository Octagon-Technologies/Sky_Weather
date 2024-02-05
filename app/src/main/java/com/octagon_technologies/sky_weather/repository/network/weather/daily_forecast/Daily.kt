package com.octagon_technologies.sky_weather.repository.network.weather.daily_forecast


import com.squareup.moshi.Json

data class Daily(
    @Json(name = "time")
    val time: String,
    @Json(name = "values")
    val values: Values
)