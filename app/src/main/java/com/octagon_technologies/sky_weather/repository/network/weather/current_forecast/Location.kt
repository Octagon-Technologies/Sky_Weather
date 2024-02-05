package com.octagon_technologies.sky_weather.repository.network.weather.current_forecast


import com.squareup.moshi.Json

data class Location(
    @Json(name = "lat")
    val lat: Double,
    @Json(name = "lon")
    val lon: Double,
    @Json(name = "name")
    val name: String,
    @Json(name = "type")
    val type: String
)