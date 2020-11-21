package com.octagon_technologies.sky_weather.network.allergy_forecast


import com.squareup.moshi.Json

data class Allergy(
    @Json(name = "data")
    val data: List<Data>?,
    @Json(name = "message")
    val message: String?
)