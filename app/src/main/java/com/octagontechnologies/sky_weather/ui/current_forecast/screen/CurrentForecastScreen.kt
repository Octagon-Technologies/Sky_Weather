package com.octagontechnologies.sky_weather.ui.current_forecast.screen

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.octagontechnologies.sky_weather.domain.SingleForecast
import com.octagontechnologies.sky_weather.domain.getFormattedFeelsLike
import com.octagontechnologies.sky_weather.domain.getFormattedTemp
import com.octagontechnologies.sky_weather.domain.getWeatherIcon
import com.octagontechnologies.sky_weather.main_activity.Screens
import com.octagontechnologies.sky_weather.ui.compose.getActivity
import com.octagontechnologies.sky_weather.ui.compose.theme.AppTheme
import com.octagontechnologies.sky_weather.ui.compose.theme.CabinCondensed
import com.octagontechnologies.sky_weather.ui.compose.theme.LessDarkBlue
import com.octagontechnologies.sky_weather.ui.compose.theme.LightBlack
import com.octagontechnologies.sky_weather.ui.compose.theme.LocalAppColors
import com.octagontechnologies.sky_weather.ui.compose.theme.Poppins
import com.octagontechnologies.sky_weather.ui.compose.theme.QuickSand
import com.octagontechnologies.sky_weather.ui.current_forecast.CurrentForecastViewModel
import com.octagontechnologies.sky_weather.ui.current_forecast.components.CurrentTopBar
import com.octagontechnologies.sky_weather.ui.daily_forecast.selected_details.LunarPreview
import com.octagontechnologies.sky_weather.ui.see_more_current.components.MiniWeatherDescription
import com.octagontechnologies.sky_weather.utils.Theme
import com.octagontechnologies.sky_weather.utils.Units
import com.octagontechnologies.sky_weather.utils.weather.getCoreWeatherConditions

@Composable
fun CurrentForecastScreen(
    navController: NavController,
    viewModel: CurrentForecastViewModel = hiltViewModel()
) {
    val currentForecast by viewModel.currentForecast.collectAsState(initial = null)
    val location by viewModel.location.collectAsState(initial = null)
    val lunar by viewModel.lunarForecast.observeAsState()

    val units by viewModel.units.observeAsState()
    val theme by viewModel.theme.observeAsState(initial = Theme.DARK)
    val windDirectionUnits by viewModel.windDirectionUnits.observeAsState()

    val coreConditions = remember(key1 = currentForecast) {
        currentForecast?.getCoreWeatherConditions(units, windDirectionUnits) ?: mapOf()
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(LocalAppColors.current.background)
    ) {
        CurrentTopBar(location, navController)


        val scrollState = rememberScrollState()
        if (currentForecast == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(36.dp)
                )
            }
        } else {
            Column(
                Modifier
                    .padding(top = 2.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                CurrentTempBar(
                    units = units,
                    theme = theme,
                    currentForecast = currentForecast!!,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                val predictions by viewModel.predictions.observeAsState()
                predictions?.let {
                    Row(
                        modifier = Modifier
                            .padding(top = 28.dp)
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        predictions!!.forEachIndexed { index, hourly ->
                            CurrentHourlyPrediction(
                                hour = index + 1,
                                formattedTemp = hourly.getFormattedTemp(units),
                                formattedFeelsLike = hourly.getFormattedFeelsLike(units),
                                weatherIcon = hourly.weatherCode.getWeatherIcon(),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

//                val lunar = Lunar("8:00", "17:51", null, null)

                Row(
                    Modifier
                        .padding(horizontal = 8.dp)
                        .padding(top = 30.dp)
                        .height(150.dp)
                ) {
                    LunarPreview(
                        modifier = Modifier.weight(1f),
                        isSunPreview = true,
                        rise = lunar?.sunRise,
                        set = lunar?.sunSet,
                        tabColor = LocalAppColors.current.background,
                        tabSecondaryColor = LocalAppColors.current.backgroundVariant,
                        onTabColor = LocalAppColors.current.onBackground
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    CurrentRainProbability(
                        rainProbability = currentForecast?.weatherCode?.rainProbability,
                        modifier = Modifier.weight(1f)
                    )
                }


                Text(
                    text = "Current Conditions",
                    color = LocalAppColors.current.onBackground,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 24.dp)
                )

                Surface(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(top = 12.dp),
                    shadowElevation = 2.dp,
                    tonalElevation = 2.dp,
                    shape = RoundedCornerShape(8.dp),
                    color = LocalAppColors.current.background
                ) {
                    Column {
                        coreConditions.forEach { (title, value) ->
                            MiniWeatherDescription(
                                title = title,
                                value = value,
                                cardColor = LocalAppColors.current.backgroundVariant.copy(alpha = 0.2f),
                                onCardColor = LocalAppColors.current.onBackground
                            )
                        }
                    }
                }


                Button(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(top = 12.dp, bottom = 16.dp)
                        .fillMaxWidth(),
                    onClick = {
                        navController.navigate(Screens.SeeMore)
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = LocalAppColors.current.background,
                        containerColor = LocalAppColors.current.onBackground
                    ),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, LocalAppColors.current.onBackground)
                ) {
                    Text(
                        text = stringResource(id = R.string.see_more_plain_text),
                        fontFamily = QuickSand,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

            }
        }
    }
}

@Composable
private fun CurrentTempBar(
    units: Units?,
    theme: Theme,
    currentForecast: SingleForecast,
    modifier: Modifier
) {
    Card(
        shape = CircleShape,
        modifier = modifier
            .padding(top = 16.dp)
            .fillMaxWidth(0.7f)
            .aspectRatio(1f),
        colors = CardDefaults.cardColors(
            containerColor = LocalAppColors.current.background,
            contentColor = LocalAppColors.current.onBackground
        ),
        border =
        BorderStroke(
            16.dp,
            if (theme == Theme.BLACK) Brush.linearGradient(
                0f to Color.White,
                0.15f to Color.White,
                0.45f to LightBlack,
                0.75f to Color.Black
            )
            else Brush.linearGradient(0f to LessDarkBlue, 1f to LessDarkBlue)
        )
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Column(
                Modifier
                    .padding(top = 24.dp)
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = currentForecast.weatherCode.getWeatherIcon()),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )

                Box(Modifier.padding(top = 4.dp)) {
                    Text(
                        text = currentForecast.getFormattedTemp(units),
                        fontSize = 42.sp
                    )
                    Text(
                        text = (units ?: Units.getDefault()).getUnitSymbol(),
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .align(Alignment.BottomEnd),
                        fontSize = 14.sp,
                        color = LocalAppColors.current.onBackground.copy(alpha = 0.75f)
                    )
                }

                Text(
                    text = currentForecast.getFormattedFeelsLike(units),
                    fontFamily = CabinCondensed,
                    fontSize = 17.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}


@Composable
fun CurrentHourlyPrediction(
    hour: Int,
    formattedTemp: String,
    formattedFeelsLike: String,
    @DrawableRes weatherIcon: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = LocalAppColors.current.backgroundVariant,
            contentColor = LocalAppColors.current.onBackground
        )
    ) {
        Column(Modifier.padding(vertical = 6.dp, horizontal = 12.dp)) {
            Text(
                "In $hour hour${if (hour > 1) "s" else ""}",
                fontFamily = QuickSand,
                fontWeight = FontWeight.Medium,
                color = LocalAppColors.current.onBackground.copy(alpha = 0.8f)
            )

            Row(
                modifier = Modifier.padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = weatherIcon),
                    contentDescription = null,
                    modifier = Modifier.size(34.dp)
                )

                Text(
                    text = formattedTemp,
                    fontSize = 28.sp,
                    letterSpacing = (0.2).sp,
                    fontFamily = QuickSand,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }

            Text(
                text = formattedFeelsLike,
                fontFamily = CabinCondensed,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}


@Composable
fun CurrentRainProbability(rainProbability: Int?, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxHeight(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = LocalAppColors.current.backgroundVariant,
            contentColor = LocalAppColors.current.onBackground
        )
    ) {
        Column(
            Modifier
                .padding(start = 8.dp, top = 12.dp)
                .fillMaxWidth()
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = Color.LightGray.copy(alpha = 0.25f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Rain Probability",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }

            Text(
                text = "${rainProbability ?: "--"}%",
                fontSize = 34.sp,
                fontFamily = Poppins,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 20.dp)
            )
        }
    }
}


@Preview
@Composable
private fun PreviewCurrentForecastScreen() = AppTheme {
    CurrentForecastScreen(rememberNavController())
}