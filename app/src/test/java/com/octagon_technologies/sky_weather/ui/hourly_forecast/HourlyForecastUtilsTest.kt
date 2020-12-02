package com.octagon_technologies.sky_weather.ui.hourly_forecast

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.junit.Test

class HourlyForecastUtilsTest {

    @Test
    fun getDayOfWeek_withTuesdayAsIso_returnsTuesday() {
        val timeInIso = ISODateTimeFormat.dateTime().print(DateTime())
        val dayOfWeek = getDayOfWeek(timeInIso)
        println(dayOfWeek)

        assertThat(dayOfWeek, `is`("Tuesday"))
    }
}