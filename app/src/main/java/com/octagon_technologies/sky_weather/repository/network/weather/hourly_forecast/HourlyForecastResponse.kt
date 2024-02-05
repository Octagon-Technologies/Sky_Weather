package com.octagon_technologies.sky_weather.repository.network.weather.hourly_forecast


import com.squareup.moshi.Json

data class HourlyForecastResponse(
    @Json(name = "location")
    val location: Location,
    @Json(name = "timelines")
    val timelines: Timelines
)