package com.octagontechnologies.sky_weather.ui.daily_forecast.list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.octagontechnologies.sky_weather.domain.daily.DailyForecast
import com.octagontechnologies.sky_weather.domain.getWeatherIcon
import com.octagontechnologies.sky_weather.ui.compose.theme.AdventPro
import com.octagontechnologies.sky_weather.ui.compose.theme.LocalAppColors
import com.octagontechnologies.sky_weather.ui.compose.theme.QuickSand
import com.octagontechnologies.sky_weather.utils.Units
import com.octagontechnologies.sky_weather.utils.getDateOfMonth
import com.octagontechnologies.sky_weather.utils.getDay

@Composable
fun DailyForecastCard(forecast: DailyForecast, units: Units, modifier: Modifier = Modifier, onForecastClick: () -> Unit) {
    Card(
        modifier = modifier,
        onClick = { onForecastClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = LocalAppColors.current.backgroundVariant,
            contentColor = LocalAppColors.current.onBackground
        )
    ) {
        Column(
            Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 8.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    modifier = Modifier.requiredSize(28.dp),
                    color = LocalAppColors.current.distinctBackground
                ) {
                    Box(Modifier.fillMaxSize()) {
                        Text(
                            text = forecast.timeInEpochSeconds.getDateOfMonth(),
                            modifier = Modifier.align(Alignment.Center),
                            fontFamily = QuickSand,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = forecast.timeInEpochSeconds.getDay(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp
                )
            }


            Image(
                painter = painterResource(id = forecast.dayTime.weatherCode.getWeatherIcon()),
                contentDescription = null,
                modifier = Modifier.size(56.dp)
            )

            Column(Modifier.padding(start = 8.dp, top = 12.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "High: ${forecast.getTempHigh(units)}",
                        letterSpacing = (0.25).sp, fontSize = (17.5).sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = AdventPro,)
                    Text(text = "Low: ${forecast.getTempLow(units)}",
                        letterSpacing = (0.25).sp, fontSize = (17.5).sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = AdventPro,)
                }

                Text(
                    text = "Rain probability: ${forecast.dayTime.rainProbability}%",
                    fontSize = 18.sp,
                    letterSpacing = (0.25).sp,
                    lineHeight = 17.sp,
                    fontFamily = AdventPro,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                )
            }
        }
    }
}