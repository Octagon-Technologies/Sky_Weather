package com.octagon_technologies.sky_weather.repository.network.allergy_forecast


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "Count")
    val count: Count?,
    @Json(name = "Risk")
    val risk: Risk?
)