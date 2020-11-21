package com.octagon_technologies.sky_weather.network.daily_forecast


import com.octagon_technologies.sky_weather.network.single_forecast.ObservationTime
import com.octagon_technologies.sky_weather.network.single_forecast.WeatherCode
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EachDailyForecast(
    @Json(name = "humidity")
    val humidity: List<Humidity>?,
    @Json(name = "lat")
    val lat: Double?,
    @Json(name = "lon")
    val lon: Double?,
    @Json(name = "observation_time")
    val observationTime: ObservationTime?,
    @Json(name = "temp")
    val temp: List<Temp>?,
    @Json(name = "weather_code")
    val weatherCode: WeatherCode?
)