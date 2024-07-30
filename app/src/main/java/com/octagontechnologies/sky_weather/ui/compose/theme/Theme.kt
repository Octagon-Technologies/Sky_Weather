package com.octagontechnologies.sky_weather.ui.compose.theme

import android.view.View
import android.view.Window
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.octagontechnologies.sky_weather.repository.repo.SettingsRepo
import com.octagontechnologies.sky_weather.ui.compose.getActivity
import com.octagontechnologies.sky_weather.utils.Theme
import timber.log.Timber

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


fun changeColor(
    window: Window,
    view: View,
    useWhiteIcons: Boolean,

    // Most of the time, this two will be similar
    statusBarColor: Color,
    bottomBarColor: Color = statusBarColor,
) {
    window.statusBarColor = statusBarColor.toArgb()
    window.navigationBarColor = bottomBarColor.toArgb()

    WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = useWhiteIcons
    WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = useWhiteIcons
}

@Composable
fun isDarkTheme(): Boolean {
    val systemDarkTheme = false // isSystemInDarkTheme()

    val context = LocalContext.current
    val settingsRepo = remember { SettingsRepo(context) }
    val userDarkTheme by settingsRepo.theme.observeAsState()

    return userDarkTheme?.let { it == Theme.DARK } ?: systemDarkTheme
}

@Composable
fun AppTheme(
    systemDarkTheme: Boolean = false,// isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current
    val isDarkTheme = isDarkTheme()

    LaunchedEffect(key1 = isDarkTheme) {
        Timber.d("Theme in AppTheme is dark is $isDarkTheme")
    }

    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        isDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }


    val appColors = if (isDarkTheme) darkAppColors else lightAppColors


    LaunchedEffect(key1 = Unit) {
        val window = (context.getActivity() ?: return@LaunchedEffect).window
        changeColor(window, view, isDarkTheme, appColors.background)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = {
            CompositionLocalProvider(
                LocalAppColors provides appColors,
                LocalRippleTheme provides DisabledRipple
            ) {
                content()
            }
        }
    )
}