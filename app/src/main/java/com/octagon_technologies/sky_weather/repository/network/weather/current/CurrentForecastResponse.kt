package com.octagon_technologies.sky_weather.repository.network.weather.current


import com.squareup.moshi.Json

data class CurrentForecastResponse(
    @Json(name = "current")
    val current: Current,
    @Json(name = "current_units")
    val currentUnits: CurrentUnits,
    @Json(name = "elevation")
    val elevation: Double,
    @Json(name = "generationtime_ms")
    val generationtimeMs: Double,
    @Json(name = "latitude")
    val latitude: Double,
    @Json(name = "longitude")
    val longitude: Double,
    @Json(name = "timezone")
    val timezone: String,
    @Json(name = "timezone_abbreviation")
    val timezoneAbbreviation: String,
    @Json(name = "utc_offset_seconds")
    val utcOffsetSeconds: Int
)