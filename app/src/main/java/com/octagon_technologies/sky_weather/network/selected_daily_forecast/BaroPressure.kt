package com.octagon_technologies.sky_weather.network.selected_daily_forecast


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaroPressure(
    @Json(name = "max")
    val max: Min?,
    @Json(name = "min")
    val min: Min?,
    @Json(name = "observation_time")
    val observationTime: String?
)