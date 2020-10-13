package com.example.kotlinweatherapp.network.allergy_forecast


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class Data(
    @Json(name = "grass_pollen")
    val grassPollen: Int?,
    @Json(name = "risk")
    val risk: String?,
    @Json(name = "tree_pollen")
    val treePollen: Int?,
    @Json(name = "weed_pollen")
    val weedPollen: Int?
)