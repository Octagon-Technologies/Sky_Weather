package com.octagontechnologies.sky_weather.feature.forecast.presentation.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.octagontechnologies.sky_weather.core.common.time.getDay
import com.octagontechnologies.sky_weather.core.designsystem.components.OnBackgroundAsContentColor
import com.octagontechnologies.sky_weather.core.designsystem.theme.LocalAppColors
import com.octagontechnologies.sky_weather.core.model.preferences.TimeFormat
import com.octagontechnologies.sky_weather.core.model.preferences.Units
import com.octagontechnologies.sky_weather.core.model.preferences.WindDirectionUnits
import com.octagontechnologies.sky_weather.core.ui.system.LocalSystemBarsColorOverrides
import com.octagontechnologies.sky_weather.feature.forecast.presentation.list.components.HourlyForecastCard
import com.octagontechnologies.sky_weather.feature.forecast.presentation.list.components.ShimmerHourlyForecastCard
import com.octagontechnologies.sky_weather.feature.forecast.presentation.selected_details.HourlySelectedTab
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HourlyForecastScreen(
    coroutineScope: CoroutineScope,
    modifier: Modifier = Modifier,
    viewModel: HourlyForecastViewModel = hiltViewModel(),
    showBottomNavView: (Boolean) -> Unit,
) {
    OnBackgroundAsContentColor {
        Column(
            modifier
                .fillMaxSize()
                .background(LocalAppColors.current.background),
        ) {
            val listOfHourlyForecast by viewModel.listOfHourlyForecast.observeAsState()

            val selectedHourlyForecast by viewModel.selectedHourlyForecast.observeAsState()

            val units by viewModel.units.observeAsState(initial = Units.getDefault())
            val timeFormat by viewModel.timeFormat.observeAsState(initial = TimeFormat.getDefault())
            val windDirectionUnits by viewModel.windDirectionUnits.observeAsState(initial = WindDirectionUnits.getDefault())

            val peekHeight = remember { 64.dp }
            val sheetState = rememberBottomSheetScaffoldState()

            val systemBarsOverrides = LocalSystemBarsColorOverrides.current
            val blueBackground = LocalAppColors.current.background
            val bottomSheetBackground = LocalAppColors.current.surface

            val scrollState = rememberScrollState()

            LaunchedEffect(key1 = sheetState.bottomSheetState.currentValue) {
                val isExpanded = sheetState.bottomSheetState.currentValue == SheetValue.Expanded
                showBottomNavView(!isExpanded)

                if (!isExpanded) {
                    scrollState.animateScrollTo(0)
                }

                systemBarsOverrides?.setNavigationBarColor(
                    if (isExpanded) bottomSheetBackground else blueBackground,
                )
            }

            // Restore the navigation bar color to the default when leaving this screen
            DisposableEffect(Unit) {
                onDispose {
                    systemBarsOverrides?.setNavigationBarColor(blueBackground)
                }
            }

            // If back button is pressed while the bottom sheet is expanded, hide it
            BackHandler(enabled = sheetState.bottomSheetState.currentValue == SheetValue.Expanded) {
                coroutineScope.launch { sheetState.bottomSheetState.partialExpand() }
            }

            BottomSheetScaffold(
                scaffoldState = sheetState,
                sheetPeekHeight = peekHeight,
                sheetContent = {
                    HourlySelectedTab(
                        units = units,
                        windDirectionUnits = windDirectionUnits,
                        timeFormat = timeFormat,
                        selectedForecast = selectedHourlyForecast,
                        sheetState = scrollState,
                    )
                },
                sheetDragHandle = null,
            ) {
                if (listOfHourlyForecast.isNullOrEmpty()) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .background(LocalAppColors.current.background),
                    ) {
                        Text(
                            "Hourly Forecasts",
                            style = MaterialTheme.typography.titleMedium,
                            modifier =
                                Modifier
                                    .padding(vertical = 18.dp)
                                    .align(Alignment.CenterHorizontally),
                        )

                        LazyColumn(
                            modifier =
                                Modifier
                                    .background(LocalAppColors.current.background)
                                    .padding(top = 8.dp)
                                    .padding(horizontal = 8.dp)
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .shimmer(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(10) {
                                ShimmerHourlyForecastCard(
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }
                    }
                } else {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .background(LocalAppColors.current.background)
                                .padding(horizontal = 8.dp)
                                .padding(bottom = peekHeight),
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 18.dp, bottom = 16.dp),
                        ) {
                            val titleDay by viewModel.titleDay.collectAsState()

                            Text(
                                text = titleDay,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(horizontal = 8.dp),
                            )

                            Surface(
                                modifier =
                                    Modifier
                                        .align(Alignment.CenterEnd)
                                        .padding(end = 4.dp),
                                color = LocalAppColors.current.onBackground.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp),
                                contentColor = LocalAppColors.current.onBackground,
                            ) {
                                Text(
                                    text = units.getUnitSymbol(),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            }
                        }

                        val lazyColumnState = rememberLazyListState()
                        val firstVisibleIndex by remember { derivedStateOf { lazyColumnState.firstVisibleItemIndex } }

                        LaunchedEffect(key1 = firstVisibleIndex) {
                            val timeInEpochMillis =
                                listOfHourlyForecast?.getOrNull(firstVisibleIndex)?.timeInEpochMillis
                            viewModel.updateTitleDay(timeInEpochMillis.getDay())
                        }

                        LazyColumn(
                            state = lazyColumnState,
                            modifier =
                                Modifier
                                    .padding(bottom = (0.5).dp)
                                    .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            items(listOfHourlyForecast!!) { hourlyForecast ->
                                HourlyForecastCard(
                                    hourlyForecast = hourlyForecast,
                                    units = units,
                                    timeFormat = timeFormat,
                                    selectHourlyForecast = {
                                        viewModel.selectHourlyForecast(hourlyForecast)

                                        coroutineScope.launch {
                                            sheetState.bottomSheetState.expand()
                                        }
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
