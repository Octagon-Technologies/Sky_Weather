package com.octagon_technologies.sky_weather.utils

import java.util.*

fun String.capitalize() = replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }