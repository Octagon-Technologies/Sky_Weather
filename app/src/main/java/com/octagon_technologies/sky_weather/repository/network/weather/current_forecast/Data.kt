package com.octagon_technologies.sky_weather.repository.network.weather.current_forecast


import com.squareup.moshi.Json

data class Data(
    @Json(name = "time")
    val time: String,
    @Json(name = "values")
    val values: Values
)