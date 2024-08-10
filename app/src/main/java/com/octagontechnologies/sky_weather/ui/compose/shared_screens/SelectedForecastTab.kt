package com.octagontechnologies.sky_weather.ui.compose.shared_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.octagontechnologies.sky_weather.domain.WeatherCode
import com.octagontechnologies.sky_weather.domain.getWeatherIcon
import com.octagontechnologies.sky_weather.domain.getWeatherTitle
import com.octagontechnologies.sky_weather.ui.compose.theme.AppTheme
import com.octagontechnologies.sky_weather.ui.compose.theme.LocalAppColors
import com.octagontechnologies.sky_weather.ui.compose.theme.QuickSand
import com.octagontechnologies.sky_weather.ui.see_more_current.components.MiniWeatherDescription
import com.octagontechnologies.sky_weather.utils.Units


@Composable
fun SelectedForecastScreen(
    conditions: Map<String, String>,
    weatherCode: WeatherCode?,
    temp: Int?,
    units: Units,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            weatherCode?.getWeatherIcon()?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
            }

            Box(Modifier.padding(start = 16.dp)) {
                Text(
                    text = temp?.let { stringResource(id = R.string.temp_format, it) } ?: "",
                    fontFamily = QuickSand,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 58.sp,
                    color = LocalAppColors.current.onSurface
                )
                Text(
                    text = units.getUnitSymbol(),
                    modifier = Modifier.align(Alignment.BottomEnd),
                    fontSize = 22.sp,
                    color = LocalAppColors.current.onSurface.copy(alpha = 0.7f)
                )
            }
        }

        Surface(
            color = LocalAppColors.current.surfaceVariant.copy(alpha = 0.35f),
            modifier = Modifier.padding(top = 16.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                text = weatherCode?.getWeatherTitle() ?: "",
                modifier = Modifier.padding(vertical = 2.dp, horizontal = 16.dp),
                color = LocalAppColors.current.onSurface,
                fontFamily = QuickSand
            )
        }


        Card(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 8.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = LocalAppColors.current.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = (1).dp)
        ) {
            MiniWeatherDescription(
                modifier = Modifier,
                title = "Rain Probability",
                value = "${weatherCode?.rainProbability ?: "--"}%",
                cardColor = LocalAppColors.current.surface,//.surfaceVariant.copy(alpha = 0.35f),
                onCardColor = LocalAppColors.current.onSurface
            )
        }

        Column(Modifier.padding(bottom = 8.dp)) {
            Card(
                colors = CardDefaults.cardColors(containerColor = LocalAppColors.current.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = (0.5).dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    conditions.forEach { (title, value) ->
                        MiniWeatherDescription(
                            modifier = Modifier,
                            title = title,
                            value = value,
                            cardColor = LocalAppColors.current.surfaceSmallerVariant,//surfaceVariant.copy(alpha = 0.35f),
                            onCardColor = LocalAppColors.current.onSurface
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun PreviewSelectedForecastScreen() = AppTheme {
    Column(
        Modifier
            .fillMaxSize()
            .background(LocalAppColors.current.surface)
    ) {
        SelectedForecastScreen(
            conditions = mapOf(),
            weatherCode = WeatherCode(3, 45),
            temp = 24,
            units = Units.METRIC
        )
    }
}