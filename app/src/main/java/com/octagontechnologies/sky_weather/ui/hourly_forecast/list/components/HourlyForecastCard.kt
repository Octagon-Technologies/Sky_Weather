package com.octagontechnologies.sky_weather.ui.hourly_forecast.list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.octagontechnologies.sky_weather.R
import com.octagontechnologies.sky_weather.domain.SingleForecast
import com.octagontechnologies.sky_weather.domain.getFormattedTemp
import com.octagontechnologies.sky_weather.domain.getWeatherIcon
import com.octagontechnologies.sky_weather.ui.compose.theme.AppTheme
import com.octagontechnologies.sky_weather.ui.compose.theme.LocalAppColors
import com.octagontechnologies.sky_weather.utils.TimeFormat
import com.octagontechnologies.sky_weather.utils.Units
import com.octagontechnologies.sky_weather.utils.getHoursAndMins

@Composable
fun HourlyForecastCard(
    hourlyForecast: SingleForecast,
    units: Units?,
    timeFormat: TimeFormat?,
    modifier: Modifier = Modifier,
    selectHourlyForecast: () -> Unit
) {
    Card(
        onClick = { selectHourlyForecast() },
        colors = CardDefaults.cardColors(
            containerColor = LocalAppColors.current.backgroundVariant,
            contentColor = LocalAppColors.current.onBackground
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            Modifier.padding(horizontal = 8.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.padding(horizontal = 4.dp),
                color = LocalAppColors.current.onBackground.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp),
                contentColor = LocalAppColors.current.onBackground
            ) {
                Text(
                    text = hourlyForecast.timeInEpochMillis.getHoursAndMins(timeFormat),
                    letterSpacing = 0.sp,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
//            Text(text = hourlyForecast.timeInEpochMillis.getHoursAndMins(timeFormat))

            Image(
                painter = painterResource(id = hourlyForecast.weatherCode.getWeatherIcon()),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 14.dp)
                    .size(43.dp)
            )

            Box(modifier = Modifier.padding(start = 20.dp)) {
                Text(
                    text = hourlyForecast.getFormattedTemp(units),
                    fontSize = 27.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(end = 6.dp, top = 5.dp, bottom = 5.dp)
                )

                Text(
                    text = (units ?: Units.getDefault()).getUnitSymbol(),
                    modifier = Modifier.align(Alignment.BottomEnd),
                    fontSize = 14.sp,
                    color = LocalAppColors.current.onBackground.copy(alpha = 0.75f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.drop),
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(16.dp)
            )

            Text(
                text = "${hourlyForecast.weatherCode.rainProbability ?: "0"}%",
                fontWeight = FontWeight.SemiBold,
                fontSize = (17.25).sp
            )
        }
    }
}


@Preview
@Composable
private fun PreviewHourlyForecastCard() = AppTheme {
    Column(
        Modifier
            .fillMaxWidth()
            .background(LocalAppColors.current.background)
            .padding(vertical = 8.dp)
    ) {
        repeat(6) {
            HourlyForecastCard(
                hourlyForecast = SingleForecast.TEST_DUMMY,
                units = Units.getDefault(),
                timeFormat = TimeFormat.getDefault(),
                modifier = Modifier.padding(horizontal = 8.dp),
                selectHourlyForecast = {

                }
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}