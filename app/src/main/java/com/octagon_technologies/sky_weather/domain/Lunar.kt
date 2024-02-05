package com.octagon_technologies.sky_weather.domain

import com.octagon_technologies.sky_weather.utils.TimeFormat
import com.octagon_technologies.sky_weather.utils.getHoursAndMins
import timber.log.Timber

data class Lunar(
    val sunRise: String?,
    val sunSet: String?,
    val moonRise: String?,
    val moonSet: String?
) {

    val sunHrsOnly = getFinalHours(sunRise, sunSet)
    val sunMinsOnly = getFinalMinutes(sunRise, sunSet)

    val moonHrsOnly = getFinalHours(moonRise, moonSet)
    val moonMinsOnly = getFinalMinutes(moonRise, moonSet)

    fun getSunRiseDisplay(timeFormat: TimeFormat?) = sunRise?.getHoursAndMins(timeFormat)
    fun getSunSetDisplay(timeFormat: TimeFormat?) = sunSet?.getHoursAndMins(timeFormat)

    fun getMoonRiseDisplay(timeFormat: TimeFormat?) = moonRise?.getHoursAndMins(timeFormat)
    fun getMoonSetDisplay(timeFormat: TimeFormat?) = moonSet?.getHoursAndMins(timeFormat)

    /*
TODO: Test these function extensively to eliminate odd numbers e.g 36 hours of sunlight
 */
    fun getFinalHours(rise: String?, set: String?): String {
        if (rise == null || set == null) return "--"
        val riseList = rise.split(":").map { it.toInt() }
        val setList = set.split(":").map { it.toInt() }

        return (setList[0] - riseList[0]).let {
            if (it >= 0) it.toString() else (it + 24).toString()
        }
    }

    fun getFinalMinutes(rise: String?, set: String?): String {
        if (rise == null || set == null) return "--"
        val riseList = rise.split(":").map { it.toInt() }
        val setList = set.split(":").map { it.toInt() }

        val diff =
            (setList[0].toMinutesFromHours() + setList[1]) - (riseList[0].toMinutesFromHours() + riseList[1])
        Timber.d("Minutes diff is $diff with rise as $rise and set as $set")
        return (if (diff >= 0) diff else (diff + 2400)).toHoursAndMinutes().second.toString()
    }

    fun Int.toMinutesFromHours() = (this * 60)
    // Returns minutes and seconds respectively
    fun Int.toHoursAndMinutes(): Pair<Int, Int> = Pair((this / 60), (this % 60))
}
