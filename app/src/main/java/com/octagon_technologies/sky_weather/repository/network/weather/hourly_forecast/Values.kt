package com.octagon_technologies.sky_weather.repository.network.weather.hourly_forecast


import com.squareup.moshi.Json

data class Values(
    @Json(name = "cloudBase")
    val cloudBase: Double?,
    @Json(name = "cloudCeiling")
    val cloudCeiling: Double?,
    @Json(name = "cloudCover")
    val cloudCover: Double,
    @Json(name = "dewPoint")
    val dewPoint: Double,
    @Json(name = "evapotranspiration")
    val evapotranspiration: Double,
    @Json(name = "freezingRainIntensity")
    val freezingRainIntensity: Int,
    @Json(name = "humidity")
    val humidity: Double,
    @Json(name = "iceAccumulation")
    val iceAccumulation: Int,
    @Json(name = "iceAccumulationLwe")
    val iceAccumulationLwe: Int?,
    @Json(name = "precipitationProbability")
    val precipitationProbability: Int,
    @Json(name = "pressureSurfaceLevel")
    val pressureSurfaceLevel: Double,
    @Json(name = "rainAccumulation")
    val rainAccumulation: Double,
    @Json(name = "rainAccumulationLwe")
    val rainAccumulationLwe: Double?,
    @Json(name = "rainIntensity")
    val rainIntensity: Double,
    @Json(name = "sleetAccumulation")
    val sleetAccumulation: Int,
    @Json(name = "sleetAccumulationLwe")
    val sleetAccumulationLwe: Int?,
    @Json(name = "sleetIntensity")
    val sleetIntensity: Int,
    @Json(name = "snowAccumulation")
    val snowAccumulation: Int,
    @Json(name = "snowAccumulationLwe")
    val snowAccumulationLwe: Int?,
    @Json(name = "snowIntensity")
    val snowIntensity: Int,
    @Json(name = "temperature")
    val temperature: Double,
    @Json(name = "temperatureApparent")
    val temperatureApparent: Double,
    @Json(name = "uvHealthConcern")
    val uvHealthConcern: Int?,
    @Json(name = "uvIndex")
    val uvIndex: Int?,
    @Json(name = "visibility")
    val visibility: Double,
    @Json(name = "weatherCode")
    val weatherCode: Int,
    @Json(name = "windDirection")
    val windDirection: Double,
    @Json(name = "windGust")
    val windGust: Double,
    @Json(name = "windSpeed")
    val windSpeed: Double
)