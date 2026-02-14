package com.octagontechnologies.sky_weather.feature.current.presentation.formatter

import com.octagontechnologies.sky_weather.core.model.preferences.Units
import com.octagontechnologies.sky_weather.core.model.preferences.WindDirectionUnits
import com.octagontechnologies.sky_weather.domain.model.SingleForecast
import com.octagontechnologies.sky_weather.domain.model.getBasicFeelsLike
import com.octagontechnologies.sky_weather.domain.model.getFormattedCloudCover
import com.octagontechnologies.sky_weather.domain.model.getFormattedHumidity
import com.octagontechnologies.sky_weather.domain.model.getFormattedSeaLevelPressure
import com.octagontechnologies.sky_weather.domain.model.getFormattedSnowDepth
import com.octagontechnologies.sky_weather.domain.model.getFormattedSoilMoisture
import com.octagontechnologies.sky_weather.domain.model.getFormattedSurfacePressure
import com.octagontechnologies.sky_weather.domain.model.getFormattedTemp
import com.octagontechnologies.sky_weather.domain.model.getFormattedTerrestrialRad
import com.octagontechnologies.sky_weather.domain.model.getFormattedVisibility

fun SingleForecast?.getCoreWeatherConditions(
    units: Units?,
    windDirectionUnits: WindDirectionUnits?,
) = mapOf(
    "Temperature" to getFormattedTemp(units),
    "FeelsLike Temperature" to
        getBasicFeelsLike(units),
    "Rain Probability" to
        "${(this?.weatherCode?.rainProbability ?: 0)}%",
    "Wind" to
        (this?.wind?.getWindSpeedWithDirection(units, windDirectionUnits) ?: "--"),
    "Max Wind Gusts" to
        (this?.wind?.getWindGusts(units) ?: "--"),
    "UV Index" to
        (this?.uvIndex?.toString() ?: "Moderate"),
    "Humidity" to
        getFormattedHumidity(),
)

fun SingleForecast?.getAdvancedWeatherConditions(units: Units?) =
    mapOf(
        "Dew Point" to "${this?.dewPoint?.toInt() ?: "--"}°",
        "Surface Pressure" to getFormattedSurfacePressure(units == Units.IMPERIAL),
        "Sea Level Pressure" to
            getFormattedSeaLevelPressure(units == Units.IMPERIAL),
        "Cloud Cover" to
            getFormattedCloudCover(),
        "Visibility" to
            getFormattedVisibility(),
        "Terrestrial Radiation" to
            getFormattedTerrestrialRad(),
        "Soil Moisture" to
            getFormattedSoilMoisture(),
        "Snow Depth" to
            getFormattedSnowDepth(),
    )
