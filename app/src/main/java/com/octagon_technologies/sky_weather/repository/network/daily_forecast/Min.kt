package com.octagon_technologies.sky_weather.repository.network.daily_forecast


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Min(
    @Json(name = "units")
    val units: String?,
    @Json(name = "value")
    val value: Double?
)