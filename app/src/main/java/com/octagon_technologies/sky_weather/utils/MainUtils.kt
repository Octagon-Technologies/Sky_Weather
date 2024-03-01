package com.octagon_technologies.sky_weather.utils

import android.os.Build.VERSION_CODES.M
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.RequiresApi
import com.octagon_technologies.sky_weather.domain.SingleForecast
import com.octagon_technologies.sky_weather.domain.getBasicFeelsLike
import com.octagon_technologies.sky_weather.domain.getFormattedCloudCover
import com.octagon_technologies.sky_weather.domain.getFormattedHumidity
import com.octagon_technologies.sky_weather.domain.getFormattedSeaLevelPressure
import com.octagon_technologies.sky_weather.domain.getFormattedSnowDepth
import com.octagon_technologies.sky_weather.domain.getFormattedSoilMoisture
import com.octagon_technologies.sky_weather.domain.getFormattedSurfacePressure
import com.octagon_technologies.sky_weather.domain.getFormattedTemp
import com.octagon_technologies.sky_weather.domain.getFormattedTerrestrialRad
import com.octagon_technologies.sky_weather.domain.getFormattedVisibility
import com.octagon_technologies.sky_weather.models.EachWeatherDescription
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(M)
const val darkStatusIcons = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

@RequiresApi(M)
const val whiteStatusIcons = 0

enum class Units {
    IMPERIAL,
    METRIC;

    fun getUrlParameter(): String = if (this == IMPERIAL) "imperial" else "metric"
    fun getUnitSymbol(): String = if (this == IMPERIAL) "F" else "C"
}

fun Units?.isImperial() = this == Units.IMPERIAL

enum class WindDirectionUnits { CARDINAL, DEGREES }
enum class TimeFormat { HALF_DAY, FULL_DAY }
enum class Theme { LIGHT, DARK }

enum class StatusCode { Success, NoNetwork, ApiLimitExceeded }

data class EachDataStoreItem(val preferencesName: String, val newValue: Any)

fun Any?.isNull() = this == null

fun TimeFormat?.getAmOrPmBasedOnTime(hourIn24HourSystem: Int, date: Date): String =
    if (this == TimeFormat.HALF_DAY)
        SimpleDateFormat("hh:mm ", Locale.getDefault()).format(date) +
                (if (hourIn24HourSystem <= 11) "am " else "pm ")
    else SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)


interface CustomTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {}
}

fun getBasicForecastConditions(
    singleForecast: SingleForecast?,
    units: Units?,
    windDirectionUnits: WindDirectionUnits?
): ArrayList<EachWeatherDescription> {
    val arrayOfWeatherDescriptions = ArrayList<EachWeatherDescription>()
    arrayOfWeatherDescriptions.add(
        EachWeatherDescription(
            "Temperature",
            singleForecast.getFormattedTemp(units.isImperial())
        )
    )
    arrayOfWeatherDescriptions.add(
        EachWeatherDescription(
            "FeelsLike Temperature",
            singleForecast.getBasicFeelsLike(units.isImperial())
        )
    )
    arrayOfWeatherDescriptions.add(
        EachWeatherDescription(
            "Rain Probability",
            "${(singleForecast?.weatherCode?.rainProbability?.toInt() ?: 0)}%"
        )
    )
    arrayOfWeatherDescriptions.add(
        EachWeatherDescription(
            "Wind",
            singleForecast?.wind?.getWindSpeedWithDirection(units, windDirectionUnits) ?: "--"
        )
    )
    arrayOfWeatherDescriptions.add(
        EachWeatherDescription(
            "Max Wind Gusts",
            singleForecast?.wind?.getWindGusts(units) ?: "--"
        )
    )
    arrayOfWeatherDescriptions.add(
        EachWeatherDescription(
            "UV Index",
            singleForecast?.uvIndex?.toString() ?: "Moderate"
        )
    )
    arrayOfWeatherDescriptions.add(
        EachWeatherDescription(
            "Humidity",
            singleForecast?.getFormattedHumidity() ?: "--%"
        )
    )

    return arrayOfWeatherDescriptions
}


fun getAdvancedForecastDescription(
    singleForecast: SingleForecast?,
    units: Units?,
    windDirectionUnits: WindDirectionUnits?
): ArrayList<EachWeatherDescription> {
    val arrayOfWeatherDescriptions = ArrayList<EachWeatherDescription>()
    singleForecast?.let {
        arrayOfWeatherDescriptions.addAll(getBasicForecastConditions(it, units, windDirectionUnits))

        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "Dew Point",
                "${singleForecast.dewPoint?.toInt() ?: "--"}°"
            )
        )
        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "Surface Pressure",
                singleForecast.getFormattedSurfacePressure(units == Units.IMPERIAL)
            )
        )
        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "Sea Level Pressure",
                singleForecast.getFormattedSeaLevelPressure(units == Units.IMPERIAL)
            )
        )
        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "Cloud Cover",
                singleForecast.getFormattedCloudCover()
            )
        )
        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "Visibility",
                singleForecast.getFormattedVisibility()
            )
        )
        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "Terrestrial Radiation",
                singleForecast.getFormattedTerrestrialRad()
            )
        )
        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "Soil Moisture",
                singleForecast.getFormattedSoilMoisture()
            )
        )
        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "Snow Depth",
                singleForecast.getFormattedSnowDepth()
            )
        )
    }

    return arrayOfWeatherDescriptions
}
