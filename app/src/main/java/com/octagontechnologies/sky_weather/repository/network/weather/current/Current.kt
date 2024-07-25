package com.octagontechnologies.sky_weather.repository.network.weather.current


import com.octagontechnologies.sky_weather.domain.SingleForecast
import com.octagontechnologies.sky_weather.domain.UVIndex
import com.octagontechnologies.sky_weather.domain.WeatherCode
import com.octagontechnologies.sky_weather.domain.Wind
import com.squareup.moshi.Json
import org.joda.time.Instant

data class Current(
    @Json(name = "apparent_temperature")
    val apparentTemperature: Double,
    @Json(name = "cloud_cover")
    val cloudCover: Int,
    @Json(name = "dew_point_2m")
    val dewPoint2m: Double,
    @Json(name = "evapotranspiration")
    val evapotranspiration: Double,
    @Json(name = "interval")
    val interval: Int,
    @Json(name = "is_day")
    val isDay: Int,
    @Json(name = "precipitation")
    val precipitation: Double,
    @Json(name = "precipitation_probability")
    val precipitationProbability: Int,
    @Json(name = "pressure_msl")
    val pressureMsl: Double,
    @Json(name = "relative_humidity_2m")
    val relativeHumidity2m: Int,
    @Json(name = "snow_depth")
    val snowDepth: Double,
    @Json(name = "soil_moisture_0_to_1cm")
    val soilMoisture0To1cm: Double,
    @Json(name = "soil_temperature_0cm")
    val soilTemperature0cm: Double,
    @Json(name = "surface_pressure")
    val surfacePressure: Double,
    @Json(name = "temperature_2m")
    val temperature2m: Double,
    @Json(name = "terrestrial_radiation")
    val terrestrialRadiation: Double,
    @Json(name = "time")
    val time: String,
    @Json(name = "uv_index")
    val uvIndex: Double,
    @Json(name = "visibility")
    val visibility: Double,
    @Json(name = "weather_code")
    val weatherCode: Int,
    @Json(name = "wind_direction_10m")
    val windDirection10m: Int,
    @Json(name = "wind_gusts_10m")
    val windGusts10m: Double,
    @Json(name = "wind_speed_10m")
    val windSpeed10m: Double
) {

    fun toSingleForecast() =
        SingleForecast(
            temp = temperature2m,
            feelsLike = apparentTemperature,
            weatherCode = WeatherCode(weatherCode, precipitationProbability),
            uvIndex = UVIndex.getUVIndexFromNum(uvIndex.toInt()),
            wind = Wind(windSpeed10m, windGusts10m, windDirection10m.toDouble()),
            cloudCover = cloudCover,
            visibility = visibility.toInt(),
            dewPoint = dewPoint2m,
            evapotranspiration = evapotranspiration,
            surfacePressure = surfacePressure.toInt(),
            seaLevelPressure = pressureMsl.toInt(),
            terrestrialRadiation = terrestrialRadiation,
            soilMoisture = soilMoisture0To1cm,
            soilTemp = soilTemperature0cm,
            snowDepth = snowDepth,
            timeInEpochMillis = Instant.parse(time).millis,
            humidity = relativeHumidity2m
        )

}