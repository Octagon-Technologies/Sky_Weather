package com.example.kotlinweatherapp.network

import com.squareup.moshi.Json

data class Rain(
    @Json(name = "3h")
    val after3h : Double
)