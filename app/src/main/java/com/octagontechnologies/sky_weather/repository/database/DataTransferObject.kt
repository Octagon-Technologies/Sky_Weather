package com.octagontechnologies.sky_weather.repository.database

import com.octagontechnologies.sky_weather.domain.Location
import com.octagontechnologies.sky_weather.domain.Lunar
import com.octagontechnologies.sky_weather.repository.database.location.LocalLocation
import com.octagontechnologies.sky_weather.repository.database.lunar.LocalLunar
import com.octagontechnologies.sky_weather.repository.network.lunar.models.LunarForecastResponse


fun LunarForecastResponse.toLunar() =
    Lunar(sunRise, sunSet, moonRise, moonSet)

fun LunarForecastResponse.toLocalLunar(): LocalLunar =
    LocalLunar(lunarForecast = toLunar())

fun Location.toLocalLocation() = LocalLocation(location = this)