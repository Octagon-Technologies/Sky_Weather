package com.octagon_technologies.sky_weather.ui.current_forecast

import com.octagon_technologies.sky_weather.utils.WindDirectionUnits
import timber.log.Timber

fun getUVIndex(wattsPerSquareMeter: Long): UVClass {
    val milliWattPerSquareMeter = (wattsPerSquareMeter * 0.1 / 25).toInt()
    Timber.d("milliWattPerSquareMeter is $milliWattPerSquareMeter and wattsPerSquareMeter is $wattsPerSquareMeter")
    val uvLevelString = when (milliWattPerSquareMeter) {
        0, 1, 2 -> "Low"
        3, 4, 5 -> "Moderate"
        6, 7 -> "High"
        8, 9, 10 -> "Very High"
        else -> "Extreme"
    }
    Timber.d("uvLevel is $uvLevelString")
    return UVClass(milliWattPerSquareMeter, uvLevelString)
}

fun getActualWind(mainWind: MainWind, windDirectionUnits: WindDirectionUnits?): String? {
    Timber.d("mainWind.windDirection?.value is ${mainWind.windDirection?.value}")
    val cardinalDirection =
        if (windDirectionUnits == WindDirectionUnits.DEGREES)
            "${mainWind.windDirection?.value?.toInt()?.toString() ?: "--"}°"
        else
            when (((mainWind.windDirection?.value ?: return null) / 22.5).toInt()) {
                2 -> "NNE"
                3 -> "NE"
                4 -> "ENE"
                5 -> "E"
                6 -> "ESE"
                7 -> "SE"
                8 -> "SSE"
                9 -> "S"
                10 -> "SSW"
                11 -> "SW"
                12 -> "WSW"
                13 -> "W"
                14 -> "WNW"
                15 -> "NW"
                16 -> "NNW"
                else -> "N"
            }

    val windSpeedInKm = mainWind.windSpeed?.let {
        if (it.units == "m/s") getWindSpeedInKm((it.value ?: 4).toLong())
        else "${it.value?.toInt() ?: "--"} mph"
    }

    Timber.d("result from getActualWind is ${"$cardinalDirection $windSpeedInKm"}")
    return "$cardinalDirection $windSpeedInKm"
}
/*
"baro_pressure":{"value":30.0689,"units":"inHg"}
1 inchOfMercury is 33.864 millibars so the above in millibars is 1015 mbars
 */

fun getWindSpeedInKm(windSpeedInMetersPerSecond: Long): String {
    return "${(windSpeedInMetersPerSecond * 3.6).toInt()} km/h"
}

fun Int.toMinutesFromHours() = (this * 60)

// Returns minutes and seconds respectively
fun Int.toHoursAndMinutes(): Pair<Int, Int> = Pair((this / 60), (this % 60))


fun getFinalMinutes(rise: String?, set: String?): String {
    if (rise == null || set == null) return "--"
    val riseList = rise.split(":").map { it.toInt() }
    val setList = set.split(":").map { it.toInt() }

    val diff =
        (setList[0].toMinutesFromHours() + setList[1]) - (riseList[0].toMinutesFromHours() + riseList[1])
    Timber.d("Minutes diff is $diff with rise as $rise and set as $set")
    return (if (diff >= 0) diff else (diff + 2400)).toHoursAndMinutes().second.toString()
}

fun getFinalHours(rise: String?, set: String?): String {
    if (rise == null || set == null) return "--"
    val riseList = rise.split(":").map { it.toInt() }
    val setList = set.split(":").map { it.toInt() }

    return (setList[0] - riseList[0]).let {
        if (it >= 0) it.toString() else (it + 24).toString()
    }
}