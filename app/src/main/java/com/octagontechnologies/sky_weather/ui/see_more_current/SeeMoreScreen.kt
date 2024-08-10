package com.octagontechnologies.sky_weather.ui.see_more_current

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.octagontechnologies.sky_weather.R
import com.octagontechnologies.sky_weather.domain.getFormattedTemp
import com.octagontechnologies.sky_weather.domain.getWeatherIcon
import com.octagontechnologies.sky_weather.domain.getWeatherTitle
import com.octagontechnologies.sky_weather.ui.compose.theme.AppTheme
import com.octagontechnologies.sky_weather.ui.compose.theme.LocalAppColors
import com.octagontechnologies.sky_weather.ui.compose.theme.QuickSand
import com.octagontechnologies.sky_weather.ui.see_more_current.components.MiniWeatherDescription

@Composable
fun SeeMoreScreen(navController: NavController) {
    var navigateBack by remember { mutableStateOf(false) }

    val viewModel = hiltViewModel<SeeMoreViewModel>()
    val currentForecast by viewModel.currentForecast.collectAsState(initial = null)
    val units by viewModel.units.collectAsState()
    val conditions by viewModel.conditions.collectAsState(mapOf())


    LaunchedEffect(key1 = navigateBack) {
        if (navigateBack) {
            navController.popBackStack()
            navigateBack = false
        }
    }


    Column(
        Modifier
            .fillMaxSize()
            .background(LocalAppColors.current.background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            Image(
                modifier = Modifier
                    .clickable { navigateBack = true }
                    .align(Alignment.CenterStart)
                    .clip(RoundedCornerShape(6.dp))
                    .background(LocalAppColors.current.backgroundVariant.copy(alpha = 0.6f))
                    .padding(6.dp),
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = stringResource(id = R.string.back_button),
                colorFilter = ColorFilter.tint(LocalAppColors.current.onBackground)
            )

            Text(
                text = stringResource(R.string.current_conditions),
                style = MaterialTheme.typography.labelMedium,
                fontSize = 18.sp,
                color = LocalAppColors.current.onBackground,
                modifier = Modifier.align(Alignment.Center)
            )
        }


        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                currentForecast?.weatherCode?.getWeatherIcon()?.let {
                    Image(
                        painter = painterResource(id = it),
                        contentDescription = null,
                        modifier = Modifier.size(64.dp)
                    )
                }

                Box(Modifier.padding(start = 16.dp)) {
                    Text(
                        text = currentForecast.getFormattedTemp(units),
                        fontFamily = QuickSand,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 58.sp,
                        color = LocalAppColors.current.onBackground
                    )
                    Text(
                        text = units.getUnitSymbol(),
                        modifier = Modifier.align(Alignment.BottomEnd),
                        fontSize = 22.sp,
                        color = LocalAppColors.current.onBackground.copy(alpha = 0.7f)
                    )
                }
            }

            Surface(
                color = LocalAppColors.current.backgroundVariant,
                modifier = Modifier.padding(top = 16.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = currentForecast?.weatherCode?.getWeatherTitle() ?: "",
                    modifier = Modifier.padding(vertical = 2.dp, horizontal = 12.dp),
                    color = LocalAppColors.current.onBackground,
                    fontFamily = QuickSand
                )
            }

//            val conditions = mapOf(
//                "Temperature" to "27",
//                "FeelsLike Temperature" to "27",
//                "Rain Probability" to "27",
//                "Wind" to "24 km/h"
//            )

            Card(
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 24.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(6),
                colors = CardDefaults.cardColors(
                    containerColor = LocalAppColors.current.background,
                    contentColor = LocalAppColors.current.onBackground
                ),
                elevation = CardDefaults.elevatedCardElevation()
            ) {
                Column(Modifier.padding(vertical = 8.dp)) {
                    conditions.forEach { (name, value) ->
                        MiniWeatherDescription(
                            title = name,
                            value = value,
                            cardColor = LocalAppColors.current.backgroundVariant,
                            onCardColor = LocalAppColors.current.onBackground
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSeeMoreScreen() = AppTheme {
    SeeMoreScreen(navController = rememberNavController())
}