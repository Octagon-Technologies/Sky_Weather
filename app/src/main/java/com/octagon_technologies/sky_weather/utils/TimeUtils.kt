package com.octagon_technologies.sky_weather.utils

import org.joda.time.DateTimeFieldType
import org.joda.time.Instant
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

fun getFirstLetterOfDay(stringTime: String?) =
    if (stringTime != null)
        Instant.parse(stringTime).toDateTime().dayOfWeek().asShortText.first().toString()
    else "-"

fun String?.getDayOfWeek() =
    if (this != null)
        SimpleDateFormat("DD", Locale.getDefault()).format(Instant.parse(this))
            .toString()
    else "--"

fun getDayOfMonth(stringTime: String?) =
    if (stringTime != null)
        Instant.parse(stringTime).toDateTime().dayOfMonth.toString()
    else "--"

fun getFullMonth(stringTime: String?): String {
    Timber.d("stringTime in getFullMonth() is $stringTime")
    return if (stringTime != null)
        Instant.parse(stringTime).toDateTime().monthOfYear().asText
    else
        "-----"
}

fun Long.getHoursOfDay() =
    Instant.ofEpochMilli(this).toDateTime().hourOfDay


fun String?.getDayWithMonth(): String =
    if (this != null)
        SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault())
            .format(Instant.parse(this).toDate())
    else
        "-----, ----"

fun Long?.getHoursAndMinsWithDay(timeFormat: TimeFormat?): String =
    if (this != null) {
        val hourAndMinOnly = this.getHoursAndMins(timeFormat)
        val day = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date(this))

        "$hourAndMinOnly, $day"
    }
    else
        "--:--, ------"

/*
Returns time in AM/PM or 24-hour format e.g 4:00 pm or 16:00 based on TimeFormat
 */
fun String.getHoursAndMins(timeFormat: TimeFormat?): String {
    val hourIn24System = split(":")[0].toInt()
    val minutes = split(":")[1]

    return if (timeFormat == TimeFormat.HALF_DAY) {
        if (hourIn24System <= 11)
            "$this:$minutes am"
        else
            "${hourIn24System - 12}:$minutes pm"
    } else this
}

fun Long.getHoursAndMins(timeFormat: TimeFormat?): String {
    val hourOfDay = with(Calendar.getInstance()) {
        timeInMillis = this@getHoursAndMins
        get(Calendar.HOUR_OF_DAY)
    }

    val rawTime = SimpleDateFormat(
        "${if (timeFormat == TimeFormat.FULL_DAY) "HH" else "hh"}:mm",
        Locale.getDefault()
    ).format(Date(this))

    return if (timeFormat == TimeFormat.HALF_DAY) "$rawTime ${if (hourOfDay < 12) "am" else "pm"}"
    else rawTime
}

fun Long.toLunarDateFormat(): String =
    SimpleDateFormat(
        "yyyyMMdd",
        Locale.getDefault()
    ).format(this)

fun Long.toLunarTimeZone() =
    (TimeZone.getDefault().getOffset(Date().time).toDouble() / 3_600_000.0).toInt()
        .toString()