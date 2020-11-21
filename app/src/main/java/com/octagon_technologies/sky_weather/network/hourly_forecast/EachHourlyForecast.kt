package com.octagon_technologies.sky_weather.network.hourly_forecast

import com.octagon_technologies.sky_weather.network.single_forecast.*
import com.squareup.moshi.Json

data class EachHourlyForecast (
    @Json(name = "feels_like")
    val feelsLike: FeelsLike?,

    @Json(name = "weather_code")
    val weatherCode: WeatherCode,

    @Json(name = "observation_time")
    val observationTime: ObservationTime?,

    val lat: Double?,
    val lon: Double?,
    val humidity: Humidity?,
    val temp: Temp?
)