package com.octagontechnologies.sky_weather.feature.splash.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.octagontechnologies.sky_weather.core.designsystem.theme.LocalAppColors
import com.octagontechnologies.sky_weather.core.navigation.Screens
import com.octagontechnologies.sky_weather.data.repository.LocationRepo
import kotlinx.coroutines.flow.firstOrNull

@Composable
fun SplashScreen(
    locationRepo: LocationRepo,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(LocalAppColors.current.background),
        contentAlignment = Alignment.Center,
    ) {
        LaunchedEffect(key1 = Unit) {
            val location = locationRepo.location.firstOrNull()
            navController.navigate(if (location != null) Screens.CURRENT else Screens.SELECT_LOCATION)
        }
    }
}
