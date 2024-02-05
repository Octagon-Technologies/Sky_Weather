package com.octagon_technologies.sky_weather.repository.network.weather.current_forecast


import com.squareup.moshi.Json

data class CurrentForecastResponse(
    @Json(name = "data")
    val `data`: Data,
    @Json(name = "location")
    val location: Location
)