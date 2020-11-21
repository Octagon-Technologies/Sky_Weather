package com.octagon_technologies.sky_weather.network.selected_daily_forecast

import com.octagon_technologies.sky_weather.network.single_forecast.ObservationTime
import com.octagon_technologies.sky_weather.network.single_forecast.WeatherCode
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SelectedDailyForecast(
    @Json(name = "baro_pressure")
    val baroPressure: List<BaroPressure>?,
    @Json(name = "feels_like")
    val feelsLike: List<FeelsLike>?,
    @Json(name = "humidity")
    val humidity: List<Humidity>?,
    @Json(name = "lat")
    val lat: Double?,
    @Json(name = "lon")
    val lon: Double?,
    @Json(name = "moon_phase")
    val moonPhase: MoonPhase?,
    @Json(name = "observation_time")
    val observationTime: ObservationTime?,
    @Json(name = "precipitation")
    val precipitation: List<Precipitation>?,
    @Json(name = "precipitation_accumulation")
    val precipitationAccumulation: PrecipitationAccumulation?,
    @Json(name = "sunrise")
    val sunrise: Sunrise?,
    @Json(name = "sunset")
    val sunset: Sunset?,
    @Json(name = "temp")
    val temp: List<Temp>?,
    @Json(name = "visibility")
    val visibility: List<Visibility>?,
    @Json(name = "weather_code")
    val weatherCode: WeatherCode?,
    @Json(name = "wind_direction")
    val windDirection: List<WindDirection>?,
    @Json(name = "wind_speed")
    val windSpeed: List<WindSpeed>?
)