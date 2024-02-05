package com.octagon_technologies.sky_weather.utils

import org.joda.time.Instant
import java.text.SimpleDateFormat
import java.util.*

fun getFirstLetterOfDay(stringTime: String?) =
    if (stringTime != null)
        SimpleDateFormat("EEEE", Locale.getDefault()).format(Instant.parse(stringTime))
            .first().toString()
    else "-"

fun String?.getDayOfWeek() =
    if (this != null)
        SimpleDateFormat("DD", Locale.getDefault()).format(Instant.parse(this))
            .toString()
    else "--"

fun getDayOfMonth(stringTime: String?) =
    if (stringTime != null)
        SimpleDateFormat("dd", Locale.getDefault()).format(Instant.parse(stringTime))
            .toString()
    else "--"

fun getFullMonth(stringTime: String?) =
    if (stringTime != null)
        SimpleDateFormat("MMMM", Locale.getDefault()).format(Instant.parse(stringTime))
            .first().toString()
    else
        "-----"

fun Long.getHoursOfDay() =
    Instant.ofEpochMilli(this).toDateTime().hourOfDay


fun String?.getDayWithMonth(): String =
    if (this != null)
        SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault())
            .format(Instant.parse(this).toDate())
    else
        "-----, ----"

fun Long?.getHoursAndMinsWithDay(timeFormat: TimeFormat?): String =
    if (this != null)
        SimpleDateFormat(
            "${if (timeFormat == TimeFormat.FULL_DAY) "HH" else "hh"}:mm, EEEE",
            Locale.getDefault()
        ).format(Date(this))
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
    val date = Instant.ofEpochSecond(this).toDateTime()
    val hoursOfDay = date.hourOfDay
    val minuteOfHour = date.minuteOfHour()

    return if (timeFormat == TimeFormat.HALF_DAY) {
        if (hoursOfDay <= 11)
            "$hoursOfDay:$minuteOfHour am"
        else
            "${hoursOfDay - 12}:$minuteOfHour pm"
    } else
        "$hoursOfDay:$minuteOfHour"
}

fun Long.toLunarDateFormat(): String =
    SimpleDateFormat(
        "yyyyMMdd",
        Locale.getDefault()
    ).format(this)

fun Long.toLunarTimeZone() =
    (TimeZone.getDefault().getOffset(Date().time).toDouble() / 3_600_000.0).toInt()
        .toString()