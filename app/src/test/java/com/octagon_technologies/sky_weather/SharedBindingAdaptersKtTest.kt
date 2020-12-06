package com.octagon_technologies.sky_weather

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class SharedBindingAdaptersKtTest {

    @Test
    fun changeLunarTimeTest() {
        val list = listOf(
            Triple("23:00", TimeFormat.HALF_DAY, "11:00 pm"),
            Triple("11:34", TimeFormat.HALF_DAY, "11:34 am"),
            Triple("4:23", TimeFormat.FULL_DAY, "4:23"))

         list.forEach {
             val formattedTime = it.first.changeLunarTime(it.second)
             assertThat(formattedTime, `is`(it.third))
         }
    }
}