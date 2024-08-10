package com.octagontechnologies.sky_weather.ui.hourly_forecast.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.octagontechnologies.sky_weather.ui.compose.getActivity
import com.octagontechnologies.sky_weather.ui.compose.theme.CabinSemiCondensed
import com.octagontechnologies.sky_weather.ui.compose.theme.LocalAppColors
import com.octagontechnologies.sky_weather.ui.hourly_forecast.list.components.HourlyForecastCard
import com.octagontechnologies.sky_weather.ui.hourly_forecast.selected_details.HourlySelectedTab
import com.octagontechnologies.sky_weather.utils.TimeFormat
import com.octagontechnologies.sky_weather.utils.Units
import com.octagontechnologies.sky_weather.utils.WindDirectionUnits
import com.octagontechnologies.sky_weather.utils.getDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HourlyForecastScreen(
    coroutineScope: CoroutineScope,
    showBottomNavView: (Boolean) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(LocalAppColors.current.background)
    ) {
        val viewModel = hiltViewModel<HourlyForecastViewModel>()
        val listOfHourlyForecast by viewModel.listOfHourlyForecast.observeAsState()

        val selectedHourlyForecast by viewModel.selectedHourlyForecast.observeAsState()

        val units by viewModel.units.observeAsState(initial = Units.getDefault())
        val timeFormat by viewModel.timeFormat.observeAsState(initial = TimeFormat.getDefault())
        val windDirectionUnits by viewModel.windDirectionUnits.observeAsState(initial = WindDirectionUnits.getDefault())

        val peekHeight = remember { 64.dp }
        val sheetState = rememberBottomSheetScaffoldState()

        val view = LocalView.current
        val window = (view.context.getActivity() ?: return).window


        val blueBackground = LocalAppColors.current.background
        val bottomSheetBackground = LocalAppColors.current.surface

        val scrollState = rememberScrollState()


        LaunchedEffect(key1 = sheetState.bottomSheetState.currentValue) {
            val isExpanded = sheetState.bottomSheetState.currentValue == SheetValue.Expanded
            showBottomNavView(!isExpanded)

            // It's being collapsed; scroll to the top bar of the dialog
            if (!isExpanded)
                scrollState.animateScrollTo(0)


            window.navigationBarColor =
                (if (isExpanded) bottomSheetBackground else blueBackground).toArgb()
        }

        // If back button is pressed if the bottom sheet is expanded, hide it
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
                    selectedForecast = selectedHourlyForecast
                )
            },
            sheetDragHandle = null
        ) {
            if (listOfHourlyForecast.isNullOrEmpty()) {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(36.dp)
                            .align(Alignment.Center)
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(LocalAppColors.current.background)
                        .padding(horizontal = 8.dp)
                        .padding(bottom = peekHeight)
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 18.dp, bottom = 16.dp)
                    ) {
                        val titleDay by viewModel.titleDay.collectAsState()

                        Text(
                            text = titleDay,
                            color = LocalAppColors.current.onBackground,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            fontSize = 19.sp
                        )

                        Surface(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 4.dp),
                            color = LocalAppColors.current.onBackground.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = units.getUnitSymbol(),
                                color = LocalAppColors.current.onBackground,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                fontFamily = CabinSemiCondensed,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }


                    val lazyColumnState = rememberLazyListState()
                    val firstVisibleIndex by remember { derivedStateOf { lazyColumnState.firstVisibleItemIndex } }

                    LaunchedEffect(key1 = firstVisibleIndex) {
                        val timeInEpochMillis = listOfHourlyForecast?.getOrNull(firstVisibleIndex)?.timeInEpochMillis
                        viewModel.updateTitleDay(timeInEpochMillis.getDay())
                    }

                    LazyColumn(
                        state = lazyColumnState,
                        modifier = Modifier
                            .padding(bottom = (0.5).dp)
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
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
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}