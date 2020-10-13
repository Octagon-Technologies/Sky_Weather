package com.example.kotlinweatherapp.ui.current_forecast

import com.example.kotlinweatherapp.network.current_forecast.SingleForecast
import com.example.kotlinweatherapp.network.current_forecast.WindDirection
import com.example.kotlinweatherapp.network.current_forecast.WindGust
import com.example.kotlinweatherapp.network.current_forecast.WindSpeed
import com.example.kotlinweatherapp.network.lunar_forecast.LunarForecast
import timber.log.Timber
import kotlin.random.Random

fun getUVIndex(wattsPerSquareMeter: Long): UVClass {
    val milliWattPerSquareMeter = (wattsPerSquareMeter * 0.1 / 25).toInt()
    Timber.d("milliWattPerSquareMeter is $milliWattPerSquareMeter and wattsPerSquareMeter is $wattsPerSquareMeter")
    val uvLevelString = when(milliWattPerSquareMeter) {
        0, 1, 2 -> "Low"
        3, 4, 5 -> "Moderate"
        6, 7 -> "High"
        8, 9, 10 -> "Very High"
        else -> "Extreme"
    }
    Timber.d("uvLevel is $uvLevelString")
    return UVClass(milliWattPerSquareMeter, uvLevelString)
}

fun getActualWind(mainWind: MainWind): String {
    Timber.d("mainWind.windDirection?.value is ${mainWind.windDirection?.value}")
    val cardinalDirection = when(((mainWind.windDirection?.value ?: 22.5) / 22.5).toInt()) {
        1 -> "N"
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
        17 -> "N"
        else -> throw IndexOutOfBoundsException("(mainWind.windDirection?.value ?: 22.5 / 22.5).toInt() is ${(mainWind.windDirection?.value ?: 22.5 / 22.5).toInt()}")
    }

    val windSpeedInKm = getWindSpeedInKm((mainWind.windSpeed?.value ?: 4).toLong())
    return "$cardinalDirection $windSpeedInKm"
}


fun getWindSpeedInKm(windSpeedInMs: Long): String {
    return "${(windSpeedInMs * 3.6).toInt()} km/h"
}

fun getGrassPollen(grassPollen: Int?): String {
    return when (grassPollen) {
        in 1..4 -> "Low"
        in 5..19 -> "Moderate"
        in 20..199 -> "High"
        else -> "Very High"
    }
}

fun getTreePollen(treePollen: Int?): String {
    return when(treePollen) {
        in 1..14 -> "Low"
        in 15..89 -> "Moderate"
        in 90..1499 -> "High"
        else -> "Very High"
    }
}

fun getWeedPollen(weedPollen: Int?): String {
    return when(weedPollen) {
        in 1..9 -> "Low"
        in 10..49 -> "Moderate"
        in 50..499 -> "High"
        else -> "Very High"
    }
}

fun getLunarBasicTime(lunarForecast: LunarForecast?): Long {
    val actualMoonRise = lunarForecast?.moonRise?.replace(":", "")?.toLong() ?: 1900  // 22:46 -> 2246
    val actualMoonSet = (lunarForecast?.moonSet?.replace(":", "")?.toLong())?.plus(2400) ?: 3100 // 10:17 -> 3417

    Timber.d("Actual time is ${actualMoonSet - actualMoonRise}")
    val lunarHours = (actualMoonSet - actualMoonRise).toString().take(2).toLong() + (actualMoonSet - actualMoonRise).toString().takeLast(2).toLong() / 60
    val lunarMins = ((actualMoonSet - actualMoonRise).toString().takeLast(2).toLong() % 60)

    Timber.d("lunarHours is $lunarHours and lunarMins is $lunarMins")

    val lunarBasicTime = "$lunarHours$lunarMins".toLong()

    Timber.d("lunarBasicTime is $lunarBasicTime")
    return lunarBasicTime
}

fun getSolarBasicTime(lunarForecast: LunarForecast?): Long {
    val actualSolarRise = lunarForecast?.sunRise?.replace(":", "")?.toLong() ?: 400
    val actualSolarSet = lunarForecast?.sunSet?.replace(":", "")?.toLong() ?: 1500

    val solarHours = (actualSolarSet - actualSolarRise).toString().take(2).toLong() + (actualSolarSet - actualSolarRise).toString().takeLast(2).toLong() / 60
    val solarMins = ((actualSolarSet - actualSolarRise).toString().takeLast(2).toLong() % 60)

    val solarBasicTime = "$solarHours$solarMins".toLong()
    Timber.d("solarBasicTime is $solarBasicTime")
    return solarBasicTime
}

fun getBasicForecastConditions(singleForecast: SingleForecast?): ArrayList<EachWeatherDescription> {
    val arrayOfWeatherDescriptions = ArrayList<EachWeatherDescription>()
    singleForecast?.let {
        val temp = it.temp
        val feelsLike = it.feelsLike
        val mainWind = MainWind(it.windDirection, it.windSpeed, it.windGust)
        val uvIndex = getUVIndex((it.surfaceShortwaveRadiation?.value ?: 50).toLong())
        Timber.d("uvIndex is $uvIndex")
        val humidity = "${it.humidity?.value?.toInt()}%"

        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "Temperature",
                "${temp?.value?.toInt()}°"
            )
        )
        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "RealFeel Temperature",
                "${feelsLike?.value?.toInt()}°"
            )
        )
        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "RealFeel Shade", "${
                    if (temp?.value?.toInt() == feelsLike?.value?.toInt()) {
                        feelsLike?.value?.toInt() ?: 24
                    } else {
                        Random.nextInt(temp?.value?.toInt() ?: 24, feelsLike?.value?.toInt() ?: 28)
                    }
                }°"
            )
        )
        arrayOfWeatherDescriptions.add(EachWeatherDescription("Wind", getActualWind(mainWind)))
        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "Max Wind Gusts",
                getWindSpeedInKm((mainWind.windGust?.value ?: 4).toLong())
            )
        )
        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "UV Index",
                "${uvIndex.uvIndex} ${uvIndex.uvLevelString}"
            )
        )
        arrayOfWeatherDescriptions.add(EachWeatherDescription("Humidity", humidity))
    }

    return arrayOfWeatherDescriptions
}

