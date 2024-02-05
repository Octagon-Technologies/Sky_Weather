package com.octagon_technologies.sky_weather.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SingleForecast(
    val temp: Double,
    val feelsLike: Double,
    val weatherCode: WeatherCode,
    val uvIndex: UVIndex,
    val wind: Wind,
    val cloudCover: Double?,
    val cloudCeiling: Double?,
    val visibility: Int,
    val dewPoint: Double,
    val pressure: Double,
    val timeInMillis: Long,
    val humidity: Double
) : Parcelable

val SingleForecast.formattedTemp: String
    get() = (this?.temp?.toInt()?.toString() ?: "--") + "°"

fun SingleForecast?.getFormattedTemp(): String = (this?.temp?.toInt()?.toString() ?: "--") + "°"
fun SingleForecast?.getFormattedFeelsLike(): String = "FeelsLike" +  (this?.feelsLike?.toInt()?.toString() ?: "--") + "°"
fun SingleForecast?.getFormattedHumidity(): String =  (this?.humidity?.toInt()?.toString() ?: "--") + "%"
fun SingleForecast?.getFormattedCloudCover(): String =  (this?.cloudCover?.toInt()?.toString() ?: "--") + "%"
fun SingleForecast?.getFormattedCloudCeiling(): String = if (this?.cloudCeiling !=  null) "${cloudCeiling.toInt()} m" else "None"