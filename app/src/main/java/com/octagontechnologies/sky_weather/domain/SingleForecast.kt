package com.octagontechnologies.sky_weather.domain

import android.os.Parcelable
import com.octagontechnologies.sky_weather.utils.Units
import kotlinx.parcelize.Parcelize

// ALWAYS fetch data km/h, C then do the convertions on the device
@Parcelize
data class SingleForecast(
    val temp: Double?,
    internal val feelsLike: Double?,
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
) : Parcelable {

    companion object {
        val TEST_DUMMY = SingleForecast(
            23.0,
            24.2,
            WeatherCode(3, 34),
            UVIndex.getUVIndexFromNum(6),
            Wind(24.0, 23.0, 90.0),
            4,
            null,
            35.0,
            23.0,
            1500,
            1650,
            12.0,
            5.0,
            20.5,
            null,
            System.currentTimeMillis(),
            45
        )
    }

}

fun SingleForecast?.getFormattedTemp(units: Units?): String =
    this?.temp?.let { (if (units == Units.IMPERIAL) (temp * (9 / 5) + 32) else temp).toInt().toString() + "°" }
        ?: "--°"

fun SingleForecast?.getBasicFeelsLike(units: Units?): String =
    this?.feelsLike?.let {
        (if (units == Units.IMPERIAL) (feelsLike * (9 / 5) + 32) else feelsLike).toInt().toString() + "°"
    } ?: "--°"

fun SingleForecast?.getFormattedFeelsLike(units: Units?): String =
    "FeelsLike " + getBasicFeelsLike(units)

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