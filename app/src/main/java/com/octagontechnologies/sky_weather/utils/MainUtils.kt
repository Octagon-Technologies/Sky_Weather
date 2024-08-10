package com.octagontechnologies.sky_weather.utils

import java.text.SimpleDateFormat
import java.util.*

enum class Units {
    IMPERIAL,
    METRIC;

    companion object { fun getDefault() = METRIC }

    fun getUrlParameter(): String = if (this == IMPERIAL) "imperial" else "metric"
    fun getUnitSymbol(): String = if (this == IMPERIAL) "F" else "C"
}

enum class WindDirectionUnits { CARDINAL, DEGREES; companion object { fun getDefault() = CARDINAL } }
enum class TimeFormat { HALF_DAY, FULL_DAY; companion object { fun getDefault() = HALF_DAY } }
enum class Theme { LIGHT, DARK, BLACK; companion object { fun getDefault() = DARK } }

enum class StatusCode { Success, NoNetwork, ApiLimitExceeded }


fun Any?.isNull() = this == null

fun TimeFormat?.getAmOrPmBasedOnTime(hourIn24HourSystem: Int, date: Date): String =
    if (this == TimeFormat.HALF_DAY)
        SimpleDateFormat("hh:mm ", Locale.getDefault()).format(date) +
                (if (hourIn24HourSystem <= 11) "am " else "pm ")
    else SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
