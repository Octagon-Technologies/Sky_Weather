package com.octagontechnologies.sky_weather.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography =
    Typography(
        titleMedium =
            TextStyle(
                fontSize = 20.sp,
                fontFamily = QuickSand,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 24.sp,
            ),
        bodyLarge =
            TextStyle(
                fontSize = 16.sp,
                fontFamily = QuickSand,
                fontWeight = FontWeight.Medium,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
            ),
        bodyMedium =
            TextStyle(
                fontSize = 15.sp,
                fontFamily = QuickSand,
                lineHeight = 17.sp,
                fontWeight = FontWeight.Medium,
            ),
        bodySmall =
            TextStyle(
                fontSize = 14.sp,
                fontFamily = QuickSand,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Medium,
            ),
        labelLarge =
            TextStyle(
                fontSize = 16.sp,
                fontFamily = QuickSand,
                fontWeight = FontWeight.SemiBold,
            ),
        labelMedium =
            TextStyle(
                fontSize = 16.sp,
                fontFamily = QuickSand,
                fontWeight = FontWeight.SemiBold,
            ),
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
     */
    )
