package com.octagontechnologies.sky_weather.ui.daily_forecast.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.octagontechnologies.sky_weather.ui.compose.getActivity
import com.octagontechnologies.sky_weather.ui.compose.theme.AppTheme
import com.octagontechnologies.sky_weather.ui.compose.theme.LocalAppColors
import com.octagontechnologies.sky_weather.ui.compose.theme.Poppins
import com.octagontechnologies.sky_weather.ui.daily_forecast.list.components.DailyForecastCard
import com.octagontechnologies.sky_weather.ui.daily_forecast.selected_details.DailySelectedTab
import com.octagontechnologies.sky_weather.utils.Units
import com.octagontechnologies.sky_weather.utils.WindDirectionUnits
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyForecastScreen(coroutineScope: CoroutineScope, showBottomNavView: (Boolean) -> Unit) {

    val viewModel = hiltViewModel<DailyForecastViewModel>()
    val listOfDailyForecast by viewModel.listOfDailyForecast.observeAsState()

    val selectedDailyForecast by viewModel.selectedDailyForecast.observeAsState()
    val selectedLunarForecast by viewModel.selectedLunarForecast.observeAsState()

    val units by viewModel.units.observeAsState(initial = Units.getDefault())
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
        coroutineScope.launch {
            sheetState.bottomSheetState.partialExpand()
        }
    }

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetPeekHeight = peekHeight,
        sheetContent = {
            DailySelectedTab(
                units = units,
                windDirectionUnits = windDirectionUnits,
                selectedDailyForecast = selectedDailyForecast,
                selectedLunarForecast = selectedLunarForecast
            )
        },
        sheetDragHandle = null
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(LocalAppColors.current.background)
                .padding(horizontal = 8.dp)
                .padding(bottom = peekHeight)
                .padding(bottom = 6.dp)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = viewModel.currentMonth,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(vertical = 8.dp),
                    fontSize = 19.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LocalAppColors.current.onBackground,
                    style = MaterialTheme.typography.bodyMedium
                )

                Surface(
                    modifier = Modifier
                        .align(Alignment.CenterEnd),
                    color = LocalAppColors.current.backgroundVariant,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = units.getUnitSymbol(),
                        fontSize = 17.sp,
                        fontFamily = Poppins,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                        color = LocalAppColors.current.onBackground
                    )
                }
            }


            if (listOfDailyForecast.isNullOrEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(36.dp),
                        trackColor = Color.Transparent,
                        color = Color.White
                    )
                }
            } else {
                LazyVerticalGrid(
                    GridCells.Fixed(2),
                    Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                            }
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun PreviewDailyForecastScreen() = AppTheme {
    DailyForecastScreen(rememberCoroutineScope(), {})
}