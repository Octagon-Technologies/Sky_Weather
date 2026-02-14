package com.octagontechnologies.sky_weather.feature.forecast.presentation.list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.octagontechnologies.sky_weather.R
import com.octagontechnologies.sky_weather.core.common.time.getHoursAndMins
import com.octagontechnologies.sky_weather.core.designsystem.theme.AppTheme
import com.octagontechnologies.sky_weather.core.designsystem.theme.LocalAppColors
import com.octagontechnologies.sky_weather.core.model.preferences.TimeFormat
import com.octagontechnologies.sky_weather.core.model.preferences.Units
import com.octagontechnologies.sky_weather.core.ui.constants.BASE_SHIMMER_COLOR
import com.octagontechnologies.sky_weather.domain.model.SingleForecast
import com.octagontechnologies.sky_weather.domain.model.getFormattedTemp
import com.octagontechnologies.sky_weather.domain.model.getWeatherIcon
import com.valentinilk.shimmer.shimmer

@Composable
fun HourlyForecastCard(
    hourlyForecast: SingleForecast,
    units: Units?,
    timeFormat: TimeFormat?,
    modifier: Modifier = Modifier,
    selectHourlyForecast: () -> Unit,
) {
    Card(
        onClick = { selectHourlyForecast() },
        colors =
            CardDefaults.cardColors(
                containerColor = LocalAppColors.current.backgroundVariant,
                contentColor = LocalAppColors.current.onBackground,
            ),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(horizontal = 8.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.padding(horizontal = 4.dp),
                color = LocalAppColors.current.onBackground.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp),
                contentColor = LocalAppColors.current.onBackground,
            ) {
                Text(
                    text = hourlyForecast.timeInEpochMillis.getHoursAndMins(timeFormat),
                    letterSpacing = 0.sp,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                )
            }
//            Text(text = hourlyForecast.timeInEpochMillis.getHoursAndMins(timeFormat))

            Image(
                painter = painterResource(id = hourlyForecast.weatherCode.getWeatherIcon()),
                contentDescription = null,
                modifier =
                    Modifier
                        .padding(start = 14.dp)
                        .size(43.dp),
            )

            Box(modifier = Modifier.padding(start = 20.dp)) {
                Text(
                    text = hourlyForecast.getFormattedTemp(units),
                    fontSize = 27.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(end = 6.dp, top = 5.dp, bottom = 5.dp),
                )

                Text(
                    text = (units ?: Units.getDefault()).getUnitSymbol(),
                    modifier = Modifier.align(Alignment.BottomEnd),
                    fontSize = 14.sp,
                    color = LocalAppColors.current.onBackground.copy(alpha = 0.75f),
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.drop),
                contentDescription = null,
                modifier =
                    Modifier
                        .padding(horizontal = 4.dp)
                        .size(16.dp),
            )

            Text(
                text = "${hourlyForecast.weatherCode.rainProbability ?: "0"}%",
                fontWeight = FontWeight.SemiBold,
                fontSize = (17.25).sp,
            )
        }
    }
}

@Composable
fun ShimmerHourlyForecastCard(modifier: Modifier = Modifier) {
    Card(
        onClick = { },
        colors =
            CardDefaults.cardColors(
                containerColor = BASE_SHIMMER_COLOR.copy(alpha = 0.4f),
                contentColor = LocalAppColors.current.onBackground,
            ),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier =
                    Modifier
                        .height(50.dp)
                        .width(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(BASE_SHIMMER_COLOR),
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f),
            ) {
                Box(
                    modifier =
                        Modifier
                            .height(20.dp)
                            .width(150.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(BASE_SHIMMER_COLOR),
                )

                Box(
                    modifier =
                        Modifier
                            .height(20.dp)
                            .width(60.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(BASE_SHIMMER_COLOR),
                )
            }

            Box(
                modifier =
                    Modifier
                        .height(20.dp)
                        .width(60.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(BASE_SHIMMER_COLOR),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewHourlyForecastCard() =
    AppTheme {
        Column(
            Modifier
                .fillMaxWidth()
                .background(LocalAppColors.current.background)
                .padding(vertical = 8.dp),
        ) {
            HourlyForecastCard(
                hourlyForecast = SingleForecast.TEST_DUMMY,
                units = Units.getDefault(),
                timeFormat = TimeFormat.getDefault(),
                modifier = Modifier.padding(horizontal = 8.dp),
                selectHourlyForecast = {},
            )
        }
    }

@Preview
@Composable
private fun PreviewShimmerHourlyForecastCard() =
    AppTheme {
        Column(
            Modifier
                .fillMaxWidth()
                .background(LocalAppColors.current.background)
                .padding(vertical = 8.dp)
                .shimmer(),
        ) {
            ShimmerHourlyForecastCard()
        }
    }

@Preview
@Composable
private fun PreviewListOfHourlyForecastCard() =
    AppTheme {
        Column(
            Modifier
                .fillMaxWidth()
                .background(LocalAppColors.current.background)
                .padding(vertical = 8.dp),
        ) {
            repeat(6) {
                HourlyForecastCard(
                    hourlyForecast = SingleForecast.TEST_DUMMY,
                    units = Units.getDefault(),
                    timeFormat = TimeFormat.getDefault(),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    selectHourlyForecast = {
                    },
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
