package com.octagontechnologies.sky_weather.core.designsystem.components

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.octagontechnologies.sky_weather.core.designsystem.theme.LocalAppColors

/**
 * Provides [LocalAppColors.current.onBackground] as [LocalContentColor] for all descendants.
 *
 * This is intended for screens/sections that use the app "background" palette (blue context).
 * By setting a content color at the boundary, nested [androidx.compose.material3.Text] and
 * icons inherit the expected foreground color automatically, which removes repetitive
 * `color = LocalAppColors.current.onBackground` declarations.
 */
@Composable
fun OnBackgroundAsContentColor(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalContentColor provides LocalAppColors.current.onBackground) {
        content()
    }
}

/**
 * Provides [LocalAppColors.current.onSurface] as [LocalContentColor] for all descendants.
 *
 * This is intended for screens/sections that use the app "surface" palette
 * (light/dark card-style context).
 *
 * We keep both background and surface scopes because the app has two distinct visual contexts:
 * one where text sits on `background`, and one where text sits on `surface`.
 * Separating them keeps contrast rules explicit and prevents accidental color reuse
 * across different UI layers.
 */
@Composable
fun OnSurfaceAsContentColor(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalContentColor provides LocalAppColors.current.onSurface) {
        content()
    }
}
