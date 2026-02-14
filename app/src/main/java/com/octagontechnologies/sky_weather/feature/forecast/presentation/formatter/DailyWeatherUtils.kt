package com.octagontechnologies.sky_weather.feature.forecast.presentation.formatter

import com.octagontechnologies.sky_weather.core.model.preferences.Units
import com.octagontechnologies.sky_weather.core.model.preferences.WindDirectionUnits
import com.octagontechnologies.sky_weather.domain.model.daily.TimePeriod
import com.octagontechnologies.sky_weather.domain.model.daily.getBasicFeelsLike
import com.octagontechnologies.sky_weather.domain.model.daily.getFormattedCloudCover
import com.octagontechnologies.sky_weather.domain.model.daily.getFormattedHumidity
import com.octagontechnologies.sky_weather.domain.model.daily.getFormattedSeaLevelPressure
import com.octagontechnologies.sky_weather.domain.model.daily.getFormattedSurfacePressure
import com.octagontechnologies.sky_weather.domain.model.daily.getFormattedTemp

fun TimePeriod?.getWeatherConditions(
    units: Units?,
    windDirectionUnits: WindDirectionUnits?,
) = mapOf(
    "Temperature" to getFormattedTemp(units),
    "FeelsLike Temperature" to
        getBasicFeelsLike(units),
    "Wind" to
        (this?.wind?.getWindSpeedWithDirection(units, windDirectionUnits) ?: "--"),
    "Max Wind Gusts" to
        (this?.wind?.getWindGusts(units) ?: "--"),
    "UV Index" to
        (this?.uvIndex?.toString() ?: "Moderate"),
    "Humidity" to
        getFormattedHumidity(),
    "Surface Pressure" to getFormattedSurfacePressure(units),
    "Sea Level Pressure" to
        getFormattedSeaLevelPressure(units),
    "Cloud Cover" to
        getFormattedCloudCover(),
)
