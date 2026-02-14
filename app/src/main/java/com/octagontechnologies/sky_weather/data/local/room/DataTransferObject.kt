package com.octagontechnologies.sky_weather.data.local.room

import com.octagontechnologies.sky_weather.data.local.room.location.LocalLocation
import com.octagontechnologies.sky_weather.data.local.room.lunar.LocalLunar
import com.octagontechnologies.sky_weather.data.remote.api.lunar.models.LunarForecastResponse
import com.octagontechnologies.sky_weather.domain.model.Location
import com.octagontechnologies.sky_weather.domain.model.Lunar

fun LunarForecastResponse.toLunar() = Lunar(sunRise, sunSet, moonRise, moonSet)

fun LunarForecastResponse.toLocalLunar(): LocalLunar = LocalLunar(lunarForecast = toLunar())

fun Location.toLocalLocation() = LocalLocation(location = this)
