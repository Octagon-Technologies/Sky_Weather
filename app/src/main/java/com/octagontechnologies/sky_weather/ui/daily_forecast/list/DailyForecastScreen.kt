package com.octagontechnologies.sky_weather.ui.daily_forecast.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.octagontechnologies.sky_weather.ui.compose.LocalSystemBarsColorOverrides
import com.octagontechnologies.sky_weather.ui.compose.theme.AppTheme
import com.octagontechnologies.sky_weather.ui.compose.theme.LocalAppColors
import com.octagontechnologies.sky_weather.ui.compose.theme.OnBackgroundAsContentColor
import com.octagontechnologies.sky_weather.ui.daily_forecast.list.components.DailyForecastCard
import com.octagontechnologies.sky_weather.ui.daily_forecast.list.components.ShimmerDailyForecastScreen
import com.octagontechnologies.sky_weather.ui.daily_forecast.selected_details.DailySelectedTab
import com.octagontechnologies.sky_weather.utils.Units
import com.octagontechnologies.sky_weather.utils.WindDirectionUnits
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyForecastScreen(
    coroutineScope: CoroutineScope,
    showBottomNavView: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DailyForecastViewModel = hiltViewModel(),
) {
    val listOfDailyForecast by viewModel.listOfDailyForecast.observeAsState()

    val selectedDailyForecast by viewModel.selectedDailyForecast.observeAsState()
    val selectedLunarForecast by viewModel.selectedLunarForecast.observeAsState()

    val units by viewModel.units.observeAsState(initial = Units.getDefault())
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

        // It's being collapsed; scroll to the top bar of the dialog
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
            systemBarsOverrides?.setStatusBarColor(blueBackground)
            systemBarsOverrides?.setNavigationBarColor(blueBackground)
        }
    }

    // If back button is pressed while the bottom sheet is expanded, hide it
    BackHandler(enabled = sheetState.bottomSheetState.currentValue == SheetValue.Expanded) {
        coroutineScope.launch {
            sheetState.bottomSheetState.partialExpand()
        }
    }

    OnBackgroundAsContentColor {
        BottomSheetScaffold(
            modifier = modifier.navigationBarsPadding(),
            scaffoldState = sheetState,
            sheetPeekHeight = peekHeight,
            sheetContent = {
                DailySelectedTab(
                    sheetState = scrollState,
                    units = units,
                    windDirectionUnits = windDirectionUnits,
                    selectedDailyForecast = selectedDailyForecast,
                    selectedLunarForecast = selectedLunarForecast,
                )
            },
            sheetDragHandle = null,
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(LocalAppColors.current.background)
                    .padding(horizontal = 8.dp)
                    .padding(bottom = peekHeight)
                    .padding(bottom = 6.dp),
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                ) {
                    Text(
                        text = viewModel.currentMonth,
                        modifier =
                            Modifier
                                .align(Alignment.Center)
                                .padding(vertical = 8.dp),
                        style = MaterialTheme.typography.titleMedium,
                    )

                    Surface(
                        modifier =
                            Modifier
                                .align(Alignment.CenterEnd),
                        color = LocalAppColors.current.backgroundVariant,
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text(
                            text = units.getUnitSymbol(),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }

                if (listOfDailyForecast.isNullOrEmpty()) {
                    ShimmerDailyForecastScreen()
                } else {
                    LazyVerticalGrid(
                        GridCells.Fixed(2),
                        Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        items(listOfDailyForecast!!) { forecast ->
                            DailyForecastCard(
                                forecast = forecast,
                                units = units,
                                onForecastClick = {
                                    coroutineScope.launch {
                                        sheetState.bottomSheetState.expand()
                                    }
                                    viewModel.selectDailyForecast(forecast)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewDailyForecastScreen() =
    AppTheme {
        DailyForecastScreen(rememberCoroutineScope(), viewModel = hiltViewModel<DailyForecastViewModel>(), showBottomNavView = {})
    }
