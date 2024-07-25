package com.octagontechnologies.sky_weather.ui.compose.theme

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val lightAppColors = AppColors(
    isDarkTheme = false,

    background = LightBlue,
    onBackground = Color.White,

    surface = Color.White,
    onSurface = DarkBlack,
    onSurfaceLighter = LightBlack
)

val darkAppColors = AppColors(
    isDarkTheme = true,

    background = DarkBlue,
    onBackground = Color.White,

    surface = DarkBlack,
    onSurface = Color.White,
    onSurfaceLighter = LightWhite
)

val LocalAppColors: ProvidableCompositionLocal<AppColors> = staticCompositionLocalOf { lightAppColors }

data class AppColors (
    val isDarkTheme: Boolean,

    val background: Color,
    val onBackground: Color,


    val surface: Color,

    // Text Color to be used on a black/white background (main)
    val onSurface: Color,

    // Lighter Text Color to be used on a black/white background (description text)
    val onSurfaceLighter: Color
)