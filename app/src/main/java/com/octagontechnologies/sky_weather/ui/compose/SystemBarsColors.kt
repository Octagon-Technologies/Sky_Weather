package com.octagontechnologies.sky_weather.ui.compose

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Callbacks for screens to request dynamic system bar background colors.
 * The root layout draws these colors in the status/navigation bar inset areas
 * instead of using deprecated [Window.statusBarColor] and [Window.navigationBarColor] APIs.
 */
data class SystemBarsColorOverrides(
    val setStatusBarColor: (Color?) -> Unit,
    val setNavigationBarColor: (Color?) -> Unit,
)

/**
 * Composition local providing callbacks to override system bar colors from any screen.
 * Set to null when not provided (e.g. in previews).
 */
val LocalSystemBarsColorOverrides = compositionLocalOf<SystemBarsColorOverrides?> { null }
