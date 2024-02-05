package com.octagon_technologies.sky_weather.repository.network.weather.current_forecast


import com.squareup.moshi.Json

data class Values(
    @Json(name = "cloudBase")
    val cloudBase: Double,
    @Json(name = "cloudCeiling")
    val cloudCeiling: Double,
    @Json(name = "cloudCover")
    val cloudCover: Double,
    @Json(name = "dewPoint")
    val dewPoint: Double,
    @Json(name = "freezingRainIntensity")
    val freezingRainIntensity: Int,
    @Json(name = "humidity")
    val humidity: Double,
    @Json(name = "precipitationProbability")
    val precipitationProbability: Int,
    @Json(name = "pressureSurfaceLevel")
    val pressureSurfaceLevel: Double,
    @Json(name = "rainIntensity")
    val rainIntensity: Int,
    @Json(name = "sleetIntensity")
    val sleetIntensity: Int,
    @Json(name = "snowIntensity")
    val snowIntensity: Int,
    @Json(name = "temperature")
    val temperature: Double,
    @Json(name = "temperatureApparent")
    val temperatureApparent: Double,
    @Json(name = "uvHealthConcern")
    val uvHealthConcern: Int,
    @Json(name = "uvIndex")
    val uvIndex: Int,
    @Json(name = "visibility")
    val visibility: Int,
    @Json(name = "weatherCode")
    val weatherCode: Int,
    @Json(name = "windDirection")
    val windDirection: Double,
    @Json(name = "windGust")
    val windGust: Double,
    @Json(name = "windSpeed")
    val windSpeed: Double
)