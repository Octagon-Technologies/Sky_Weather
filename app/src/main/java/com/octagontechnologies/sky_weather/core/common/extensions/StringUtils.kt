package com.octagontechnologies.sky_weather.core.common.extensions

import java.util.Locale

fun String.capitalize() = replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
