package com.octagon_technologies.sky_weather

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val cardinalDirection = when((8.31 / 22.5).toInt()) {
            1 -> "N"
            2 -> "NNE"
            3 -> "NE"
            4 -> "ENE"
            5 -> "E"
            6 -> "ESE"
            7 -> "SE"
            8 -> "SSE"
            9 -> "S"
            10 -> "SSW"
            11 -> "SW"
            12 -> "WSW"
            13 -> "W"
            14 -> "WNW"
            15 -> "NW"
            16 -> "NNW"
            17 -> "N"
            else -> throw IndexOutOfBoundsException("mainWind.windDirection.value.toInt() is ${(8.31 / 22.5).toInt()}")
        }
    }
}