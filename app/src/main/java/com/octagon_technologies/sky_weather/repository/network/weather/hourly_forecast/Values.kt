package com.octagon_technologies.sky_weather.repository.network.weather.hourly_forecast


import com.squareup.moshi.Json

data class Values(
    @Json(name = "cloudBase")
    val cloudBase: Double?,
    @Json(name = "cloudCeiling")
    val cloudCeiling: Double?,
    @Json(name = "cloudCover")
    val cloudCover: Double?,
    @Json(name = "dewPoint")
    val dewPoint: Double?,
    @Json(name = "evapotranspiration")
    val evapotranspiration: Double?,
    @Json(name = "freezingRainIntensity")
    val freezingRainIntensity: Double?,
    @Json(name = "humidity")
    val humidity: Double?,
    @Json(name = "iceAccumulation")
    val iceAccumulation: Double?,
    @Json(name = "iceAccumulationLwe")
    val iceAccumulationLwe: Double?,
    @Json(name = "precipitationProbability")
    val precipitationProbability: Double?,
    @Json(name = "pressureSurfaceLevel")
    val pressureSurfaceLevel: Double?,
    @Json(name = "rainAccumulation")
    val rainAccumulation: Double?,
    @Json(name = "rainAccumulationLwe")
    val rainAccumulationLwe: Double?,
    @Json(name = "rainIntensity")
    val rainIntensity: Double?,
    @Json(name = "sleetAccumulation")
    val sleetAccumulation: Double?,
    @Json(name = "sleetAccumulationLwe")
    val sleetAccumulationLwe: Double?,
    @Json(name = "sleetIntensity")
    val sleetIntensity: Double?,
    @Json(name = "snowAccumulation")
    val snowAccumulation: Double?,
    @Json(name = "snowAccumulationLwe")
    val snowAccumulationLwe: Double?,
    @Json(name = "snowIntensity")
    val snowIntensity: Double?,
    @Json(name = "temperature")
    val temperature: Double,
    @Json(name = "temperatureApparent")
    val temperatureApparent: Double,
    @Json(name = "uvHealthConcern")
    val uvHealthConcern: Int?,
    @Json(name = "uvIndex")
    val uvIndex: Int?,
    @Json(name = "visibility")
    val visibility: Double?,
    @Json(name = "weatherCode")
    val weatherCode: Int,
    @Json(name = "windDirection")
    val windDirection: Double?,
    @Json(name = "windGust")
    val windGust: Double?,
    @Json(name = "windSpeed")
    val windSpeed: Double
)