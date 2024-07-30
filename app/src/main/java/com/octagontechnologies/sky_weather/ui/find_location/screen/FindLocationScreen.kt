package com.octagontechnologies.sky_weather.ui.find_location.screen

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.octagontechnologies.sky_weather.R
import com.octagontechnologies.sky_weather.domain.Location
import com.octagontechnologies.sky_weather.ui.compose.theme.AdventPro
import com.octagontechnologies.sky_weather.ui.compose.theme.AppTheme
import com.octagontechnologies.sky_weather.ui.compose.theme.LocalAppColors
import com.octagontechnologies.sky_weather.ui.compose.theme.QuickSand
import com.octagontechnologies.sky_weather.ui.find_location.FindLocationViewModel
import com.octagontechnologies.sky_weather.ui.find_location.components.LocationSuggestion
import com.octagontechnologies.sky_weather.ui.find_location.components.SearchTab
import com.octagontechnologies.sky_weather.ui.settings.ChangeStatusBars
import com.octagontechnologies.sky_weather.utils.ErrorType
import com.octagontechnologies.sky_weather.utils.Resource
import com.octagontechnologies.sky_weather.utils.Units
import com.skydoves.cloudy.cloudy
import timber.log.Timber

@Composable
fun FindLocationScreen(navController: NavController, snackbarHostState: SnackbarHostState) {
    val viewModel = viewModel<FindLocationViewModel>()

    val units by viewModel.units.observeAsState()

    var showSearchTab by remember { mutableStateOf(false) }


    val currentLocation by viewModel.currentLocation.collectAsState()
    val isRefreshingCurrentLocation by viewModel.isRefreshingCurrentLocation.collectAsState()

    var navigateBack by remember { mutableStateOf(false) }

    ChangeStatusBars(
        navigateBack = navigateBack,
        navController = navController,
        statusBarColor = LocalAppColors.current.surfaceVariant,
        bottomBarColor = LocalAppColors.current.surface,
        resetNavigateBack = { navigateBack = false }
    )

    Box {
        var showPermissionInfoBar by remember { mutableStateOf(false) }
        var showLocationRequest by remember { mutableStateOf(false) }



        Column(
            Modifier
                .fillMaxSize()
                .background(LocalAppColors.current.surfaceSmallerVariant)
                .padding(bottom = 4.dp)
                .verticalScroll(rememberScrollState())
                .cloudy(radius = if (showPermissionInfoBar) 4 else 0)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = LocalAppColors.current.surfaceVariant),
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
            ) {

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        modifier = Modifier.clickable { navigateBack = true },
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button),
                        tint = LocalAppColors.current.onSurface
                    )

                    Text(
                        text = stringResource(R.string.select_location),
                        style = MaterialTheme.typography.labelMedium,
                        fontSize = 18.sp,
                        color = LocalAppColors.current.onSurface
                    )

                    val unitSymbol = when (units) {
                        null -> ""
                        Units.METRIC -> stringResource(id = R.string.temp_unit_C)
                        Units.IMPERIAL -> stringResource(id = R.string.temp_unit_F)
                    }
                    Text(
                        text = unitSymbol,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 17.sp,
                        fontFamily = QuickSand,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }


                TextButton(
                    onClick = {
                        // TODO: Open Search Tab
                        showSearchTab = true
                    },
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LocalAppColors.current.surface
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.find_location_plain_text),
                        style = MaterialTheme.typography.bodyMedium,
                        color = LocalAppColors.current.onSurface,
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 4.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                }

                Card(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                        if (currentLocation != null) LocalAppColors.current.surface
                        else LocalAppColors.current.surfaceVariant
                    )
                ) {
                    if (currentLocation != null)
                        UseCurrentLocation(
                            location = currentLocation!!,
                            isRefreshingCurrentLocation = isRefreshingCurrentLocation,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            useCurrentLocation = { viewModel.setLocationAsCurrentLocation() }
                        )
                    else
                        EnableLocationButton(
                            modifier = Modifier.padding(top = 12.dp),
                            requestLocation = {
                                showPermissionInfoBar = true
                            }
                        )
                }


                Spacer(modifier = Modifier.height(16.dp))
            }


            val favourites by viewModel.favouriteLocationsList.observeAsState(initial = listOf())
            SpecialLocationTab(
                tabTitle = stringResource(id = R.string.favourites_plain_text),
                locationList = favourites,
                selectLocation = { viewModel.setNewLocation(it) },
                removeFromSpecialList = { viewModel.removeFromFavourites(it) },
                clearAll = { viewModel.deleteAllFavourite() }
            )

            val recents by viewModel.recentLocationsList.observeAsState(initial = listOf())
            SpecialLocationTab(
                tabTitle = stringResource(id = R.string.recent_plain_text),
                locationList = recents,
                selectLocation = { viewModel.setNewLocation(it) },
                removeFromSpecialList = { viewModel.removeFromRecent(it) },
                clearAll = { viewModel.deleteAllRecent() }
            )
        }


        val context = LocalContext.current
        val permissionLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { permissionGranted ->
                Timber.d("permissionGranted is $permissionGranted")
                if (permissionGranted)
                    viewModel.fetchCurrentLocation(context)
            }
        LaunchedEffect(key1 = showLocationRequest) {
            if (showLocationRequest) {
                permissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                showLocationRequest = false
            }
        }


        if (showPermissionInfoBar)
            EnableLocationInfo(
                cancelRequest = { showPermissionInfoBar = false },
                proceedWithRequest = {
                    showPermissionInfoBar = false
                    showLocationRequest = true
                }
            )


        var searchQuery by remember { mutableStateOf("") }
        val suggestions by viewModel.suggestions.observeAsState(
            Resource.Error(
                ErrorType.Other,
                R.string.no_search_input
            )
        )
        val favouriteLocations by viewModel.listOfFavouriteLocation.observeAsState(listOf())

        if (showSearchTab)
            SearchTab(
                query = searchQuery,
                onSearchQueryChanged = {
                    searchQuery = it
                    viewModel.getLocationSuggestions(searchQuery)
                },
                suggestions = suggestions,
                favouriteLocations = favouriteLocations,
                selectLocation = {
                    viewModel.setNewLocation(it)
                    showSearchTab = false
                    navigateBack = true
                },
                addOrRemoveFromFavourite = { viewModel.addOrRemoveFavourite(it) },
                hideSearchTab = {
                    showSearchTab = false
                }
            )


        val errorMessage by viewModel.errorMessage.collectAsState()

        LaunchedEffect(key1 = errorMessage) {
            errorMessage?.let {
                val snackbarResult = snackbarHostState.showSnackbar(
                    context.getString(it),
                    // Add the Open settings action if the error is: Location is off
                    if (errorMessage == R.string.turn_location_on) "Open settings" else null
                )
                if (snackbarResult == SnackbarResult.ActionPerformed) {
                    // TODO: Turn location on
                }
                viewModel.resetErrorMessage()
            }
        }
    }
}

@Composable
private fun SpecialLocationTab(
    modifier: Modifier = Modifier,
    tabTitle: String,
    locationList: List<Location>?,
    selectLocation: (Location) -> Unit,
    removeFromSpecialList: (Location) -> Unit,
    clearAll: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(top = 16.dp)
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = LocalAppColors.current.surface,
            contentColor = LocalAppColors.current.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = tabTitle,
                fontFamily = QuickSand,
                color = LocalAppColors.current.onSurface,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            )

            Button(
                onClick = {
                    clearAll()
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.clear_all_plain_text),
                    fontFamily = QuickSand,
                    color = LocalAppColors.current.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }

        if (locationList.isNullOrEmpty()) {
            Box(
                Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.no_favourite_locations_selected_yet),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            // TODO: Change this from Column to LazyColumn
            Column(
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .fillMaxWidth()
            ) {
                locationList.forEachIndexed { index, location ->
                    LocationSuggestion(
                        onSelectLocation = selectLocation,
                        location = location,
                        actionIcon = Icons.Rounded.Favorite,
                        actionIconContentDescription = stringResource(R.string.remove_from_favourites),
                        onActionIcon = removeFromSpecialList,
                        currentIndex = index,
                        lastIndex = locationList.lastIndex
                    )
                }
            }
        }
    }
}


@Composable
fun EnableLocationButton(modifier: Modifier = Modifier, requestLocation: () -> Unit) {
    Row(
        modifier
            .fillMaxWidth()
            .clickable { requestLocation() }
            .padding(horizontal = 12.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.send),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = stringResource(id = R.string.enable_location_services_plain_text),
            fontFamily = QuickSand,
            letterSpacing = 0.sp,
            fontSize = 17.sp,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
fun UseCurrentLocation(
    location: Location,
    isRefreshingCurrentLocation: Boolean,
    modifier: Modifier = Modifier,
    useCurrentLocation: (Location) -> Unit
) {
    Column(
        modifier
            .fillMaxWidth()
            .clickable { useCurrentLocation(location) }
            .padding(vertical = 12.dp, horizontal = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(
                    id =
                    // TODO: Change this to Refresh icon
                    if (isRefreshingCurrentLocation) R.drawable.radar else R.drawable.send
                ),
                contentDescription = null,
                tint = LocalAppColors.current.onSurface,
                modifier = Modifier.size(16.dp)
            )

            Text(
                text = stringResource(id = if (isRefreshingCurrentLocation) R.string.refreshing_location else R.string.current_plain_text),
                fontFamily = AdventPro,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = LocalAppColors.current.onSurface,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        Text(
            text = location.displayName,
            fontFamily = QuickSand,
            fontSize = 18.sp,
            letterSpacing = (-0.25).sp,
            lineHeight = 18.sp,
            color = LocalAppColors.current.onSurface,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = location.country,
            fontFamily = QuickSand,
            fontSize = 15.sp,
            lineHeight = 15.sp,
            color = LocalAppColors.current.onSurfaceLighter,
            modifier = Modifier.padding(top = 1.dp)
        )
    }
}


@Composable
fun EnableLocationInfo(
    modifier: Modifier = Modifier,
    cancelRequest: () -> Unit,
    proceedWithRequest: () -> Unit
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .clickable { cancelRequest() }) {

        Card(
            colors = CardDefaults.cardColors(
                containerColor = LocalAppColors.current.surfaceSmallerVariant,
                contentColor = LocalAppColors.current.onSurface
            ),
            modifier = modifier
                .fillMaxWidth(0.85f)
                .align(Alignment.Center)
                .padding(vertical = 8.dp, horizontal = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(Modifier.padding(vertical = 12.dp, horizontal = 10.dp)) {

                Text(
                    text = stringResource(R.string.sky_weather_is_about_to_request_for_location_permission),
                    style = MaterialTheme.typography.labelMedium
                )

                Text(
                    text = stringResource(R.string.location_permission_reason),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 6.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { cancelRequest() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LocalAppColors.current.surface,
                            contentColor = LocalAppColors.current.onSurface
                        ),
                        border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.6f))
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { proceedWithRequest() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green.copy(alpha = 0.55f),
                            contentColor = LocalAppColors.current.onSurface
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.proceed),
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun PreviewFindLocationScreen() = AppTheme {
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            FindLocationScreen(rememberNavController(), snackbarHostState)
        }
    }
}