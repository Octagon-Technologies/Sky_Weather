package com.octagon_technologies.sky_weather.utils

import android.os.Build.VERSION_CODES.M
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.RequiresApi
import com.octagon_technologies.sky_weather.models.EachWeatherDescription
import com.octagon_technologies.sky_weather.models.MainWind
import com.octagon_technologies.sky_weather.repository.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.repository.network.single_forecast.SingleForecast
import com.octagon_technologies.sky_weather.ui.current_forecast.*
import com.octagon_technologies.sky_weather.models.Coordinates
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

// TODO - Needs lots of cleanups (housekeeping)
// group similar extension funcs together
// remove enums and data classes here
// Like Odari said - don't mix things together (group them accordingly) - easy to find

@RequiresApi(M)
const val darkStatusIcons = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
@RequiresApi(M)
const val whiteStatusIcons = 0

enum class Units {
    IMPERIAL {
        override var value = "us"
    },
    METRIC {
        override var value = "si"
    };

    open lateinit var value: String
}

enum class WindDirectionUnits { CARDINAL, DEGREES }
enum class TimeFormat { HALF_DAY, FULL_DAY }
enum class Theme { LIGHT, DARK }

enum class StatusCode { Success, NoNetwork, ApiLimitExceeded }

data class EachDataStoreItem(val preferencesName: String, val newValue: Any)

// TODO - Its already there in Kotlin
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
    windDirectionUnits: WindDirectionUnits?
): ArrayList<EachWeatherDescription> {
    val arrayOfWeatherDescriptions = ArrayList<EachWeatherDescription>()
    singleForecast?.let {
        val temp = it.temp
        val feelsLike = it.feelsLike
        val mainWind = MainWind(it.windDirection, it.windSpeed, it.windGust)
        val uvIndex = getUVIndex((it.surfaceShortwaveRadiation?.value ?: 50).toLong())
        Timber.d("uvIndex is $uvIndex")

        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "Temperature",
                "${temp?.value?.toInt()}°"
            )
        )
        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "FeelsLike Temperature",
                "${feelsLike?.value?.toInt()}°"
            )
        )
        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "Wind",
                getActualWind(mainWind, windDirectionUnits) ?: "--"
            )
        )
        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "Max Wind Gusts",
                with(mainWind.windGust) {
                    if (this?.units == "m/s")
                        getWindSpeedInKm((value ?: 4).toLong())
                    else
                        "${this?.value?.toInt() ?: "--"} mph"
                }
            )
        )
        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "UV Index",
                "${uvIndex.uvIndex} ${uvIndex.uvLevelString}"
            )
        )
        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "Humidity",
                "${it.humidity?.value?.toInt()}%"
            )
        )
    }

    return arrayOfWeatherDescriptions
}

fun getAdvancedForecastDescription(
    singleForecast: SingleForecast?,
    units: Units?,
    windDirectionUnits: WindDirectionUnits?
): ArrayList<EachWeatherDescription> {
    val arrayOfWeatherDescriptions = ArrayList<EachWeatherDescription>()
    singleForecast?.let {
        arrayOfWeatherDescriptions.addAll(getBasicForecastConditions(it, windDirectionUnits))

        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "Dew Point",
                "${it.dewPoint?.value?.toInt() ?: 0}°"
            )
        )
        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "Pressure",
                "${it.baroPressure?.value?.toInt() ?: 0} ${if (units == Units.METRIC) "mbar" else "inHg"}"
            )
        )
        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "Cloud Cover",
                "${it.cloudCover?.value?.toInt() ?: 0}%"
            )
        )
        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "Visibility",
                "${it.visibility?.value?.toInt()} ${it.visibility?.units ?: "--"}"
            )
        )
        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "Cloud Ceiling",
                if (it.cloudCeiling?.value == null) "None" else "${it.cloudCeiling.value.toInt()} m"
            )
        )
        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "Moon Phase",
                (it.moonPhase?.value ?: "--").capitalizeWordsWithUnderscore()
            )
        )
    }

    return arrayOfWeatherDescriptions
}
