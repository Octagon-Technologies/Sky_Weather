package com.octagon_technologies.sky_weather.repository.network.weather.daily_forecast


import com.squareup.moshi.Json

data class Values(
    @Json(name = "cloudBaseAvg")
    val cloudBaseAvg: Double,
    @Json(name = "cloudBaseMax")
    val cloudBaseMax: Double,
    @Json(name = "cloudBaseMin")
    val cloudBaseMin: Double,
    @Json(name = "cloudCeilingAvg")
    val cloudCeilingAvg: Double,
    @Json(name = "cloudCeilingMax")
    val cloudCeilingMax: Double,
    @Json(name = "cloudCeilingMin")
    val cloudCeilingMin: Int,
    @Json(name = "cloudCoverAvg")
    val cloudCoverAvg: Double,
    @Json(name = "cloudCoverMax")
    val cloudCoverMax: Double,
    @Json(name = "cloudCoverMin")
    val cloudCoverMin: Double,
    @Json(name = "dewPointAvg")
    val dewPointAvg: Double,
    @Json(name = "dewPointMax")
    val dewPointMax: Double,
    @Json(name = "dewPointMin")
    val dewPointMin: Double,
    @Json(name = "evapotranspirationAvg")
    val evapotranspirationAvg: Double,
    @Json(name = "evapotranspirationMax")
    val evapotranspirationMax: Double,
    @Json(name = "evapotranspirationMin")
    val evapotranspirationMin: Double,
    @Json(name = "evapotranspirationSum")
    val evapotranspirationSum: Double,
    @Json(name = "freezingRainIntensityAvg")
    val freezingRainIntensityAvg: Int,
    @Json(name = "freezingRainIntensityMax")
    val freezingRainIntensityMax: Int,
    @Json(name = "freezingRainIntensityMin")
    val freezingRainIntensityMin: Int,
    @Json(name = "humidityAvg")
    val humidityAvg: Double,
    @Json(name = "humidityMax")
    val humidityMax: Double,
    @Json(name = "humidityMin")
    val humidityMin: Double,
    @Json(name = "iceAccumulationAvg")
    val iceAccumulationAvg: Int,
    @Json(name = "iceAccumulationLweAvg")
    val iceAccumulationLweAvg: Int,
    @Json(name = "iceAccumulationLweMax")
    val iceAccumulationLweMax: Int,
    @Json(name = "iceAccumulationLweMin")
    val iceAccumulationLweMin: Int,
    @Json(name = "iceAccumulationLweSum")
    val iceAccumulationLweSum: Int,
    @Json(name = "iceAccumulationMax")
    val iceAccumulationMax: Int,
    @Json(name = "iceAccumulationMin")
    val iceAccumulationMin: Int,
    @Json(name = "iceAccumulationSum")
    val iceAccumulationSum: Int,
    @Json(name = "moonriseTime")
    val moonriseTime: String?,
    @Json(name = "moonsetTime")
    val moonsetTime: String,
    @Json(name = "precipitationProbabilityAvg")
    val precipitationProbabilityAvg: Double,
    @Json(name = "precipitationProbabilityMax")
    val precipitationProbabilityMax: Int,
    @Json(name = "precipitationProbabilityMin")
    val precipitationProbabilityMin: Int,
    @Json(name = "pressureSurfaceLevelAvg")
    val pressureSurfaceLevelAvg: Double,
    @Json(name = "pressureSurfaceLevelMax")
    val pressureSurfaceLevelMax: Double,
    @Json(name = "pressureSurfaceLevelMin")
    val pressureSurfaceLevelMin: Double,
    @Json(name = "rainAccumulationAvg")
    val rainAccumulationAvg: Double,
    @Json(name = "rainAccumulationLweAvg")
    val rainAccumulationLweAvg: Double,
    @Json(name = "rainAccumulationLweMax")
    val rainAccumulationLweMax: Double,
    @Json(name = "rainAccumulationLweMin")
    val rainAccumulationLweMin: Int,
    @Json(name = "rainAccumulationMax")
    val rainAccumulationMax: Double,
    @Json(name = "rainAccumulationMin")
    val rainAccumulationMin: Int,
    @Json(name = "rainAccumulationSum")
    val rainAccumulationSum: Double,
    @Json(name = "rainIntensityAvg")
    val rainIntensityAvg: Double,
    @Json(name = "rainIntensityMax")
    val rainIntensityMax: Double,
    @Json(name = "rainIntensityMin")
    val rainIntensityMin: Int,
    @Json(name = "sleetAccumulationAvg")
    val sleetAccumulationAvg: Int,
    @Json(name = "sleetAccumulationLweAvg")
    val sleetAccumulationLweAvg: Int,
    @Json(name = "sleetAccumulationLweMax")
    val sleetAccumulationLweMax: Int,
    @Json(name = "sleetAccumulationLweMin")
    val sleetAccumulationLweMin: Int,
    @Json(name = "sleetAccumulationLweSum")
    val sleetAccumulationLweSum: Int,
    @Json(name = "sleetAccumulationMax")
    val sleetAccumulationMax: Int,
    @Json(name = "sleetAccumulationMin")
    val sleetAccumulationMin: Int,
    @Json(name = "sleetIntensityAvg")
    val sleetIntensityAvg: Int,
    @Json(name = "sleetIntensityMax")
    val sleetIntensityMax: Int,
    @Json(name = "sleetIntensityMin")
    val sleetIntensityMin: Int,
    @Json(name = "snowAccumulationAvg")
    val snowAccumulationAvg: Int,
    @Json(name = "snowAccumulationLweAvg")
    val snowAccumulationLweAvg: Int,
    @Json(name = "snowAccumulationLweMax")
    val snowAccumulationLweMax: Int,
    @Json(name = "snowAccumulationLweMin")
    val snowAccumulationLweMin: Int,
    @Json(name = "snowAccumulationLweSum")
    val snowAccumulationLweSum: Int,
    @Json(name = "snowAccumulationMax")
    val snowAccumulationMax: Int,
    @Json(name = "snowAccumulationMin")
    val snowAccumulationMin: Int,
    @Json(name = "snowAccumulationSum")
    val snowAccumulationSum: Int,
    @Json(name = "snowIntensityAvg")
    val snowIntensityAvg: Int,
    @Json(name = "snowIntensityMax")
    val snowIntensityMax: Int,
    @Json(name = "snowIntensityMin")
    val snowIntensityMin: Int,
    @Json(name = "sunriseTime")
    val sunriseTime: String,
    @Json(name = "sunsetTime")
    val sunsetTime: String,
    @Json(name = "temperatureApparentAvg")
    val temperatureApparentAvg: Double,
    @Json(name = "temperatureApparentMax")
    val temperatureApparentMax: Double,
    @Json(name = "temperatureApparentMin")
    val temperatureApparentMin: Double,
    @Json(name = "temperatureAvg")
    val temperatureAvg: Double,
    @Json(name = "temperatureMax")
    val temperatureMax: Double,
    @Json(name = "temperatureMin")
    val temperatureMin: Double,
    @Json(name = "uvHealthConcernAvg")
    val uvHealthConcernAvg: Int?,
    @Json(name = "uvHealthConcernMax")
    val uvHealthConcernMax: Int?,
    @Json(name = "uvHealthConcernMin")
    val uvHealthConcernMin: Int?,
    @Json(name = "uvIndexAvg")
    val uvIndexAvg: Int?,
    @Json(name = "uvIndexMax")
    val uvIndexMax: Int?,
    @Json(name = "uvIndexMin")
    val uvIndexMin: Int?,
    @Json(name = "visibilityAvg")
    val visibilityAvg: Double,
    @Json(name = "visibilityMax")
    val visibilityMax: Double,
    @Json(name = "visibilityMin")
    val visibilityMin: Double,
    @Json(name = "weatherCodeMax")
    val weatherCodeMax: Int,
    @Json(name = "weatherCodeMin")
    val weatherCodeMin: Int,
    @Json(name = "windDirectionAvg")
    val windDirectionAvg: Double,
    @Json(name = "windGustAvg")
    val windGustAvg: Double,
    @Json(name = "windGustMax")
    val windGustMax: Double,
    @Json(name = "windGustMin")
    val windGustMin: Double,
    @Json(name = "windSpeedAvg")
    val windSpeedAvg: Double,
    @Json(name = "windSpeedMax")
    val windSpeedMax: Double,
    @Json(name = "windSpeedMin")
    val windSpeedMin: Double
)

/*
      {
        "time": "2024-02-06T03:00:00Z",
        "values": {
          "cloudBaseAvg": 0.29,
          "cloudBaseMax": 2.2,
          "cloudBaseMin": 0,
          "cloudCeilingAvg": 0,
          "cloudCeilingMax": 0,
          "cloudCeilingMin": 0,
          "cloudCoverAvg": 72.51,
          "cloudCoverMax": 100,
          "cloudCoverMin": 6.23,
          "dewPointAvg": 10.76,
          "dewPointMax": 12.07,
          "dewPointMin": 8.36,
          "evapotranspirationAvg": 0.288,
          "evapotranspirationMax": 0.779,
          "evapotranspirationMin": 0.034,
          "evapotranspirationSum": 6.622,
          "humidityAvg": 53.95,
          "humidityMax": 75.43,
          "humidityMin": 30.83,
          "moonriseTime": "2024-02-06T00:02:27Z",
          "moonsetTime": "2024-02-06T12:41:11Z",
          "precipitationProbabilityAvg": 0,
          "pressureSurfaceLevelAvg": 829.34,
          "pressureSurfaceLevelMax": 831.2,
          "pressureSurfaceLevelMin": 826.93,
          "sunriseTime": "2024-02-06T03:42:00Z",
          "sunsetTime": "2024-02-06T15:51:00Z",
          "temperatureApparentAvg": 21.07,
          "temperatureApparentMax": 26.74,
          "temperatureApparentMin": 15.66,
          "temperatureAvg": 21.13,
          "temperatureMax": 27.43,
          "temperatureMin": 15.66,
//          "uvHealthConcernAvg": 2,
//          "uvHealthConcernMax": 5,
//          "uvHealthConcernMin": 0,
          "uvIndexAvg": 4,
//          "uvIndexMax": 15,
//          "uvIndexMin": 0,
          "visibilityAvg": 24.13,
          "visibilityMax": 24.14,
          "visibilityMin": 24.13,
          "weatherCodeMax": 1001,
          "weatherCodeMin": 1001,
          "windDirectionAvg": 50.95,
          "windGustAvg": 8.53,
          "windGustMax": 10.93,
          "windGustMin": 5.75,
          "windSpeedAvg": 4.96,
          "windSpeedMax": 7.85,
          "windSpeedMin": 2.07
        }
 */