package com.octagon_technologies.sky_weather.repository.network.weather.hourly_forecast


import com.squareup.moshi.Json

data class Timelines(
    @Json(name = "hourly")
    val hourly: List<Hourly>
)