package com.octagon_technologies.sky_weather

import android.os.Build
import android.os.Build.VERSION_CODES.M
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.octagon_technologies.sky_weather.repository.network.mockLat
import com.octagon_technologies.sky_weather.repository.network.mockLon
import com.octagon_technologies.sky_weather.repository.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.repository.network.single_forecast.SingleForecast
import com.octagon_technologies.sky_weather.ui.current_forecast.*
import com.octagon_technologies.sky_weather.ui.find_location.Coordinates
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

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

fun Int.checkBuildVersion() = Build.VERSION.SDK_INT >= this

private fun Fragment.showToast(message: String, length: Int) {
    Toast.makeText(requireContext(), message, length).show()
}

fun Fragment.showLongToast(message: String) = showToast(message, Toast.LENGTH_LONG)
fun Fragment.showShortToast(message: String) = showToast(message, Toast.LENGTH_SHORT)

fun Fragment.getStringResource(@StringRes stringRes: Int) = resources.getString(stringRes)

fun Fragment.removeToolbarAndBottomNav(statusBarColor: Int = R.color.line_grey) {
    val mainActivity = (this.activity as MainActivity)
    val gone = View.GONE

    mainActivity.binding.apply {
        navView.visibility = gone
        topToolbarConstraint.visibility = gone
        topLineDivider.visibility = gone
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        mainActivity.apply {
            if (Build.VERSION.SDK_INT >= M) {
                window.decorView.systemUiVisibility =
                    if (statusBarColor in listOf(
                            R.color.color_black,
                            R.color.light_theme_blue,
                            R.color.dark_theme_blue,
                            R.color.dark_black
                        )
                    ) whiteStatusIcons else darkStatusIcons
            }
            window.statusBarColor =
                ResourcesCompat.getColor(resources, statusBarColor, null)
        }
    }
}


fun Fragment.addToolbarAndBottomNav(theme: Theme?, includeBottomNavView: Boolean = true) =
    (activity as MainActivity).addToolbarAndBottomNav(theme, includeBottomNavView)


fun MainActivity.addToolbarAndBottomNav(theme: Theme?, includeBottomNavView: Boolean = true) {
    val visible = View.VISIBLE
    val defaultColor =
        if (theme == Theme.LIGHT) R.color.current_forecast_night_time else R.color.dark_theme_blue
    Timber.d("Theme is $theme")

    binding.apply {
        if (includeBottomNavView) navView.visibility = visible
        topToolbarConstraint.visibility = visible
        topLineDivider.visibility = visible
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        if (Build.VERSION.SDK_INT >= M) {
            window.decorView.systemUiVisibility = whiteStatusIcons
        }
        window.statusBarColor =
            ResourcesCompat.getColor(resources, defaultColor, null)
    }
}

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
    return "${
        this?.reverseGeoCodingAddress?.let {
            when {
                it.suburb != null -> it.suburb
                it.city != null -> it.city
                displayName != null -> displayName.split(",")[0].capitalize(Locale.getDefault())
                else -> "--"
            }
        } ?: "--"
    }, ${this?.reverseGeoCodingAddress?.countryCode?.toUpperCase(Locale.getDefault()) ?: "--"}"
}

fun ReverseGeoCodingLocation?.getCoordinates(): Coordinates = Coordinates(
    this?.lon?.toDouble() ?: mockLon,
    this?.lat?.toDouble() ?: mockLat
)


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
