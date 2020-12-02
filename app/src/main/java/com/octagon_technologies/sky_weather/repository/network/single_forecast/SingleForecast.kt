package com.octagon_technologies.sky_weather.repository.network.single_forecast

import com.octagon_technologies.sky_weather.network.single_forecast.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SingleForecast(
    @Json(name = "baro_pressure")
    val baroPressure: BaroPressure?,

    @Json(name = "cloud_base")
    val cloudBase: CloudBase?,

    @Json(name = "cloud_ceiling")
    val cloudCeiling: CloudCeiling?,

    @Json(name = "cloud_cover")
    val cloudCover: CloudCover?,

    @Json(name = "dewpoint")
    val dewPoint: Dewpoint?,

    @Json(name = "feels_like")
    val feelsLike: FeelsLike?,

    val humidity: Humidity?,

    val lat: Double?,
    val lon: Double?,

    @Json(name = "moon_phase")
    val moonPhase: MoonPhase?,

    @Json(name = "observation_time")
    val observationTime: ObservationTime?,

    val precipitation: Precipitation?,

    @Json(name = "precipitation_type")
    val precipitationType: PrecipitationType?,

    val sunrise: Sunrise?,
    val sunset: Sunset?,

    @Json(name = "surface_shortwave_radiation")
    val surfaceShortwaveRadiation: SurfaceShortwaveRadiation?,

    val temp: Temp?,
    val visibility: Visibility?,

    @Json(name = "weather_code")
    val weatherCode: WeatherCode?,

    @Json(name = "wind_direction")
    val windDirection: WindDirection?,

    @Json(name = "wind_gust")
    val windGust: WindGust?,

    @Json(name = "wind_speed")
    val windSpeed: WindSpeed?
)