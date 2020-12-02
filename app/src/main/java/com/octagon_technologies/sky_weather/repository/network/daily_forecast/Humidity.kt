package com.octagon_technologies.sky_weather.repository.network.daily_forecast


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Humidity(
    @Json(name = "max")
    val max: Max?,
    @Json(name = "min")
    val min: Min?,
    @Json(name = "observation_time")
    val observationTime: String?
)