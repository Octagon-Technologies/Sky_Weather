package com.octagontechnologies.sky_weather.ui.current_forecast.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.octagontechnologies.sky_weather.R
import com.octagontechnologies.sky_weather.domain.Location
import com.octagontechnologies.sky_weather.main_activity.Screens
import com.octagontechnologies.sky_weather.ui.compose.theme.AppTheme
import com.octagontechnologies.sky_weather.ui.compose.theme.LocalAppColors

@Composable
fun CurrentTopBar(location: Location?, navController: NavController, modifier: Modifier = Modifier) {
    Card(
        modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = LocalAppColors.current.background,//.copy(alpha = 0.1f),
            contentColor = LocalAppColors.current.onBackground
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            IconButton(
                onClick = {
                    navController.navigate(Screens.Settings)
                },
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = stringResource(R.string.open_settings)
                )
            }

            Surface(
                onClick = { navController.navigate(Screens.SelectLocation) },
                modifier = Modifier
//                    .fillMaxWidth(0.65f)
                    .align(Alignment.Center),
                color = LocalAppColors.current.backgroundVariant,
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = location?.displayNameWithoutCountryCode ?: "----",
//                        text = "Karen Hardy, Karen ward, Nairobi, Nairobi County, 00561",
                        lineHeight = 17.sp,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .widthIn(max = 230.dp)
                    )
                    Image(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(LocalAppColors.current.onBackground),
                        modifier = Modifier
                            .size(22.dp)
                            .padding(start = 4.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun PreviewCurrentTopBar() = AppTheme {
    CurrentTopBar(null, rememberNavController())
}


enum class BottomNav(@StringRes val nameRes: Int, @DrawableRes val icon: Int) {
    Current(R.string.current, R.drawable.radar),
    Hourly(R.string.hourly, R.drawable.clock),
    Daily(R.string.daily, R.drawable.daily_calender)
}

fun BottomNav.getScreen() = when(this) {
    BottomNav.Current -> Screens.Current
    BottomNav.Hourly -> Screens.Hourly
    BottomNav.Daily -> Screens.Daily
}

