package com.octagon_technologies.sky_weather.domain.daily

import com.octagon_technologies.sky_weather.domain.Lunar
import com.octagon_technologies.sky_weather.domain.UVIndex
import com.octagon_technologies.sky_weather.domain.WeatherCode
import com.octagon_technologies.sky_weather.domain.Wind

data class DailyForecast(
    val timeInMillis: String,
    val dayTime: TimePeriod,
    val nightTime: TimePeriod
)


fun TimePeriod?.getFormattedTemp(): String = (this?.temp?.toInt()?.toString() ?: "--") + "°"
fun TimePeriod?.getFormattedFeelsLike(): String =
    "FeelsLike " + (this?.feelsLike?.toInt()?.toString() ?: "--") + "°"

fun TimePeriod?.getFormattedHumidity(): String = (this?.humidity?.toInt()?.toString() ?: "--") + "%"
fun TimePeriod?.getFormattedCloudCover(): String =
    (this?.cloudCover?.toInt()?.toString() ?: "--") + "%"

fun TimePeriod?.getFormattedCloudCeiling(): String =
    if (this?.cloudCeiling != null && cloudCeiling.toInt() != 0) "${cloudCeiling.toInt()}" else "-- m"

data class TimePeriod(
    val temp: Double?,
    val feelsLike: Double?,
    val weatherCode: WeatherCode,
    val uvIndex: UVIndex,
    val wind: Wind,
    val cloudCover: Double?,
    val cloudCeiling: Double?,
    val dewPoint: Double?,
    val pressure: Double?,
    val humidity: Double?,
    val isDay: Boolean,
    /*
    I've added this here instead of the main DailyForecast so that the selected Daily Forecast (day and night tabs)
    can access the info
     */
    val lunar: Lunar
)

/*
cloudCover
cloudCeiling
dewPoint
evapoTranspiration
humidity
sunrise, sunset, moonRise, moonSet
pressure
temp
feelsLike
visibility
wind - gust, speed, direction(const)

 */