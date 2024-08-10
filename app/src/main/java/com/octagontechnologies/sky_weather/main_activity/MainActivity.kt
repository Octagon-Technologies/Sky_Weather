package com.octagontechnologies.sky_weather.main_activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityOptionsCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.octagontechnologies.sky_weather.R
import com.octagontechnologies.sky_weather.repository.repo.LocationRepo
import com.octagontechnologies.sky_weather.repository.repo.SettingsRepo
import com.octagontechnologies.sky_weather.ui.compose.AppBottomNavBar
import com.octagontechnologies.sky_weather.ui.compose.SplashScreen
import com.octagontechnologies.sky_weather.ui.compose.theme.AppTheme
import com.octagontechnologies.sky_weather.ui.compose.theme.LocalAppColors
import com.octagontechnologies.sky_weather.ui.current_forecast.components.BottomNav
import com.octagontechnologies.sky_weather.ui.current_forecast.components.getScreen
import com.octagontechnologies.sky_weather.ui.current_forecast.screen.CurrentForecastScreen
import com.octagontechnologies.sky_weather.ui.daily_forecast.list.DailyForecastScreen
import com.octagontechnologies.sky_weather.ui.find_location.screen.FindLocationScreen
import com.octagontechnologies.sky_weather.ui.hourly_forecast.list.HourlyForecastScreen
import com.octagontechnologies.sky_weather.ui.see_more_current.SeeMoreScreen
import com.octagontechnologies.sky_weather.ui.settings.SettingsScreen
import com.octagontechnologies.sky_weather.utils.Theme
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

        ActivityOptionsCompat.makeCustomAnimation(
            applicationContext,
            R.animator.nav_default_enter_anim,
            R.animator.nav_default_exit_anim
        )

        setContent {
            val theme by settingsRepo.theme.observeAsState(initial = Theme.DARK)
            val isAppInStartup by locationRepo.location.map { it == null }
                .collectAsState(initial = true)

            AppTheme(theme = theme) {
                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }

                var showBottomNav by remember { mutableStateOf(true) }
                var activeBottomNav by remember { mutableStateOf(BottomNav.Current) }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    }
                ) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .background(LocalAppColors.current.background)
                    ) {
                        NavHost(
                            modifier = Modifier.weight(1f),
                            navController = navController,
                            startDestination = Screens.SplashScreen
                        ) {
                            composable(Screens.SplashScreen) {
                                SplashScreen(locationRepo, navController)
                            }

                            composable(Screens.Current) {
                                showBottomNav = true
                                CurrentForecastScreen(navController = navController)
                            }
                            composable(Screens.SeeMore) {
                                showBottomNav = false
                                SeeMoreScreen(navController = navController)
                            }

                            composable(Screens.Hourly) {
                                HourlyForecastScreen(
                                    coroutineScope = coroutineScope,
                                    showBottomNavView = { shouldShow ->
                                        showBottomNav = shouldShow
                                    }
                                )
                            }

                            composable(Screens.Daily) {
                                DailyForecastScreen(
                                    coroutineScope = coroutineScope,
                                    showBottomNavView = { shouldShow ->
                                        showBottomNav = shouldShow
                                    }
                                )
                            }

                            composable(Screens.SelectLocation) {
                                showBottomNav = false
                                FindLocationScreen(
                                    navController = navController,
                                    snackbarHostState = snackbarHostState
                                )
                            }

                            composable(Screens.Settings) {
                                showBottomNav = false
                                SettingsScreen(navController = navController)
                            }
                        }

                        LaunchedEffect(key1 = activeBottomNav) {
                            if (!isAppInStartup)
                                navController.navigate(activeBottomNav.getScreen())
                        }


                        if (!isAppInStartup) {
                            val ANIM_DURATION = remember { 250 }
                            AnimatedVisibility(
                                enter = fadeIn(tween(ANIM_DURATION)) + expandVertically(
                                    tween(
                                        ANIM_DURATION
                                    )
                                ),
                                exit = fadeOut(tween(ANIM_DURATION)) + shrinkVertically(
                                    tween(
                                        ANIM_DURATION
                                    )
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        LocalAppColors.current.background
                                    ), visible = showBottomNav
                            ) {
                                AppBottomNavBar(
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .padding(vertical = 6.dp),
                                    activeBottomNav = activeBottomNav,
                                    navigateToBottomNav = { newTab ->
                                        if (newTab != activeBottomNav) activeBottomNav = newTab
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}