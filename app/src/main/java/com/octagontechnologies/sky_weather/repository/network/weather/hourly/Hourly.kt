package com.octagontechnologies.sky_weather.repository.network.weather.hourly


import com.octagontechnologies.sky_weather.domain.SingleForecast
import com.octagontechnologies.sky_weather.domain.UVIndex
import com.octagontechnologies.sky_weather.domain.WeatherCode
import com.octagontechnologies.sky_weather.domain.Wind
import com.squareup.moshi.Json
import org.joda.time.Instant

data class Hourly(
    @Json(name = "apparent_temperature")
    val apparentTemperature: List<Double>,
    @Json(name = "cloud_cover")
    val cloudCover: List<Int>,
    @Json(name = "dew_point_2m")
    val dewPoint2m: List<Double>,
    @Json(name = "evapotranspiration")
    val evapotranspiration: List<Double>,
    @Json(name = "is_day")
    val isDay: List<Int>,
    @Json(name = "precipitation")
    val precipitation: List<Double>,
    @Json(name = "precipitation_probability")
    val precipitationProbability: List<Int>,
    @Json(name = "pressure_msl")
    val pressureMsl: List<Double>,
    @Json(name = "relative_humidity_2m")
    val relativeHumidity2m: List<Int>,
    @Json(name = "snow_depth")
    val snowDepth: List<Double>,
    @Json(name = "soil_moisture_0_to_1cm")
    val soilMoisture0To1cm: List<Double>,
    @Json(name = "soil_temperature_0cm")
    val soilTemperature0cm: List<Double>,
    @Json(name = "surface_pressure")
    val surfacePressure: List<Double>,
    @Json(name = "temperature_2m")
    val temperature2m: List<Double>,
    @Json(name = "terrestrial_radiation")
    val terrestrialRadiation: List<Double>,
    @Json(name = "time")
    val time: List<String>,
    @Json(name = "uv_index")
    val uvIndex: List<Double>,
    @Json(name = "visibility")
    val visibility: List<Double>,
    @Json(name = "weather_code")
    val weatherCode: List<Int>,
    @Json(name = "wind_direction_10m")
    val windDirection10m: List<Int>,
    @Json(name = "wind_gusts_10m")
    val windGusts10m: List<Double>,
    @Json(name = "wind_speed_10m")
    val windSpeed10m: List<Double>
) {

    fun toListOfSingleForecast() =
        time.map {
            val index = time.indexOf(it)

            SingleForecast(
                temp = temperature2m[index],
                feelsLike = apparentTemperature[index],
                weatherCode = WeatherCode(weatherCode[index], precipitationProbability[index]),
                uvIndex = UVIndex.getUVIndexFromNum(uvIndex[index].toInt()),
                wind = Wind(windSpeed10m[index], windGusts10m[index], windDirection10m[index].toDouble()),
                cloudCover = cloudCover[index],
                visibility = visibility[index].toInt(),
                dewPoint = dewPoint2m[index],
                evapotranspiration = evapotranspiration[index],
                surfacePressure = surfacePressure[index].toInt(),
                seaLevelPressure = pressureMsl[index].toInt(),
                terrestrialRadiation = terrestrialRadiation[index],
                soilMoisture = soilMoisture0To1cm[index],
                soilTemp = soilTemperature0cm[index],
                snowDepth = snowDepth[index],
                timeInEpochMillis = Instant.parse(time[index]).millis,
                humidity = relativeHumidity2m[index]
            )
        }


}