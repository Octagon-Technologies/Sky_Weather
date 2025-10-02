package com.octagontechnologies.sky_weather.ui.compose.theme

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider


private val AppRippleConfiguration = RippleConfiguration(
    color = LightBlue,
    rippleAlpha = RippleAlpha (
        pressedAlpha = 0.2f,
        focusedAlpha = 0.1f,
        draggedAlpha = 0.15f,
        hoveredAlpha = 0.075f
    )
)


@Composable
fun AppRipple(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalRippleConfiguration provides AppRippleConfiguration,
        content = content
    )
}

@Composable
fun DisabledRipple(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalRippleConfiguration provides null,
        content = content
    )
}