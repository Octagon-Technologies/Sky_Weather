package com.octagontechnologies.sky_weather.ui.settings.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.octagontechnologies.sky_weather.R
import com.octagontechnologies.sky_weather.ui.compose.theme.LightBlack
import com.octagontechnologies.sky_weather.ui.compose.theme.LocalAppColors
import com.octagontechnologies.sky_weather.ui.compose.theme.Poppins
import com.octagontechnologies.sky_weather.ui.compose.theme.QuickSand
import com.octagontechnologies.sky_weather.utils.Theme
import com.octagontechnologies.sky_weather.utils.TimeFormat
import com.octagontechnologies.sky_weather.utils.Units
import com.octagontechnologies.sky_weather.utils.WindDirectionUnits

data class Option<T>(@StringRes val name: Int, @StringRes val description: Int?, val type: T) {
    companion object {
        fun getUnitImperial() =
            Option(R.string.imperial_plain_text, R.string.f_mph_in_plain_text, Units.IMPERIAL)

        fun getUnitMetric() =
            Option(R.string.metric_plain_text, R.string.c_kph_mm_plain_text, Units.METRIC)

        fun getWindCardinal() = Option(
            R.string.cardinal_plain_text,
            R.string.n_e_s_w_plain_text,
            WindDirectionUnits.CARDINAL
        )

        fun getWindDegrees() = Option(
            R.string.degrees_plain_text,
            R.string._0_360_plain_text,
            WindDirectionUnits.DEGREES
        )

        fun getTime12Hour() = Option(
            R.string._12_hour_plain_text,
            R.string._2_pm_3_pm_etc_plain_text,
            TimeFormat.HALF_DAY
        )

        fun getTime24Hour() = Option(
            R.string._24_hour_plain_text,
            R.string._12_00_13_00_etc_plain_text,
            TimeFormat.FULL_DAY
        )

        fun getLightMode() = Option(R.string.light_plain_text, null, Theme.LIGHT)
        fun getDarkMode() = Option(R.string.dark_plain_text, null, Theme.DARK)
    }
}

data class DuoOption<T>(@StringRes val title: Int, val first: Option<T>, val second: Option<T>) {
    companion object {
        val units =
            DuoOption(R.string.units_plain_text, Option.getUnitImperial(), Option.getUnitMetric())
        val wind = DuoOption(
            R.string.wind_direction_plain_text,
            Option.getWindCardinal(),
            Option.getWindDegrees()
        )
        val time = DuoOption(
            R.string.time_format_plain_text,
            Option.getTime12Hour(),
            Option.getTime24Hour()
        )
        val theme =
            DuoOption(R.string.display_mode_plain_text, Option.getLightMode(), Option.getDarkMode())
    }
}

@Composable
fun <T> SettingsOption(
    selectedOption: T,
    duoOption: DuoOption<T>,
    changeOption: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    val rounded = RoundedCornerShape(percent = 100)

    Column {
        Text(
            text = stringResource(id = duoOption.title),
            fontSize = 15.sp,
            fontFamily = Poppins,
            color = LocalAppColors.current.onSurface
        )

        Row(
            modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .clip(rounded)
                .border(1.dp, LightBlack, rounded)
        ) {

            OptionItem(
                selectedOption = selectedOption, option = duoOption.first,
                onOptionSelected = changeOption, modifier = Modifier.weight(1f)
            )

            OptionItem(
                selectedOption = selectedOption, option = duoOption.second,
                onOptionSelected = changeOption, modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun <T> OptionItem(
    selectedOption: T,
    option: Option<T>,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor =
        if (selectedOption == option.type) LocalAppColors.current.onSurfaceLighter else LocalAppColors.current.surface
    val textColor =
        if (selectedOption == option.type) LocalAppColors.current.surface else LocalAppColors.current.onSurface

    Column(
        modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable { onOptionSelected(option.type) }
            .padding(vertical = if (option.description == null) 20.dp else 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = option.name),
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            fontFamily = QuickSand,
            fontSize = 16.sp
        )

        if (option.description != null) {
            Text(
                text = stringResource(id = option.description),
                modifier = Modifier.padding(top = 2.dp),
                fontWeight = FontWeight.Medium,
                color = textColor,
                fontFamily = QuickSand,
                fontSize = 15.sp
            )
        }
    }
}