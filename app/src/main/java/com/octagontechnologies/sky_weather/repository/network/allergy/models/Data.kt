package com.octagontechnologies.sky_weather.repository.network.allergy.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "Count")
    val count: Count?,
    @Json(name = "Risk")
    val risk: Risk?
)