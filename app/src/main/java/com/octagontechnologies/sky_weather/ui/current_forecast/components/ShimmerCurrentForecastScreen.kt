package com.octagontechnologies.sky_weather.ui.current_forecast.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.octagontechnologies.sky_weather.ui.compose.theme.AppTheme
import com.octagontechnologies.sky_weather.ui.compose.theme.LocalAppColors
import com.octagontechnologies.sky_weather.utils.BASE_SHIMMER_COLOR
import com.valentinilk.shimmer.shimmer

@Composable
fun ShimmerCurrentForecastScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.shimmer().fillMaxSize()) {
        Box(
            modifier =
                Modifier
                    .padding(top = 16.dp)
                    .height(250.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .align(Alignment.CenterHorizontally)
                    .background(
                        BASE_SHIMMER_COLOR,
                    ),
        )

        // Boxes for Sunlight and Rain Probability
        Row(
            Modifier.padding(horizontal = 8.dp).padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            (1..2).forEach { _ ->
                Box(
                    modifier =
                        Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(BASE_SHIMMER_COLOR)
                            .height(100.dp),
                )
            }
        }

        // Box for Current Conditions
        Box(
            Modifier
                .padding(vertical = 32.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(BASE_SHIMMER_COLOR)
                .height(28.dp)
                .width(150.dp)
                .align(Alignment.CenterHorizontally),
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(8) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(BASE_SHIMMER_COLOR.copy(alpha = 0.2f))
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(Modifier.height(24.dp).width(160.dp).clip(RoundedCornerShape(6.dp)).background(BASE_SHIMMER_COLOR))

                    Box(Modifier.height(24.dp).width(60.dp).clip(RoundedCornerShape(6.dp)).background(BASE_SHIMMER_COLOR))
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewShimmerCurrentForecastScreen() =
    AppTheme {
        Column(modifier = Modifier.background(LocalAppColors.current.background).fillMaxSize()) {
            ShimmerCurrentForecastScreen()
        }
    }
