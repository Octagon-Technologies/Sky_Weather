package com.octagontechnologies.sky_weather.ui.daily_forecast.selected_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.octagontechnologies.sky_weather.R
import com.octagontechnologies.sky_weather.domain.Lunar
import com.octagontechnologies.sky_weather.domain.daily.DailyForecast
import com.octagontechnologies.sky_weather.ui.compose.shared_screens.SelectedForecastScreen
import com.octagontechnologies.sky_weather.ui.compose.theme.AppTheme
import com.octagontechnologies.sky_weather.ui.compose.theme.DarkBlue
import com.octagontechnologies.sky_weather.ui.compose.theme.DarkOrange
import com.octagontechnologies.sky_weather.ui.compose.theme.LocalAppColors
import com.octagontechnologies.sky_weather.ui.compose.theme.QuickSand
import com.octagontechnologies.sky_weather.utils.Units
import com.octagontechnologies.sky_weather.utils.WindDirectionUnits
import com.octagontechnologies.sky_weather.utils.getDayWithMonth
import com.octagontechnologies.sky_weather.utils.weather.getWeatherConditions
import timber.log.Timber


@Composable
fun DailySelectedTab(
    units: Units?,
    windDirectionUnits: WindDirectionUnits?,
    selectedDailyForecast: DailyForecast?,
    selectedLunarForecast: Lunar?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
            .background(LocalAppColors.current.surface)
            .padding(bottom = 12.dp)
            .verticalScroll(rememberScrollState())
    ) {
        HorizontalDivider(
            Modifier
                .fillMaxWidth(0.2f)
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp, bottom = 4.dp)
                .clip(RoundedCornerShape(100)),
            thickness = (3).dp,
            color = LocalAppColors.current.onSurfaceLighter
        )

        Box(Modifier.fillMaxWidth()) {
            Text(
                text = selectedDailyForecast?.timeInEpochSeconds?.getDayWithMonth() ?: "----",
                fontFamily = QuickSand,
                fontSize = 17.sp,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
                    .align(Alignment.Center),
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = units?.getUnitSymbol() ?: "C",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(Alignment.CenterEnd)
            )
        }

        var selectedPageIndex by remember { mutableIntStateOf(0) }
        val pagerState = rememberPagerState(pageCount = { 2 })


        val tabs = listOf("Day", "Night")
        TabRow(
            containerColor = LocalAppColors.current.surface,
            contentColor = LocalAppColors.current.onSurface,
            selectedTabIndex = selectedPageIndex,
            divider = {},
            indicator = { tabPositions ->
                if (selectedPageIndex < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier
                            .tabIndicatorOffset(tabPositions[selectedPageIndex])
                            .padding(top = 8.dp),
                        color = DarkBlue
                    )
                }
            }
        ) {
            tabs.forEachIndexed { tabIndex, tabTitle ->
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 12.dp)
                        .weight(1f)
                        .clickable { selectedPageIndex = tabIndex },
                    text = tabTitle,
                    textAlign = TextAlign.Center
                )
            }
        }

        LaunchedEffect(key1 = selectedPageIndex) {
            pagerState.animateScrollToPage(selectedPageIndex)
        }

        LaunchedEffect(key1 = pagerState.currentPage, pagerState.isScrollInProgress) {
            if (!pagerState.isScrollInProgress)
                selectedPageIndex = pagerState.currentPage
        }

        HorizontalPager(state = pagerState) { pageIndex ->
            val isDayTab = pageIndex == 0

            if (isDayTab)
                SelectedForecastScreen(
                    modifier = Modifier.padding(top = 12.dp),
                    conditions = selectedDailyForecast?.dayTime?.getWeatherConditions(
                        units,
                        windDirectionUnits
                    ) ?: mapOf(),
                    weatherCode = selectedDailyForecast?.dayTime?.weatherCode,
                    temp = selectedDailyForecast?.getTempHigh(units),
                    units = units ?: Units.getDefault()
                )
            else
                SelectedForecastScreen(
                    modifier = Modifier.padding(top = 12.dp),
                    conditions = selectedDailyForecast?.nightTime?.getWeatherConditions(
                        units,
                        windDirectionUnits
                    ) ?: mapOf(),
                    weatherCode = selectedDailyForecast?.nightTime?.weatherCode,
                    temp = selectedDailyForecast?.getTempLow(units),
                    units = units ?: Units.getDefault()
                )
        }

        selectedLunarForecast?.let {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(LocalAppColors.current.surface)
                    .padding(horizontal = 8.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LaunchedEffect(key1 = Unit) {
                    Timber.d("selectedLunarForecast.sunRise is ${selectedLunarForecast.sunRise}")
                    Timber.d("selectedLunarForecast.sunSet is ${selectedLunarForecast.sunSet}")
                }

                LunarPreview(
                    true,
                    rise = selectedLunarForecast.sunRise,
                    set = selectedLunarForecast.sunSet,
                    modifier = Modifier.weight(1f)
                )
                LunarPreview(
                    false,
                    rise = selectedLunarForecast.moonRise,
                    set = selectedLunarForecast.moonSet,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun LunarPreview(
    isSunPreview: Boolean,
    rise: String?,
    set: String?,
    modifier: Modifier = Modifier,
    tabColor: Color = LocalAppColors.current.surface,
    tabSecondaryColor: Color = LocalAppColors.current.surfaceVariant.copy(alpha = 0.3f),
    onTabColor: Color = LocalAppColors.current.onSurface
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = tabColor, contentColor = onTabColor),
        modifier = modifier,
        elevation = CardDefaults.cardElevation((1).dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(Modifier.padding(top = 8.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Image(
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .size(44.dp)
                        .padding(4.dp),
                    painter = painterResource(id = if (isSunPreview) R.drawable.yellow_sun else R.drawable.moon),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(if (isSunPreview) DarkOrange else LocalAppColors.current.onSurface)
                )


                Text(
                    text = if (isSunPreview) "Sunlight" else "Moonlight",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 6.dp)
                )

//                Column {
//                    Text(text = "${getHoursInterval(lunar.rise, lunar.set)} hrs")
//                    Text(text = "${getMinsInterval(lunar.rise, lunar.set)} mins")
//                }
            }


            Surface(
                modifier = Modifier.padding(top = 12.dp),
                shape = RoundedCornerShape(8.dp), // (bottomEnd = 8.dp, bottomStart = 8.dp, topStart = 15.dp, topEnd = 15.dp),
                color = tabSecondaryColor
            ) {
                Column(Modifier.padding(vertical = 6.dp, horizontal = 2.dp)) {
                    val mapOf = mapOf("Rise" to rise, "Set" to set)
                    mapOf.forEach { (type, time) ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp, horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = type, fontWeight = FontWeight.Medium)

                            Text(text = time ?: "--:--")
//                            Text(text = time.getHoursAndMins(TimeFormat.HALF_DAY))
                        }
                    }
                }
            }
        }
    }
}

//
@Preview
@Composable
private fun PreviewLunarPreview() = AppTheme {
    Row(
        Modifier
            .fillMaxWidth()
            .background(LocalAppColors.current.surface)
            .padding(horizontal = 8.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LunarPreview(
            isSunPreview = true,
            rise = "6:13",
            set = "18:21",
            modifier = Modifier.weight(1f)
        )
        LunarPreview(
            isSunPreview = false,
            rise = "1:55",
            set = "12:45",
            modifier = Modifier.weight(1f)
        )
    }
}


@Preview
@Composable
private fun PreviewDailySelectedTab() = AppTheme {
    DailySelectedTab(
        Units.getDefault(),
        WindDirectionUnits.getDefault(),
        DailyForecast.TEST_FORE,
        selectedLunarForecast = Lunar("6:34", "18:45", "1:55", "12:56")
    )
}