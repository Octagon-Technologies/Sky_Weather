package com.octagontechnologies.sky_weather.domain.daily

import com.octagontechnologies.sky_weather.domain.UVIndex
import com.octagontechnologies.sky_weather.domain.WeatherCode
import com.octagontechnologies.sky_weather.domain.Wind
import com.octagontechnologies.sky_weather.utils.TimeFormat
import com.octagontechnologies.sky_weather.utils.Units
import org.joda.time.Instant
import org.joda.time.Interval
import timber.log.Timber

data class DailyForecast(
    val timeInEpochSeconds: Long,
    val dayTime: TimePeriod,
    val nightTime: TimePeriod
) {

    fun getTempHigh(units: Units?): Int? {
        val temp = dayTime.temp?.let { dayTemp -> if (dayTemp >= nightTime.temp!!) dayTemp else nightTime.temp }?.toInt()
        return temp?.let { if (units == Units.IMPERIAL) (temp * (9 / 5) + 32) else temp }
    }
    fun getTempLow(units: Units?): Int? {
        val temp = nightTime.temp?.let { nightTemp -> if (nightTemp <= dayTime.temp!!) nightTemp else dayTime.temp }?.toInt()
        return temp?.let { if (units == Units.IMPERIAL) (temp * (9 / 5) + 32) else temp }
    }


    companion object {
        val DAY_TIME_PERIOD = TimePeriod(
            20.0,
            4,
            25.5,
            WeatherCode(56, 5),
            UVIndex.getUVIndexFromNum(5),
            Wind(20.5, 45.3, 23.5),
            760.44,
            700.2,
            45,
            true,
            rainProbability = 45,
            DailyLunar(1722312085, 1722348085 )
        )
        val NIGHT_TIME = DAY_TIME_PERIOD.copy(isDay = false, temp = 14.7)

        val TEST_FORE = DailyForecast(System.currentTimeMillis(), DAY_TIME_PERIOD, NIGHT_TIME)
    }

}


fun TimePeriod?.getFormattedTemp(units: Units?): String =
    this?.temp?.let { (if (units == Units.IMPERIAL) (temp * (9/5) + 32) else temp).toInt().toString() + "°" } ?: "--°"
fun TimePeriod?.getFormattedTemp(): String = (this?.temp?.toInt()?.toString() ?: "--") + "°"
fun TimePeriod?.getBasicFeelsLike(units: Units?): String =
    this?.feelsLike?.let { (if (units == Units.IMPERIAL) (feelsLike * (9/5) + 32) else feelsLike).toInt().toString() + "°" } ?: "--°"

fun TimePeriod?.getFormattedFeelsLike(): String =
    "FeelsLike " + (this?.feelsLike?.toInt()?.toString() ?: "--") + "°"

fun TimePeriod?.getFormattedHumidity(): String = (this?.humidity?.toInt()?.toString() ?: "--") + "%"
fun TimePeriod?.getFormattedCloudCover(): String =
    (this?.cloudCover?.toInt()?.toString() ?: "--") + "%"

fun TimePeriod?.getFormattedSurfacePressure(units: Units?) =
    (this?.surfacePressure?.toString() ?: "--") + if (units == Units.IMPERIAL) " inHg" else " mbar"

fun TimePeriod?.getFormattedSeaLevelPressure(units: Units?) =
    (this?.seaLevelPressure?.toString() ?: "--") + if (units == Units.IMPERIAL) " inHg" else " mbar"

fun TimePeriod?.getFormattedUVIndex() = (this?.uvIndex ?: UVIndex.Low).toString()

data class TimePeriod(
    val temp: Double?,
    val cloudCover: Int?,
    val feelsLike: Double?,
    val weatherCode: WeatherCode,
    val uvIndex: UVIndex,
    val wind: Wind,
    val surfacePressure: Double?,
    val seaLevelPressure: Double?,
    val humidity: Int?,
    val isDay: Boolean,
    val rainProbability: Int,
    /*
    I've added this here instead of the main DailyForecast so that the selected Daily Forecast (day and night tabs)
    can access the info
     */
    val dailyLunar: DailyLunar
)

data class DailyLunar(val rise: Long, val set: Long) {

    fun getSunRiseDisplay(timeFormat: TimeFormat?) = rise.getHoursAndMins(timeFormat)
    fun getSunSetDisplay(timeFormat: TimeFormat?) = set.getHoursAndMins(timeFormat)

    fun getMoonRiseDisplay(timeFormat: TimeFormat?) = "--:--"
    fun getMoonSetDisplay(timeFormat: TimeFormat?) = "--:--"


    private fun Long?.getHoursAndMins(timeFormat: TimeFormat?): String {
        if (this == null)
            return "--:--"

        val instant = Instant.ofEpochMilli(this).toDateTime()
        val time = instant.toString(
            if (timeFormat == TimeFormat.FULL_DAY) "HH:mm"
            else "hh:mm a"
        )
        Timber.d("time in getHoursAndMins is $time")
        return time
    }

    fun getSunHoursFull() = getHoursForFullTimeFormat(rise, set)
    fun getSunMinutesFull() = getMinutesForFullTimeFormat(rise, set)
    fun getMoonHoursFull() = getHoursForFullTimeFormat(null, null)
    fun getMoonMinutesFull() = getMinutesForFullTimeFormat(null, null)

    // In the selected daily tab, we are getting the time in a string format e.g 18-01-20240300 instead of
    // the 6:43 we are used to
    private fun getHoursForFullTimeFormat(rise: Long?, set: Long?): String {
        if (rise == null || set == null)
            return "-- hrs"

        return try {
            val hours =
                Interval(
                    Instant.ofEpochMilli(rise),
                    Instant.ofEpochMilli(set)
                ).toDuration().standardHours
            Timber.d("Hours is $hours")
            "$hours hrs"
        } catch (e: IllegalArgumentException) {
            "-- hrs"
        }
    }

    private fun getMinutesForFullTimeFormat(rise: Long?, set: Long?): String {
        if (rise == null || set == null)
            return "-- mins"
        return try {
            val minutes =
                Interval(
                    Instant.ofEpochMilli(rise),
                    Instant.ofEpochMilli(set)
                ).toDuration().standardMinutes % 60
            Timber.d("Minutes is $minutes")
            "$minutes mins"
        } catch (e: Exception) {
            "-- mins"
        }
    }


}
/*
cloudCover
cloudCeiling
dewPoint
evapoTranspiration
humidity
sunrise, sunset, moonRise, moonSet
pressure
temp
feelsLike
visibility
wind - gust, speed, direction(const)

 */