package com.octagontechnologies.sky_weather.core.designsystem.theme

import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import com.octagontechnologies.sky_weather.core.model.preferences.Theme
import timber.log.Timber

@Composable
fun AppTheme(
    theme: Theme = Theme.DARK,
    systemDarkTheme: Boolean = false,
    // isSystemInDarkTheme(),
//    // Dynamic color is available on Android 12+
//    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    LaunchedEffect(key1 = theme) {
        Timber.d("Theme in AppTheme is $theme")
    }

    val colorScheme =
        when (theme) {
            Theme.LIGHT -> LightColorScheme
            else -> DarkColorScheme
        }

    val appColors =
        when (theme) {
            Theme.LIGHT -> lightAppColors
            Theme.DARK -> darkAppColors
            Theme.BLACK -> blackAppColors
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = {
            CompositionLocalProvider(
                LocalAppColors provides appColors,
                LocalRippleConfiguration provides null,
            ) {
                content()
            }
        },
    )
}

private val DarkColorScheme =
    darkColorScheme(
        primary = DarkBlack,
        secondary = LightBlack,
        surface = LightBlack,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onSurface = Color.White,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = Color.White,
        secondary = Color.White,
        tertiary = Color.White,
        onPrimary = DarkBlack,
        onSecondary = DarkBlack,
        onSurface = DarkBlack,
    )
