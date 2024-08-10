package com.octagontechnologies.sky_weather.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.octagontechnologies.sky_weather.main_activity.Screens
import com.octagontechnologies.sky_weather.repository.repo.LocationRepo
import com.octagontechnologies.sky_weather.ui.compose.theme.LocalAppColors
import kotlinx.coroutines.flow.firstOrNull

@Composable
fun SplashScreen(locationRepo: LocationRepo, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalAppColors.current.background),
        contentAlignment = Alignment.Center
    ) {
        LaunchedEffect(key1 = Unit) {
            val location = locationRepo.location.firstOrNull()
            navController.navigate(if (location != null) Screens.Current else Screens.SelectLocation)
        }
    }
}