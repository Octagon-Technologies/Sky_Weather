package com.octagontechnologies.sky_weather.domain.daily

import com.octagontechnologies.sky_weather.domain.UVIndex
import com.octagontechnologies.sky_weather.domain.WeatherCode
import com.octagontechnologies.sky_weather.domain.Wind
import com.octagontechnologies.sky_weather.utils.Units


data class TimePeriod(
    val temp: Double?,
    val cloudCover: Int?,
    val feelsLike: Double?,
    val weatherCode: WeatherCode,
    val uvIndex: UVIndex,
    val wind: Wind,
    val surfacePressure: Double?,
    val seaLevelPressure: Double?,
    val humidity: Int?,
    val isDay: Boolean,
    val rainProbability: Int,
)

fun TimePeriod?.getFormattedTemp(units: Units?): String =
    this?.temp?.let { (if (units == Units.IMPERIAL) (temp * (9/5) + 32) else temp).toInt().toString() + "°" } ?: "--°"


fun TimePeriod?.getBasicFeelsLike(units: Units?): String =
    this?.feelsLike?.let { (if (units == Units.IMPERIAL) (feelsLike * (9/5) + 32) else feelsLike).toInt().toString() + "°" } ?: "--°"
fun TimePeriod?.getFormattedFeelsLike(): String =
    "FeelsLike " + (this?.feelsLike?.toInt()?.toString() ?: "--") + "°"

fun TimePeriod?.getFormattedHumidity(): String = (this?.humidity?.toInt()?.toString() ?: "--") + "%"
fun TimePeriod?.getFormattedCloudCover(): String =
    (this?.cloudCover?.toInt()?.toString() ?: "--") + "%"

fun TimePeriod?.getFormattedSurfacePressure(units: Units?) =
    (this?.surfacePressure?.toString() ?: "--") + if (units == Units.IMPERIAL) " inHg" else " mbar"

fun TimePeriod?.getFormattedSeaLevelPressure(units: Units?) =
    (this?.seaLevelPressure?.toString() ?: "--") + if (units == Units.IMPERIAL) " inHg" else " mbar"

fun TimePeriod?.getFormattedUVIndex() = (this?.uvIndex ?: UVIndex.Low).toString()
