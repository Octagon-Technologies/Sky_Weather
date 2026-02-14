package com.octagontechnologies.sky_weather

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.octagontechnologies.sky_weather.core.designsystem.theme.AppTheme
import com.octagontechnologies.sky_weather.core.designsystem.theme.LocalAppColors
import com.octagontechnologies.sky_weather.core.model.preferences.Theme
import com.octagontechnologies.sky_weather.core.navigation.Screens
import com.octagontechnologies.sky_weather.core.ui.system.AppBottomNavBar
import com.octagontechnologies.sky_weather.core.ui.system.LocalSystemBarsColorOverrides
import com.octagontechnologies.sky_weather.core.ui.system.SystemBarsColorOverrides
import com.octagontechnologies.sky_weather.data.repository.LocationRepo
import com.octagontechnologies.sky_weather.data.repository.SettingsRepo
import com.octagontechnologies.sky_weather.feature.current.presentation.CurrentForecastScreen
import com.octagontechnologies.sky_weather.feature.current.presentation.components.BottomNav
import com.octagontechnologies.sky_weather.feature.current.presentation.components.getScreen
import com.octagontechnologies.sky_weather.feature.findlocation.presentation.FindLocationScreen
import com.octagontechnologies.sky_weather.feature.forecast.presentation.list.DailyForecastScreen
import com.octagontechnologies.sky_weather.feature.forecast.presentation.list.HourlyForecastScreen
import com.octagontechnologies.sky_weather.feature.seemore.presentation.SeeMoreScreen
import com.octagontechnologies.sky_weather.feature.settings.presentation.SettingsScreen
import com.octagontechnologies.sky_weather.feature.splash.presentation.SplashScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var settingsRepo: SettingsRepo

    @Inject
    lateinit var locationRepo: LocationRepo

    @SuppressLint("SetTextI18n", "UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        // Use transparent system bars so we draw colors in the inset areas (avoids deprecated APIs)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.Companion.dark(Color.Companion.Transparent.toArgb()),
            navigationBarStyle = SystemBarStyle.Companion.dark(Color.Companion.Transparent.toArgb()),
        )

        setContent {
            val theme by settingsRepo.theme.observeAsState(initial = Theme.DARK)
            val isAppInStartup by locationRepo.location
                .map { it == null }
                .collectAsState(initial = true)

            AppTheme(theme = theme) {
                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }
                var showBottomNav by remember { mutableStateOf(true) }
                var activeBottomNav by remember { mutableStateOf(BottomNav.Current) }
                var statusBarColorOverride by remember { mutableStateOf<Color?>(null) }
                var navigationBarColorOverride by remember { mutableStateOf<Color?>(null) }

                val systemBarsOverrides =
                    remember {
                        SystemBarsColorOverrides(
                            setStatusBarColor = { statusBarColorOverride = it },
                            setNavigationBarColor = { navigationBarColorOverride = it },
                        )
                    }

                CompositionLocalProvider(LocalSystemBarsColorOverrides provides systemBarsOverrides) {
                    Box(Modifier.Companion.fillMaxSize()) {
                        Scaffold(
                            modifier =
                                Modifier.Companion
                                    .fillMaxSize(),
                            snackbarHost = {
                                SnackbarHost(hostState = snackbarHostState)
                            },
                        ) {
                            Column(
                                Modifier.Companion
                                    .fillMaxSize()
                                    .background(LocalAppColors.current.background)
                                    .systemBarsPadding(),
                            ) {
                                NavHost(
                                    modifier = Modifier.Companion.weight(1f),
                                    navController = navController,
                                    startDestination = Screens.SPLASH_SCREEN,
                                ) {
                                    composable(Screens.SPLASH_SCREEN) {
                                        SplashScreen(locationRepo, navController)
                                    }

                                    composable(Screens.CURRENT) {
                                        showBottomNav = true
                                        CurrentForecastScreen(navController = navController)
                                    }
                                    composable(Screens.SEE_MORE) {
                                        showBottomNav = false
                                        SeeMoreScreen(navController = navController)
                                    }

                                    composable(Screens.HOURLY) {
                                        HourlyForecastScreen(
                                            coroutineScope = coroutineScope,
                                            showBottomNavView = { shouldShow ->
                                                showBottomNav = shouldShow
                                            },
                                        )
                                    }

                                    composable(Screens.DAILY) {
                                        DailyForecastScreen(
                                            coroutineScope = coroutineScope,
                                            showBottomNavView = { shouldShow ->
                                                showBottomNav = shouldShow
                                            },
                                        )
                                    }

                                    composable(Screens.SELECT_LOCATION) {
                                        showBottomNav = false
                                        FindLocationScreen(
                                            navController = navController,
                                            snackbarHostState = snackbarHostState,
                                        )
                                    }

                                    composable(Screens.SETTINGS) {
                                        showBottomNav = false
                                        SettingsScreen(navController = navController)
                                    }
                                }

                                LaunchedEffect(key1 = activeBottomNav) {
                                    if (!isAppInStartup) {
                                        navController.navigate(activeBottomNav.getScreen())
                                    }
                                }

                                if (!isAppInStartup) {
                                    AnimatedVisibility(
                                        enter =
                                            fadeIn(tween(ANIM_DURATION)) +
                                                expandVertically(
                                                    tween(
                                                        ANIM_DURATION,
                                                    ),
                                                ),
                                        exit =
                                            fadeOut(tween(ANIM_DURATION)) +
                                                shrinkVertically(
                                                    tween(
                                                        ANIM_DURATION,
                                                    ),
                                                ),
                                        modifier =
                                            Modifier.Companion
                                                .fillMaxWidth()
                                                .background(
                                                    LocalAppColors.current.background,
                                                ),
                                        visible = showBottomNav,
                                    ) {
                                        AppBottomNavBar(
                                            modifier =
                                                Modifier.Companion
                                                    .padding(horizontal = 8.dp)
                                                    .padding(vertical = 6.dp),
                                            activeBottomNav = activeBottomNav,
                                            navigateToBottomNav = { newTab ->
                                                if (newTab != activeBottomNav) {
                                                    activeBottomNav =
                                                        newTab
                                                }
                                            },
                                        )
                                    }
                                }
                            }

                            // Draw status bar area with override color (edge-to-edge, no deprecated APIs)
                            statusBarColorOverride?.let { color ->
                                Box(
                                    Modifier.Companion
                                        .align(Alignment.Companion.TopCenter)
                                        .fillMaxWidth()
                                        .windowInsetsTopHeight(WindowInsets.Companion.statusBars)
                                        .background(color),
                                )
                            }

                            // Draw navigation bar area with override color (edge-to-edge, no deprecated APIs)
                            navigationBarColorOverride?.let { color ->
                                Box(
                                    Modifier.Companion
                                        .align(Alignment.Companion.BottomCenter)
                                        .fillMaxWidth()
                                        .windowInsetsBottomHeight(WindowInsets.Companion.navigationBars)
                                        .background(color),
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val ANIM_DURATION = 250
    }
}
