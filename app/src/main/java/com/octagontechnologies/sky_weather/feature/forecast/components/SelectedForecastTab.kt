package com.octagontechnologies.sky_weather.feature.forecast.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.octagontechnologies.sky_weather.R
import com.octagontechnologies.sky_weather.core.designsystem.theme.AppTheme
import com.octagontechnologies.sky_weather.core.designsystem.theme.LocalAppColors
import com.octagontechnologies.sky_weather.core.designsystem.theme.QuickSand
import com.octagontechnologies.sky_weather.core.model.preferences.Units
import com.octagontechnologies.sky_weather.domain.model.WeatherCode
import com.octagontechnologies.sky_weather.domain.model.getWeatherIcon
import com.octagontechnologies.sky_weather.domain.model.getWeatherTitle
import com.octagontechnologies.sky_weather.feature.seemore.presentation.components.MiniWeatherDescription
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedForecastScreen(
    sheetState: ScrollState,
    conditions: ImmutableMap<String, String>,
    weatherCode: WeatherCode?,
    temp: Int?,
    units: Units,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
            .verticalScroll(sheetState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            weatherCode?.getWeatherIcon()?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                )
            }

            Box(Modifier.padding(start = 16.dp)) {
                Text(
                    text = temp?.let { stringResource(id = R.string.temp_format, it) } ?: "",
                    fontFamily = QuickSand,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 58.sp,
                    color = LocalAppColors.current.onSurface,
                )
                Text(
                    text = units.getUnitSymbol(),
                    modifier = Modifier.align(Alignment.BottomEnd),
                    fontSize = 22.sp,
                    color = LocalAppColors.current.onSurface.copy(alpha = 0.7f),
                )
            }
        }

        Surface(
            color = LocalAppColors.current.surfaceVariant.copy(alpha = 0.35f),
            modifier = Modifier.padding(top = 16.dp),
            shape = RoundedCornerShape(24.dp),
        ) {
            Text(
                text = weatherCode?.getWeatherTitle() ?: "",
                modifier = Modifier.padding(vertical = 2.dp, horizontal = 16.dp),
                color = LocalAppColors.current.onSurface,
                fontFamily = QuickSand,
            )
        }

        Card(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 8.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = LocalAppColors.current.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = (1).dp),
        ) {
            MiniWeatherDescription(
                modifier = Modifier,
                title = "Rain Probability",
                value = "${weatherCode?.rainProbability ?: "--"}%",
                cardColor = LocalAppColors.current.surface,
                onCardColor = LocalAppColors.current.onSurface,
            )
        }

        Column(Modifier.padding(bottom = 8.dp)) {
            Card(
                colors = CardDefaults.cardColors(containerColor = LocalAppColors.current.surface),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = (0.5).dp),
                shape = RoundedCornerShape(12.dp),
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                ) {
                    conditions.forEach { (title, value) ->
                        MiniWeatherDescription(
                            modifier = Modifier,
                            title = title,
                            value = value,
                            cardColor = LocalAppColors.current.surfaceSmallerVariant,
                            onCardColor = LocalAppColors.current.onSurface,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PreviewSelectedForecastScreen() =
    AppTheme {
        Column(
            Modifier
                .fillMaxSize()
                .background(LocalAppColors.current.surface),
        ) {
            val scrollState = rememberScrollState()

            SelectedForecastScreen(
                conditions = persistentMapOf(),
                weatherCode = WeatherCode(3, 45),
                temp = 24,
                units = Units.METRIC,
                sheetState = scrollState,
            )
        }
    }
