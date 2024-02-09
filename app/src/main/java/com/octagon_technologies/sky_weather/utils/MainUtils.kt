package com.octagon_technologies.sky_weather.utils

import android.os.Build.VERSION_CODES.M
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.RequiresApi
import com.octagon_technologies.sky_weather.domain.SingleForecast
import com.octagon_technologies.sky_weather.domain.getFormattedCloudCeiling
import com.octagon_technologies.sky_weather.domain.getFormattedCloudCover
import com.octagon_technologies.sky_weather.domain.getFormattedFeelsLike
import com.octagon_technologies.sky_weather.domain.getFormattedHumidity
import com.octagon_technologies.sky_weather.domain.getFormattedTemp
import com.octagon_technologies.sky_weather.models.Coordinates
import com.octagon_technologies.sky_weather.models.EachWeatherDescription
import com.octagon_technologies.sky_weather.repository.network.location.reverse_geocoding_location.ReverseGeoCodingLocation
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
}

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

fun String.capitalizeWordsWithUnderscore(): String {
    return this.split("_").joinToString(" ") { it.capitalize(Locale.getDefault()) }
}

fun ReverseGeoCodingLocation?.getDisplayLocation(): String {
    if (this.isNull()) return "--"
    return if (
        this?.reverseGeoCodingAddress?.suburb.isNull() ||
        this?.reverseGeoCodingAddress?.city.isNull()
    ) "${
        this?.reverseGeoCodingAddress?.let {
            when {
                it.suburb != null -> it.suburb
                it.city != null -> it.city
                displayName != null -> displayName.split(",")[0].capitalize(Locale.getDefault())
                else -> "--"
            }
        } ?: "--"
    }, ${this?.reverseGeoCodingAddress?.countryCode?.toUpperCase(Locale.getDefault()) ?: "--"}"
    else "${this?.reverseGeoCodingAddress?.suburb}, ${this?.reverseGeoCodingAddress?.city}"
}

fun ReverseGeoCodingLocation?.getCoordinates(): Coordinates? =
    try {
        Coordinates(
            this?.lon?.toDouble()!!,
            this.lat?.toDouble()!!
        )
    } catch (npe: NullPointerException) {
        null
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
            singleForecast.getFormattedTemp()
        )
    )
    arrayOfWeatherDescriptions.add(
        EachWeatherDescription(
            "FeelsLike Temperature",
            singleForecast.getFormattedFeelsLike()
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
            singleForecast?.wind?.getWindSpeed(units) ?: "--"
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
                "Pressure",
                "${singleForecast.pressure?.toInt() ?: "-- "} ${if (units == Units.METRIC) "mbar" else "inHg"}"
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
                "${singleForecast.visibility?.toInt() ?: "-- "}%"
            )
        )
        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "Cloud Ceiling",
                singleForecast.getFormattedCloudCeiling()
            )
        )
    }

    return arrayOfWeatherDescriptions
}
