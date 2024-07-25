package com.octagontechnologies.sky_weather.utils

fun <T> List<T>.isLastIndex(item: T): Boolean =
    lastIndex == indexOf(item)