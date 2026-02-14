package com.octagontechnologies.sky_weather.feature.forecast.presentation.selected_details

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.octagontechnologies.sky_weather.core.common.time.getHoursAndMinsWithDay
import com.octagontechnologies.sky_weather.core.designsystem.theme.LocalAppColors
import com.octagontechnologies.sky_weather.core.designsystem.theme.QuickSand
import com.octagontechnologies.sky_weather.core.model.preferences.TimeFormat
import com.octagontechnologies.sky_weather.core.model.preferences.Units
import com.octagontechnologies.sky_weather.core.model.preferences.WindDirectionUnits
import com.octagontechnologies.sky_weather.domain.model.SingleForecast
import com.octagontechnologies.sky_weather.feature.current.presentation.formatter.getAdvancedWeatherConditions
import com.octagontechnologies.sky_weather.feature.current.presentation.formatter.getCoreWeatherConditions
import com.octagontechnologies.sky_weather.feature.forecast.components.SelectedForecastScreen
import kotlinx.collections.immutable.toPersistentMap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HourlySelectedTab(
    sheetState: ScrollState,
    units: Units?,
    windDirectionUnits: WindDirectionUnits?,
    timeFormat: TimeFormat?,
    selectedForecast: SingleForecast?,
    modifier: Modifier = Modifier,
) {
    val conditions =
        remember(key1 = selectedForecast) {
            selectedForecast
                ?.getCoreWeatherConditions(units, windDirectionUnits)
                ?.plus(selectedForecast.getAdvancedWeatherConditions(units)) ?: mapOf()
        }

    Column(
        modifier
            .fillMaxWidth()
            .background(LocalAppColors.current.surface)
            .padding(bottom = 12.dp),
    ) {
        HorizontalDivider(
            Modifier
                .fillMaxWidth(0.2f)
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp, bottom = 4.dp)
                .clip(RoundedCornerShape(100)),
            thickness = (3).dp,
            color = LocalAppColors.current.onSurfaceLighter,
        )

        Box(Modifier.fillMaxWidth()) {
            Text(
                text =
                    selectedForecast?.timeInEpochMillis?.getHoursAndMinsWithDay(timeFormat)
                        ?: "----",
                fontFamily = QuickSand,
                fontSize = 17.sp,
                color = LocalAppColors.current.onSurface,
                modifier =
                    Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .align(Alignment.Center),
                fontWeight = FontWeight.SemiBold,
            )

            Text(
                text = units?.getUnitSymbol() ?: "C",
                fontWeight = FontWeight.SemiBold,
                color = LocalAppColors.current.onSurfaceLighter,
                modifier =
                    Modifier
                        .padding(end = 12.dp)
                        .align(Alignment.CenterEnd),
            )
        }

        SelectedForecastScreen(
            modifier = Modifier.padding(top = 12.dp),
            conditions = conditions.toPersistentMap(),
            weatherCode = selectedForecast?.weatherCode,
            temp = selectedForecast?.temp?.toInt(),
            units = units ?: Units.getDefault(),
            sheetState = sheetState,
        )
    }
}
