package com.octagontechnologies.sky_weather.ui.compose.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.octagontechnologies.sky_weather.ui.compose.getActivity
import com.octagontechnologies.sky_weather.ui.compose.restoreIconsToWhite
import com.octagontechnologies.sky_weather.utils.Theme
import timber.log.Timber


@Composable
fun AppTheme(
    theme: Theme = Theme.DARK,
    systemDarkTheme: Boolean = false,// isSystemInDarkTheme(),
//    // Dynamic color is available on Android 12+
//    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current


    LaunchedEffect(key1 = theme) {
        Timber.d("Theme in AppTheme is $theme")
    }

    val colorScheme = when (theme) {
        Theme.LIGHT -> LightColorScheme
        else -> DarkColorScheme
    }


    val appColors = when (theme) {
        Theme.LIGHT -> lightAppColors
        Theme.DARK -> darkAppColors
        Theme.BLACK -> blackAppColors
    }

    LaunchedEffect(key1 = theme) {
        val window = (context.getActivity() ?: return@LaunchedEffect).window

        window.statusBarColor = appColors.background.toArgb()
        window.navigationBarColor = appColors.background.toArgb()

        restoreIconsToWhite(window, view)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = {
            DisabledRipple {
                content()
            }
        }
    )
}


private val DarkColorScheme = darkColorScheme(
    primary = DarkBlack,
    secondary = LightBlack,
    surface = LightBlack,

    onPrimary = Color.White,
    onSecondary = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color.White,
    secondary = Color.White,
    tertiary = Color.White,

    onPrimary = DarkBlack,
    onSecondary = DarkBlack,
    onSurface = DarkBlack
)