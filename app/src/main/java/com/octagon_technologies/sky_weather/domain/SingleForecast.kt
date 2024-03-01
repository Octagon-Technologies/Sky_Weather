package com.octagon_technologies.sky_weather.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// ALWAYS fetch data km/h, C then do the convertions on the device
@Parcelize
data class SingleForecast(
    val temp: Double?,
    val feelsLike: Double?,
    val weatherCode: WeatherCode,
    val uvIndex: UVIndex,
    val wind: Wind,
    val cloudCover: Int?,
    val visibility: Int?,
    val dewPoint: Double?,
    val evapotranspiration: Double?,
    val surfacePressure: Int?,
    val seaLevelPressure: Int?,
    val terrestrialRadiation: Double?,
    val soilMoisture: Double?,
    val soilTemp: Double?,
    val snowDepth: Double?,
    val timeInEpochMillis: Long,
    val humidity: Int?
) : Parcelable

fun SingleForecast?.getFormattedTemp(isImperial: Boolean): String =
    this?.temp?.let { (if (isImperial) (temp * (9/5) + 32) else temp).toInt().toString() + "°" } ?: "--°"

fun SingleForecast?.getBasicFeelsLike(isImperial: Boolean): String =
    this?.feelsLike?.let { (if (isImperial) (feelsLike * (9/5) + 32) else feelsLike).toInt().toString() + "°" } ?: "--°"

fun SingleForecast?.getFormattedFeelsLike(isImperial: Boolean): String =
    "FeelsLike " + getBasicFeelsLike(isImperial)

fun SingleForecast?.getFormattedHumidity(): String = (this?.humidity?.toString() ?: "--") + "%"
fun SingleForecast?.getFormattedCloudCover(): String = (this?.cloudCover?.toString() ?: "--") + "%"

fun SingleForecast?.getFormattedVisibility(): String = (this?.visibility?.toString() ?: "--") + " m"

fun SingleForecast?.getFormattedTerrestrialRad(): String =
    (this?.terrestrialRadiation?.toInt()?.toString() ?: "--") + " W/m²"

fun SingleForecast?.getFormattedSnowDepth(): String =
    (this?.snowDepth?.toInt()?.toString() ?: "--") + " m"

fun SingleForecast?.getFormattedSoilMoisture(): String =
    (this?.soilMoisture?.toInt()?.toString() ?: "--") + " m³/m³"

fun SingleForecast?.getFormattedSurfacePressure(isImperial: Boolean) =
    if (isImperial)
        (this?.surfacePressure?.div(33.864)?.toInt()?.toString() ?: "--") + " inHg"
    else
        (this?.surfacePressure?.toString() ?: "--") + " mbar"

fun SingleForecast?.getFormattedSeaLevelPressure(isImperial: Boolean) =
    if (isImperial)
        (this?.seaLevelPressure?.div(33.864)?.toInt()?.toString() ?: "--") + " inHg"
    else
        (this?.seaLevelPressure?.toString() ?: "--") + " mbar"