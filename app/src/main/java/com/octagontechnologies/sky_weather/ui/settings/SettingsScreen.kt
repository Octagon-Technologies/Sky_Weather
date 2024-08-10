package com.octagontechnologies.sky_weather.ui.settings

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.octagontechnologies.sky_weather.R
import com.octagontechnologies.sky_weather.ui.compose.ChangeStatusBars
import com.octagontechnologies.sky_weather.ui.compose.theme.AppRipple
import com.octagontechnologies.sky_weather.ui.compose.theme.AppTheme
import com.octagontechnologies.sky_weather.ui.compose.theme.LightBlack
import com.octagontechnologies.sky_weather.ui.compose.theme.LightBlue
import com.octagontechnologies.sky_weather.ui.compose.theme.LocalAppColors
import com.octagontechnologies.sky_weather.ui.compose.theme.QuickSand
import com.octagontechnologies.sky_weather.ui.settings.components.MultiOption
import com.octagontechnologies.sky_weather.ui.settings.components.SettingsOption
import com.octagontechnologies.sky_weather.utils.Theme
import com.octagontechnologies.sky_weather.utils.TimeFormat
import com.octagontechnologies.sky_weather.utils.Units
import com.octagontechnologies.sky_weather.utils.WindDirectionUnits

@Composable
fun SettingsScreen(navController: NavController) {
    val viewModel = hiltViewModel<SettingsViewModel>()

    val units by viewModel.units.observeAsState(Units.METRIC)
    val windDirectionUnits by viewModel.windDirectionUnits.observeAsState(WindDirectionUnits.DEGREES)
    val timeFormat by viewModel.timeFormat.observeAsState(TimeFormat.HALF_DAY)
    val theme by viewModel.theme.collectAsState(Theme.LIGHT)

    val areNotificationsOn by viewModel.isNotificationAllowed.collectAsState(initial = false)


    var navigateBack by remember { mutableStateOf(false) }


    ChangeStatusBars(
        navigateBack = navigateBack,
        onNavigateBack = { navController.popBackStack() },
        resetNavigateBack = { navigateBack = false }
    )


    Column(
        Modifier
            .fillMaxSize()
            .background(LocalAppColors.current.surface)
    ) {
        Card(
            Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp),
            colors = CardDefaults.cardColors(containerColor = LocalAppColors.current.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                CompositionLocalProvider(LocalRippleTheme provides AppRipple) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        contentDescription = stringResource(id = R.string.back_button),
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .clickable {
                                navigateBack = true
                            }
                            .padding(2.dp)
                    )
                }

                Text(
                    text = stringResource(id = R.string.settings_plain_text),
                    modifier = Modifier.align(Alignment.Center),
                    fontFamily = QuickSand,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }


        Column(
            verticalArrangement = Arrangement.spacedBy(space = 24.dp),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 16.dp)
        ) {
            SettingsOption(
                selectedOption = units,
                multiOption = MultiOption.units,
                changeOption = { viewModel.changeUnits(it) })
            SettingsOption(
                selectedOption = windDirectionUnits,
                multiOption = MultiOption.wind,
                changeOption = { viewModel.changeWindDirections(it) })
            SettingsOption(
                selectedOption = timeFormat,
                multiOption = MultiOption.time,
                changeOption = { viewModel.changeTimeFormat(it) })
            SettingsOption(
                selectedOption = theme,
                multiOption = MultiOption.theme,
                changeOption = { viewModel.changeTheme(it) })
        }

        HorizontalDivider(
            Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height((0.5).dp)
                .background(LocalAppColors.current.onSurfaceLighter)
        )

        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()) { isGranted ->
            viewModel.handleNotificationPermissionResponse(isGranted = isGranted)
        }

        val showSystemPermissionDialog by viewModel.showSystemPermissionDialog.collectAsState(false)
        LaunchedEffect(key1 = showSystemPermissionDialog) {
            if (showSystemPermissionDialog)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }

            viewModel.resetSystemPermissionDialog()
        }


        Row(
            Modifier
                .padding(top = 24.dp)
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.enable_notifications_plain_text),
                fontFamily = QuickSand,
                fontWeight = FontWeight.Medium,
                color = LocalAppColors.current.onSurface,
                fontSize = 18.sp
            )


            Switch(
                checked = areNotificationsOn,
                onCheckedChange = { turnOn ->
                    viewModel.toggleNotificationAllowed(turnOn)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = LightBlack,
                    checkedTrackColor = LightBlue,
                    uncheckedBorderColor = Color.Transparent
                )
            )
        }
    }
}


@Preview
@Composable
private fun PreviewSettingsScreen() = AppTheme {
    SettingsScreen(rememberNavController())
}