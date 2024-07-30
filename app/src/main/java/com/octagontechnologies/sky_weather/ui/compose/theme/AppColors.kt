package com.octagontechnologies.sky_weather.ui.compose.theme

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val lightAppColors = AppColors(
    isDarkTheme = false,

    background = LightBlue,
    onBackground = Color.White,

    surface = Color.White,
    surfaceSmallerVariant = AlmostWhite,
    surfaceVariant = LightWhite,

    onSurface = DarkBlack,
    onSurfaceLighter = LightBlack
)

val darkAppColors = AppColors(
    isDarkTheme = true,

    background = DarkBlue,
    onBackground = Color.White,

    surface = DarkBlack,
    surfaceSmallerVariant = AlmostDarkBlack,
    surfaceVariant = LightBlack,

    onSurface = Color.White,
    onSurfaceLighter = LightWhite
)

val LocalAppColors: ProvidableCompositionLocal<AppColors> = staticCompositionLocalOf { lightAppColors }

data class AppColors (
    val isDarkTheme: Boolean,

    val background: Color,
    val onBackground: Color,


    val surface: Color,
    val surfaceSmallerVariant: Color,
    val surfaceVariant: Color,

    // Text Color to be used on a black/white background (main)
    val onSurface: Color,

    // Lighter Text Color to be used on a black/white background (description text)
    val onSurfaceLighter: Color
)