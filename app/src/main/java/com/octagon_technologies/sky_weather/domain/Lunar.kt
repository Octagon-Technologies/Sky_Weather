package com.octagon_technologies.sky_weather.domain

import com.octagon_technologies.sky_weather.utils.TimeFormat
import org.joda.time.Instant
import org.joda.time.Interval
import timber.log.Timber

//data class Lunar(
//    val sunRise: String?,
//    val sunSet: String?,
//    val moonRise: String?,
//    val moonSet: String?
//) {
data class Lunar(
    val sunRise: String?,
    val sunSet: String?,
    val moonRise: String?,
    val moonSet: String?
) {

    fun getSunRiseDisplay(timeFormat: TimeFormat?) = sunRise?.getHoursAndMins(timeFormat)
    fun getSunSetDisplay(timeFormat: TimeFormat?) = sunSet?.getHoursAndMins(timeFormat)

    fun getMoonRiseDisplay(timeFormat: TimeFormat?) = moonRise?.getHoursAndMins(timeFormat)
    fun getMoonSetDisplay(timeFormat: TimeFormat?) = moonSet?.getHoursAndMins(timeFormat)


    private fun String?.getHoursAndMins(timeFormat: TimeFormat?): String {
        if (this == null)
            return "--:--"

        val instant = Instant.parse(this).toDateTime()
        val time = instant.toString(
            if (timeFormat == TimeFormat.FULL_DAY) "HH:mm"
            else "hh:mm a"
        )
        Timber.d("time in getHoursAndMins is $time")
        return time
    }

    fun getSunHoursFull() = getHoursForFullTimeFormat(sunRise, sunSet)
    fun getSunMinutesFull() = getMinutesForFullTimeFormat(sunRise, sunSet)
    fun getMoonHoursFull() = getHoursForFullTimeFormat(moonRise, moonSet)
    fun getMoonMinutesFull() = getMinutesForFullTimeFormat(moonRise, moonSet)

    // In the selected daily tab, we are getting the time in a string format e.g 18-01-20240300 instead of
    // the 6:43 we are used to
    private fun getHoursForFullTimeFormat(rise: String?, set: String?): String {
        if (rise == null || set == null)
            return "-- hrs"

        return try {
            val hours =
                Interval(Instant.parse(rise), Instant.parse(set)).toDuration().standardHours
            Timber.d("Hours is $hours")
            "$hours hrs"
        } catch (e: IllegalArgumentException) {
            "-- hrs"
        }
    }

    private fun getMinutesForFullTimeFormat(rise: String?, set: String?): String {
        if (rise == null || set == null)
            return "-- mins"
        return try {
            val minutes =
                Interval(Instant.parse(rise), Instant.parse(set)).toDuration().standardMinutes % 60
            Timber.d("Minutes is $minutes")
            "$minutes mins"
        } catch (e: Exception) {
            "-- mins"
        }
    }

    /*
TODO: Test these function extensively to eliminate odd numbers e.g 36 hours of sunlight
 */
}
