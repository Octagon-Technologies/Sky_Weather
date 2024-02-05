package com.octagon_technologies.sky_weather.domain

import android.os.Parcelable
import com.octagon_technologies.sky_weather.utils.Units
import com.octagon_technologies.sky_weather.utils.WindDirectionUnits
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import timber.log.Timber

@Parcelize
data class Wind(
    private val speed: Double?,
    val gust: Double?,
    val direction: Double?
) : Parcelable {
    @IgnoredOnParcel
    private val degreeToCardinalMap = mapOf(
        1 to "N", 2 to "NNE", 3 to "NE", 4 to "ENE", 5 to "E",
        6 to "ESE", 7 to "SE", 8 to "SSE", 9 to "S", 10 to "SSW",
        11 to "SW", 12 to "WSW", 13 to "W", 14 to "WNW", 15 to "NW", 16 to "NNW"
    )

    fun getWindSpeedWithDirection(units: Units?, windDirectionUnits: WindDirectionUnits?): String {
        val cardinalDirection = getCardinalDirection(windDirectionUnits ?: WindDirectionUnits.CARDINAL)
        val windSpeed = getWindSpeed(units ?: Units.METRIC)


        Timber.d("WindSpeedWithDirection with units as $units and windDirectionUnits as $windDirectionUnits is $cardinalDirection $windSpeed")
        return "$cardinalDirection $windSpeed"
    }

    fun getCardinalDirection(windDirectionUnits: WindDirectionUnits) =
        if (windDirectionUnits == WindDirectionUnits.DEGREES)
            "${direction?.toInt()?.toString() ?: "--"}°"
        else
            degreeToCardinalMap.getOrDefault(direction?.div(22.5)?.toInt(), "N")

    fun getWindSpeed(units: Units?): String {
        return if (units == Units.METRIC || units == null) "${((speed)?.times(3.6))?.toInt() ?: "--"} km/h"
        else "${speed?.toInt() ?: "--"} mph"
    }

}

/*
    @Json(name = "windDirection")
    val windDirection: Double,
    @Json(name = "windGust")
    val windGust: Double,
    @Json(name = "windSpeed")
    val windSpeed: Double
 */