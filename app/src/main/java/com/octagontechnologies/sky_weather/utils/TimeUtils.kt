package com.octagontechnologies.sky_weather.utils

import android.annotation.SuppressLint
import org.joda.time.DateTime
import org.joda.time.DateTimeFieldType
import org.joda.time.Instant
import org.joda.time.format.DateTimeFormat
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

fun Long?.getFirstLetterOfDay() =
    if (this != null)
        Instant.ofEpochMilli(this).toDateTime().dayOfWeek().asShortText.first().toString()
    else "-"

fun Long?.getDayOfWeek() =
    if (this != null)
        SimpleDateFormat("DD", Locale.getDefault()).format(Instant.ofEpochMilli(this))
            .toString()
    else "--"

fun Long?.getDayOfMonth() =
    if (this != null)
        Instant.ofEpochMilli(this).toDateTime().dayOfMonth.toString()
    else "--"

fun Long?.getFullMonth(): String {
    return if (this != null)
        Instant.ofEpochMilli(this).toDateTime().monthOfYear().asText
    else
        "-----"
}

fun Long.getHoursOfDay() =
    Instant.ofEpochMilli(this).toDateTime().hourOfDay


fun Long?.getDayWithMonth(): String =
    if (this != null)
        SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault())
            .format(Instant.ofEpochMilli(this).toDate())
    else
        "-----, ----"

fun Long?.getHoursAndMinsWithDay(timeFormat: TimeFormat?): String =
    if (this != null) {
        val date = DateTime(this).toLocalDate().toDate()
        val hourAndMinOnly = this.getHoursAndMins(timeFormat)
        val day = SimpleDateFormat("EEEE", Locale.getDefault()).format(date)

        "$hourAndMinOnly, $day"
    }
    else
        "--:--, ------"

@SuppressLint("SimpleDateFormat")
fun Long.getHoursAndMins(timeFormat: TimeFormat?): String {
    val isHalfDay = timeFormat == TimeFormat.HALF_DAY
    val halfDayPattern = "hh:mm a"
    val builder = SimpleDateFormat(if (isHalfDay) halfDayPattern else "HH:mm")

//    val date = DateTime(this).toLocalDate().toDate()
    val date = Instant.ofEpochMilli(this).toDate()
    return builder.format(date)
}

fun Long.toLunarDateFormat(): String =
    SimpleDateFormat(
        "yyyyMMdd",
        Locale.getDefault()
    ).format(this)

fun Long.toLunarTimeZone() =
    (TimeZone.getDefault().getOffset(Date().time).toDouble() / 3_600_000.0).toInt()
        .toString()