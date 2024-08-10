package com.octagontechnologies.sky_weather.domain.daily

import com.octagontechnologies.sky_weather.domain.UVIndex
import com.octagontechnologies.sky_weather.domain.WeatherCode
import com.octagontechnologies.sky_weather.domain.Wind
import com.octagontechnologies.sky_weather.utils.Units

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
            rainProbability = 45
        )
        val NIGHT_TIME = DAY_TIME_PERIOD.copy(isDay = false, temp = 14.7)

        val TEST_FORE = DailyForecast(System.currentTimeMillis(), DAY_TIME_PERIOD, NIGHT_TIME)
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