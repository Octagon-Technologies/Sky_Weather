package com.octagontechnologies.sky_weather.utils.weather

import com.octagontechnologies.sky_weather.domain.daily.TimePeriod
import com.octagontechnologies.sky_weather.domain.daily.getBasicFeelsLike
import com.octagontechnologies.sky_weather.domain.daily.getFormattedCloudCover
import com.octagontechnologies.sky_weather.domain.daily.getFormattedHumidity
import com.octagontechnologies.sky_weather.domain.daily.getFormattedSeaLevelPressure
import com.octagontechnologies.sky_weather.domain.daily.getFormattedSurfacePressure
import com.octagontechnologies.sky_weather.domain.daily.getFormattedTemp
import com.octagontechnologies.sky_weather.utils.Units
import com.octagontechnologies.sky_weather.utils.WindDirectionUnits


fun TimePeriod?.getWeatherConditions(
    units: Units?,
    windDirectionUnits: WindDirectionUnits?
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