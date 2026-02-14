package com.octagontechnologies.sky_weather.core.designsystem.components

import androidx.annotation.ColorInt

fun shouldUseDarkIcons(
    @ColorInt color: Int,
): Boolean {
    val r = (color shr 16) and 0xFF
    val g = (color shr 8) and 0xFF
    val b = (color) and 0xFF
    // perceived brightness
    val brightness = (0.299 * r + 0.587 * g + 0.114 * b)
    return brightness > 186
}
