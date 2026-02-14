package com.octagontechnologies.sky_weather.feature.forecast.presentation.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.octagontechnologies.sky_weather.core.designsystem.theme.AppTheme
import com.octagontechnologies.sky_weather.core.ui.constants.BASE_SHIMMER_COLOR
import com.valentinilk.shimmer.shimmer

@Composable
fun ShimmerDailyForecastScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.shimmer(),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(10) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                BASE_SHIMMER_COLOR,
                            ),
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewShimmerDailyForecastScreen() =
    AppTheme {
        ShimmerDailyForecastScreen()
    }
