package com.octagon_technologies.sky_weather.repository.network.weather.current


import com.squareup.moshi.Json

data class CurrentUnits(
    @Json(name = "apparent_temperature")
    val apparentTemperature: String,
    @Json(name = "cloud_cover")
    val cloudCover: String,
    @Json(name = "dew_point_2m")
    val dewPoint2m: String,
    @Json(name = "evapotranspiration")
    val evapotranspiration: String,
    @Json(name = "interval")
    val interval: String,
    @Json(name = "is_day")
    val isDay: String,
    @Json(name = "precipitation")
    val precipitation: String,
    @Json(name = "precipitation_probability")
    val precipitationProbability: String,
    @Json(name = "pressure_msl")
    val pressureMsl: String,
    @Json(name = "relative_humidity_2m")
    val relativeHumidity2m: String,
    @Json(name = "snow_depth")
    val snowDepth: String,
    @Json(name = "soil_moisture_0_to_1cm")
    val soilMoisture0To1cm: String,
    @Json(name = "soil_temperature_0cm")
    val soilTemperature0cm: String,
    @Json(name = "surface_pressure")
    val surfacePressure: String,
    @Json(name = "temperature_2m")
    val temperature2m: String,
    @Json(name = "terrestrial_radiation")
    val terrestrialRadiation: String,
    @Json(name = "time")
    val time: String,
    @Json(name = "uv_index")
    val uvIndex: String,
    @Json(name = "visibility")
    val visibility: String,
    @Json(name = "weather_code")
    val weatherCode: String,
    @Json(name = "wind_direction_10m")
    val windDirection10m: String,
    @Json(name = "wind_gusts_10m")
    val windGusts10m: String,
    @Json(name = "wind_speed_10m")
    val windSpeed10m: String
)