package com.octagon_technologies.sky_weather.repository.network.weather.daily_forecast


import com.squareup.moshi.Json

data class Values(
    @Json(name = "cloudBaseAvg")
    val cloudBaseAvg: Double?,
    @Json(name = "cloudBaseMax")
    val cloudBaseMax: Double?,
    @Json(name = "cloudBaseMin")
    val cloudBaseMin: Double?,
    @Json(name = "cloudCeilingAvg")
    val cloudCeilingAvg: Double?,
    @Json(name = "cloudCeilingMax")
    val cloudCeilingMax: Double?,
    @Json(name = "cloudCeilingMin")
    val cloudCeilingMin: Double?,
    @Json(name = "cloudCoverAvg")
    val cloudCoverAvg: Double?,
    @Json(name = "cloudCoverMax")
    val cloudCoverMax: Double?,
    @Json(name = "cloudCoverMin")
    val cloudCoverMin: Double?,
    @Json(name = "dewPointAvg")
    val dewPointAvg: Double?,
    @Json(name = "dewPointMax")
    val dewPointMax: Double?,
    @Json(name = "dewPointMin")
    val dewPointMin: Double?,
    @Json(name = "evapotranspirationAvg")
    val evapotranspirationAvg: Double?,
    @Json(name = "evapotranspirationMax")
    val evapotranspirationMax: Double?,
    @Json(name = "evapotranspirationMin")
    val evapotranspirationMin: Double?,
    @Json(name = "evapotranspirationSum")
    val evapotranspirationSum: Double?,
    @Json(name = "freezingRainIntensityAvg")
    val freezingRainIntensityAvg: Double?,
    @Json(name = "freezingRainIntensityMax")
    val freezingRainIntensityMax: Double?,
    @Json(name = "freezingRainIntensityMin")
    val freezingRainIntensityMin: Double?,
    @Json(name = "humidityAvg")
    val humidityAvg: Double?,
    @Json(name = "humidityMax")
    val humidityMax: Double?,
    @Json(name = "humidityMin")
    val humidityMin: Double?,
    @Json(name = "iceAccumulationAvg")
    val iceAccumulationAvg: Double?,
    @Json(name = "iceAccumulationLweAvg")
    val iceAccumulationLweAvg: Double?,
    @Json(name = "iceAccumulationLweMax")
    val iceAccumulationLweMax: Double?,
    @Json(name = "iceAccumulationLweMin")
    val iceAccumulationLweMin: Double?,
    @Json(name = "iceAccumulationLweSum")
    val iceAccumulationLweSum: Double?,
    @Json(name = "iceAccumulationMax")
    val iceAccumulationMax: Double?,
    @Json(name = "iceAccumulationMin")
    val iceAccumulationMin: Double?,
    @Json(name = "iceAccumulationSum")
    val iceAccumulationSum: Double?,
    @Json(name = "moonriseTime")
    val moonriseTime: String?,
    @Json(name = "moonsetTime")
    val moonsetTime: String?,
    @Json(name = "precipitationProbabilityAvg")
    val precipitationProbabilityAvg: Double?,
    @Json(name = "precipitationProbabilityMax")
    val precipitationProbabilityMax: Double?,
    @Json(name = "precipitationProbabilityMin")
    val precipitationProbabilityMin: Double?,
    @Json(name = "pressureSurfaceLevelAvg")
    val pressureSurfaceLevelAvg: Double?,
    @Json(name = "pressureSurfaceLevelMax")
    val pressureSurfaceLevelMax: Double?,
    @Json(name = "pressureSurfaceLevelMin")
    val pressureSurfaceLevelMin: Double?,
    @Json(name = "rainAccumulationAvg")
    val rainAccumulationAvg: Double?,
    @Json(name = "rainAccumulationLweAvg")
    val rainAccumulationLweAvg: Double?,
    @Json(name = "rainAccumulationLweMax")
    val rainAccumulationLweMax: Double?,
    @Json(name = "rainAccumulationLweMin")
    val rainAccumulationLweMin: Double?,
    @Json(name = "rainAccumulationMax")
    val rainAccumulationMax: Double?,
    @Json(name = "rainAccumulationMin")
    val rainAccumulationMin: Double?,
    @Json(name = "rainAccumulationSum")
    val rainAccumulationSum: Double?,
    @Json(name = "rainIntensityAvg")
    val rainIntensityAvg: Double?,
    @Json(name = "rainIntensityMax")
    val rainIntensityMax: Double?,
    @Json(name = "rainIntensityMin")
    val rainIntensityMin: Double?,
    @Json(name = "sleetAccumulationAvg")
    val sleetAccumulationAvg: Double?,
    @Json(name = "sleetAccumulationLweAvg")
    val sleetAccumulationLweAvg: Double?,
    @Json(name = "sleetAccumulationLweMax")
    val sleetAccumulationLweMax: Double?,
    @Json(name = "sleetAccumulationLweMin")
    val sleetAccumulationLweMin: Double?,
    @Json(name = "sleetAccumulationLweSum")
    val sleetAccumulationLweSum: Double?,
    @Json(name = "sleetAccumulationMax")
    val sleetAccumulationMax: Double?,
    @Json(name = "sleetAccumulationMin")
    val sleetAccumulationMin: Double?,
    @Json(name = "sleetIntensityAvg")
    val sleetIntensityAvg: Double?,
    @Json(name = "sleetIntensityMax")
    val sleetIntensityMax: Double?,
    @Json(name = "sleetIntensityMin")
    val sleetIntensityMin: Double?,
    @Json(name = "snowAccumulationAvg")
    val snowAccumulationAvg: Double?,
    @Json(name = "snowAccumulationLweAvg")
    val snowAccumulationLweAvg: Double?,
    @Json(name = "snowAccumulationLweMax")
    val snowAccumulationLweMax: Double?,
    @Json(name = "snowAccumulationLweMin")
    val snowAccumulationLweMin: Double?,
    @Json(name = "snowAccumulationLweSum")
    val snowAccumulationLweSum: Double?,
    @Json(name = "snowAccumulationMax")
    val snowAccumulationMax: Double?,
    @Json(name = "snowAccumulationMin")
    val snowAccumulationMin: Double?,
    @Json(name = "snowAccumulationSum")
    val snowAccumulationSum: Double?,
    @Json(name = "snowIntensityAvg")
    val snowIntensityAvg: Double?,
    @Json(name = "snowIntensityMax")
    val snowIntensityMax: Double?,
    @Json(name = "snowIntensityMin")
    val snowIntensityMin: Double?,
    @Json(name = "sunriseTime")
    val sunriseTime: String?,
    @Json(name = "sunsetTime")
    val sunsetTime: String?,
    @Json(name = "temperatureApparentAvg")
    val temperatureApparentAvg: Double?,
    @Json(name = "temperatureApparentMax")
    val temperatureApparentMax: Double?,
    @Json(name = "temperatureApparentMin")
    val temperatureApparentMin: Double?,
    @Json(name = "temperatureAvg")
    val temperatureAvg: Double?,
    @Json(name = "temperatureMax")
    val temperatureMax: Double?,
    @Json(name = "temperatureMin")
    val temperatureMin: Double?,
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
    val visibilityAvg: Double?,
    @Json(name = "visibilityMax")
    val visibilityMax: Double?,
    @Json(name = "visibilityMin")
    val visibilityMin: Double?,
    @Json(name = "weatherCodeMax")
    val weatherCodeMax: Int?,
    @Json(name = "weatherCodeMin")
    val weatherCodeMin: Int?,
    @Json(name = "windDirectionAvg")
    val windDirectionAvg: Double?,
    @Json(name = "windGustAvg")
    val windGustAvg: Double?,
    @Json(name = "windGustMax")
    val windGustMax: Double?,
    @Json(name = "windGustMin")
    val windGustMin: Double?,
    @Json(name = "windSpeedAvg")
    val windSpeedAvg: Double?,
    @Json(name = "windSpeedMax")
    val windSpeedMax: Double?,
    @Json(name = "windSpeedMin")
    val windSpeedMin: Double?
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