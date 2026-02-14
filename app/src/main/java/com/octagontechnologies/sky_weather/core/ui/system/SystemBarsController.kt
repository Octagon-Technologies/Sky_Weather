package com.octagontechnologies.sky_weather.core.ui.system

import android.view.View
import android.view.Window
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.octagontechnologies.sky_weather.core.designsystem.components.shouldUseDarkIcons
import com.octagontechnologies.sky_weather.core.designsystem.theme.LocalAppColors

@Composable
fun SystemBarsController(
    navigateBack: Boolean,
    // Whether to intercept navigateBack and BackHandler commands...
    // Useful on initial startup when back press (with no location) should cause app exit
    onNavigateBack: () -> Unit,
    resetNavigateBack: () -> Unit,
) {
    val view = LocalView.current
    val window = (view.context.getActivity() ?: return).window
    val systemBarsOverrides = LocalSystemBarsColorOverrides.current

    val whiteBlackColor = LocalAppColors.current.surface

    val defaultAppBackgroundColor = LocalAppColors.current.background

    LaunchedEffect(key1 = whiteBlackColor) {
        systemBarsOverrides?.let { overrides ->
            overrides.setStatusBarColor(whiteBlackColor)
            overrides.setNavigationBarColor(whiteBlackColor)
            refreshSystemIcons(window, view, whiteBlackColor)
        }
        // If in Light mode, the status bars will be white hence icons need to be black here
        if (whiteBlackColor == Color.White) {
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            systemBarsOverrides?.let { overrides ->
                overrides.setStatusBarColor(defaultAppBackgroundColor)
                overrides.setNavigationBarColor(defaultAppBackgroundColor)
                refreshSystemIcons(window, view, defaultAppBackgroundColor)
            }
        }
    }

    LaunchedEffect(key1 = navigateBack) {
        if (navigateBack) {
            onNavigateBack()

            systemBarsOverrides?.let { overrides ->
                overrides.setStatusBarColor(defaultAppBackgroundColor)
                overrides.setNavigationBarColor(defaultAppBackgroundColor)
            }

            refreshSystemIcons(window, view, defaultAppBackgroundColor)

            resetNavigateBack()
        }
    }

    BackHandler {
        onNavigateBack()

        systemBarsOverrides?.let { overrides ->
            overrides.setStatusBarColor(defaultAppBackgroundColor)
            overrides.setNavigationBarColor(defaultAppBackgroundColor)
        }

        refreshSystemIcons(window, view, defaultAppBackgroundColor)
    }
}

fun refreshSystemIcons(
    window: Window,
    view: View,
    containerColor: Color,
) {
    val shouldUseDarkIcons = shouldUseDarkIcons(containerColor.toArgb())
    WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = shouldUseDarkIcons
    WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = shouldUseDarkIcons
}
