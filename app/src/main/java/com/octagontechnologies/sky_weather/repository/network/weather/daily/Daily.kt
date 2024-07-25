package com.octagontechnologies.sky_weather.repository.network.weather.daily


import com.octagontechnologies.sky_weather.domain.UVIndex
import com.octagontechnologies.sky_weather.domain.WeatherCode
import com.octagontechnologies.sky_weather.domain.Wind
import com.octagontechnologies.sky_weather.domain.daily.DailyForecast
import com.octagontechnologies.sky_weather.domain.daily.DailyLunar
import com.octagontechnologies.sky_weather.domain.daily.TimePeriod
import com.squareup.moshi.Json
import org.joda.time.Instant

data class Daily(
    @Json(name = "apparent_temperature_max")
    val apparentTemperatureMax: List<Double>,
    @Json(name = "apparent_temperature_min")
    val apparentTemperatureMin: List<Double>,
    @Json(name = "cloud_cover_max")
    val cloudCoverMax: List<Int>,
    @Json(name = "cloud_cover_min")
    val cloudCoverMin: List<Int>,
    @Json(name = "precipitation_probability_max")
    val precipitationProbabilityMax: List<Int>,
    @Json(name = "precipitation_probability_min")
    val precipitationProbabilityMin: List<Int>,
    @Json(name = "pressure_msl_max")
    val pressureMslMax: List<Double>,
    @Json(name = "pressure_msl_min")
    val pressureMslMin: List<Double>,
    @Json(name = "surface_pressure_max")
    val surfacePressureMax: List<Double>,
    @Json(name = "surface_pressure_min")
    val surfacePressureMin: List<Double>,
    @Json(name = "relative_humidity_2m_max")
    val relativeHumidity2mMax: List<Int>,
    @Json(name = "relative_humidity_2m_min")
    val relativeHumidity2mMin: List<Int>,
    @Json(name = "sunrise")
    val sunrise: List<String>,
    @Json(name = "sunset")
    val sunset: List<String>,
    @Json(name = "temperature_2m_max")
    val temperature2mMax: List<Double>,
    @Json(name = "temperature_2m_min")
    val temperature2mMin: List<Double>,
    @Json(name = "time")
    val time: List<String>,
    @Json(name = "uv_index_max")
    val uvIndexMax: List<Double>,
    @Json(name = "weather_code")
    val weatherCode: List<Int>,
    @Json(name = "wind_gusts_10m_max")
    val windGusts10mMax: List<Double>,
    @Json(name = "wind_gusts_10m_min")
    val windGusts10mMin: List<Double>,
    @Json(name = "wind_speed_10m_max")
    val windSpeed10mMax: List<Double>,
    @Json(name = "wind_speed_10m_min")
    val windSpeed10mMin: List<Double>
) {

    fun toListOfDailyForecast() = time.map { eachTime ->
        val index = time.indexOf(eachTime)
        val dayTimePeriod = toDayTimePeriod(index)
        val nightTimePeriod = toNightTimePeriod(index)

        DailyForecast(Instant.parse(eachTime).millis, dayTimePeriod, nightTimePeriod)
    }

    private fun toDayTimePeriod(index: Int) =
        TimePeriod(
            temp = temperature2mMax[index],
            cloudCover = cloudCoverMax[index],
            feelsLike = apparentTemperatureMax[index],
            weatherCode = WeatherCode(weatherCode[index], precipitationProbabilityMax[index]),
            uvIndex = UVIndex.getUVIndexFromNum(uvIndexMax[index].toInt()),
            wind = Wind(windSpeed10mMin[index], windGusts10mMin[index], null),
            surfacePressure = surfacePressureMin[index],
            seaLevelPressure = pressureMslMin[index],
            humidity = relativeHumidity2mMin[index],
            isDay = true,
            dailyLunar = DailyLunar(Instant.parse(sunrise[index]).millis, Instant.parse(sunset[index]).millis)
        )

    private fun toNightTimePeriod(index: Int) =
        TimePeriod(
            temp = temperature2mMin[index],
            cloudCover = cloudCoverMin[index],
            feelsLike = apparentTemperatureMin[index],
            weatherCode = WeatherCode(weatherCode[index], precipitationProbabilityMin[index]),
            uvIndex = UVIndex.getUVIndexFromNum(uvIndexMax[index].toInt()),
            wind = Wind(windSpeed10mMax[index], windGusts10mMax[index], null),
            surfacePressure = surfacePressureMax[index],
            seaLevelPressure = pressureMslMax[index],
            humidity = relativeHumidity2mMax[index],
            isDay = false,
            dailyLunar = DailyLunar(Instant.parse(sunrise[index]).millis, Instant.parse(sunset[index]).millis)
        )

}