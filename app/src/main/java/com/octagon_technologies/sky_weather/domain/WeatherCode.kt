package com.octagon_technologies.sky_weather.domain

import android.os.Build
import android.os.Parcelable
import com.octagon_technologies.sky_weather.R
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherCode(private val code: Int, private val rainProbability: Int) : Parcelable {
    @IgnoredOnParcel
    private val codeMap = mapOf(
        0 to "Unknown",
        1000 to "Clear, Sunny",
        1100 to "Mostly Clear",
        1101 to "Partly Cloudy",
        1102 to "Mostly Cloudy",
        1001 to "Cloudy",
        2000 to "Fog",
        2100 to "Light Fog",
        4000 to "Drizzle",
        4001 to "Rain",
        4200 to "Light Rain",
        4201 to "Heavy Rain",
        5000 to "Snow",
        5001 to "Flurries",
        5100 to "Light Snow",
        5101 to "Heavy Snow",
        6000 to "Freezing Drizzle",
        6001 to "Freezing Rain",
        6200 to "Light Freezing Rain",
        6201 to "Heavy Freezing Rain",
        7000 to "Ice Pellets",
        7101 to "Heavy Ice Pellets",
        7102 to "Light Ice Pellets",
        8000 to "Thunderstorm"
    )

    fun getWeatherTitle() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        codeMap.getOrDefault(code, "Fair")
    } else {
        codeMap[code]
    }

    fun getWeatherIcon(hourOfDay: Int?) =
        R.drawable.yellow_sun
}