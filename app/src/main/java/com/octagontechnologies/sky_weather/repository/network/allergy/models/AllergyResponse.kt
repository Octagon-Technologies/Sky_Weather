package com.octagontechnologies.sky_weather.repository.network.allergy.models


import com.squareup.moshi.Json

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AllergyResponse(
    @Json(name = "data")
    val data: List<Data>?,
    @Json(name = "message")
    val message: String?
)