package com.octagontechnologies.sky_weather.utils.weather

import com.octagontechnologies.sky_weather.domain.SingleForecast
import com.octagontechnologies.sky_weather.domain.getBasicFeelsLike
import com.octagontechnologies.sky_weather.domain.getFormattedCloudCover
import com.octagontechnologies.sky_weather.domain.getFormattedHumidity
import com.octagontechnologies.sky_weather.domain.getFormattedSeaLevelPressure
import com.octagontechnologies.sky_weather.domain.getFormattedSnowDepth
import com.octagontechnologies.sky_weather.domain.getFormattedSoilMoisture
import com.octagontechnologies.sky_weather.domain.getFormattedSurfacePressure
import com.octagontechnologies.sky_weather.domain.getFormattedTemp
import com.octagontechnologies.sky_weather.domain.getFormattedTerrestrialRad
import com.octagontechnologies.sky_weather.domain.getFormattedVisibility
import com.octagontechnologies.sky_weather.utils.Units
import com.octagontechnologies.sky_weather.utils.WindDirectionUnits


fun SingleForecast?.getCoreWeatherConditions(
    units: Units?,
    windDirectionUnits: WindDirectionUnits?
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
            getFormattedHumidity()
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
                getFormattedSnowDepth()
    )
