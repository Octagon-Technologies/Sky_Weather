package com.octagon_technologies.sky_weather.repository.network.weather.daily_forecast


import com.squareup.moshi.Json

data class DailyForecastResponse(
    @Json(name = "location")
    val location: Location,
    @Json(name = "timelines")
    val timelines: Timelines
) {

}