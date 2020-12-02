package com.octagon_technologies.sky_weather.repository.network.allergy_forecast


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Count(
    @Json(name = "grass_pollen")
    val grassPollen: Int?,
    @Json(name = "tree_pollen")
    val treePollen: Int?,
    @Json(name = "weed_pollen")
    val weedPollen: Int?
)