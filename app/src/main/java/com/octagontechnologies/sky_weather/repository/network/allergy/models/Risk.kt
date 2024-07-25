package com.octagontechnologies.sky_weather.repository.network.allergy.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Risk(
    @Json(name = "grass_pollen")
    val grassPollen: String?,
    @Json(name = "tree_pollen")
    val treePollen: String?,
    @Json(name = "weed_pollen")
    val weedPollen: String?
)