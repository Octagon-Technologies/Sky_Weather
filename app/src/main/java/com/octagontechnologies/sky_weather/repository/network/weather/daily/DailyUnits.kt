package com.octagontechnologies.sky_weather.repository.network.weather.daily


import com.squareup.moshi.Json

data class DailyUnits(
    @Json(name = "apparent_temperature_max")
    val apparentTemperatureMax: String,
    @Json(name = "apparent_temperature_min")
    val apparentTemperatureMin: String,
    @Json(name = "cloud_cover_max")
    val cloudCoverMax: String,
    @Json(name = "cloud_cover_min")
    val cloudCoverMin: String,
    @Json(name = "precipitation_probability_max")
    val precipitationProbabilityMax: String,
    @Json(name = "precipitation_probability_min")
    val precipitationProbabilityMin: String,
    @Json(name = "pressure_msl_max")
    val pressureMslMax: String,
    @Json(name = "pressure_msl_min")
    val pressureMslMin: String,
    @Json(name = "relative_humidity_2m_max")
    val relativeHumidity2mMax: String,
    @Json(name = "relative_humidity_2m_min")
    val relativeHumidity2mMin: String,
    @Json(name = "sunrise")
    val sunrise: String,
    @Json(name = "sunset")
    val sunset: String,
    @Json(name = "surface_pressure_max")
    val surfacePressureMax: String,
    @Json(name = "surface_pressure_min")
    val surfacePressureMin: String,
    @Json(name = "temperature_2m_max")
    val temperature2mMax: String,
    @Json(name = "temperature_2m_min")
    val temperature2mMin: String,
    @Json(name = "time")
    val time: String,
    @Json(name = "uv_index_max")
    val uvIndexMax: String,
    @Json(name = "weather_code")
    val weatherCode: String,
    @Json(name = "wind_gusts_10m_max")
    val windGusts10mMax: String,
    @Json(name = "wind_gusts_10m_min")
    val windGusts10mMin: String,
    @Json(name = "wind_speed_10m_max")
    val windSpeed10mMax: String,
    @Json(name = "wind_speed_10m_min")
    val windSpeed10mMin: String
)