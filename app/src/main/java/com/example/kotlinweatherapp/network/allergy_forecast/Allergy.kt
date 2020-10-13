package com.example.kotlinweatherapp.network.allergy_forecast


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class Allergy(
    @Json(name = "data")
    val data: Data?,
    @Json(name = "message")
    val message: String?
)