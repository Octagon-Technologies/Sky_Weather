package com.octagon_technologies.sky_weather.repository.network.allergy_forecast


import com.squareup.moshi.Json

data class Allergy(
    @Json(name = "data")
    val data: List<Data>?,
    @Json(name = "message")
    val message: String?
)