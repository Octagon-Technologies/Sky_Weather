package com.octagontechnologies.sky_weather.ui.find_location.screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.Surface
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.octagontechnologies.sky_weather.R
import com.octagontechnologies.sky_weather.domain.Location
import com.octagontechnologies.sky_weather.domain.location.CurrentLocationState
import com.octagontechnologies.sky_weather.domain.location.getTagName
import com.octagontechnologies.sky_weather.main_activity.Screens
import com.octagontechnologies.sky_weather.ui.compose.ChangeStatusBars
import com.octagontechnologies.sky_weather.ui.compose.getActivity
import com.octagontechnologies.sky_weather.ui.compose.theme.AppTheme
import com.octagontechnologies.sky_weather.ui.compose.theme.DarkGreen
import com.octagontechnologies.sky_weather.ui.compose.theme.LocalAppColors
import com.octagontechnologies.sky_weather.ui.compose.theme.QuickSand
import com.octagontechnologies.sky_weather.ui.find_location.FindLocationViewModel
import com.octagontechnologies.sky_weather.ui.find_location.components.LocationSuggestion
import com.octagontechnologies.sky_weather.ui.find_location.components.SearchTab
import com.octagontechnologies.sky_weather.utils.ErrorType
import com.octagontechnologies.sky_weather.utils.Resource
import com.octagontechnologies.sky_weather.utils.Units
import com.skydoves.cloudy.cloudy
import timber.log.Timber

@Composable
fun FindLocationScreen(navController: NavController, snackbarHostState: SnackbarHostState) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<FindLocationViewModel>()
    val units by viewModel.units.observeAsState()

    var showSearchTab by remember { mutableStateOf(false) }


    val location by viewModel.location.collectAsState(initial = null)
    val currentLocation by viewModel.currentLocation.collectAsState()
    val currentLocationState by viewModel.currentLocationState.collectAsState(CurrentLocationState.Refreshing)


    val navigateHome by viewModel.navigateHome.observeAsState(initial = false)


    // If there is no location set, back press == app exit
    val activity = LocalContext.current.getActivity()

    LaunchedEffect(key1 = Unit) {
        viewModel.refreshCurrentLocationIfGPSIsOn(context)
    }

    ChangeStatusBars(
        navigateBack = navigateHome,

        // Activate default functioning if location is set
        onNavigateBack = {
            Timber.d("onNavigateBack called with location as $location")
            if (location != null)
                navController.navigate(Screens.Current) {
                    popUpTo(Screens.Current) {
                        inclusive = true
                    }
                }
            else {
                if (!viewModel.isInitLocationSet.value)
                    activity?.finish()
            }
        },
        resetNavigateBack = { viewModel.resetNavigateHome() }
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
                        modifier = Modifier.clickable { viewModel.navigateHome() },
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
                    onClick = { showSearchTab = true },
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
                            currentLocationState = currentLocationState.getTagName(),
                            locationInUse = currentLocationState == CurrentLocationState.InUse,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            useCurrentLocation = { viewModel.setLocationAsCurrentLocation() }
                        )
                    else
                        EnableLocationButton(
                            modifier = Modifier.padding(top = 12.dp),
                            requestLocation = {
                                val locationPermission =
                                    context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)

                                if (locationPermission != PackageManager.PERMISSION_GRANTED)
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
                emptyPlaceholderText = stringResource(id = R.string.no_favourite_locations_selected_yet),
                selectLocation = { viewModel.setNewLocation(it) },
                removeFromSpecialList = { viewModel.removeFromFavourites(it) },
                clearAll = { viewModel.deleteAllFavourite() }
            )

            val recents by viewModel.recentLocationsList.observeAsState(initial = listOf())
            SpecialLocationTab(
                tabTitle = stringResource(id = R.string.recent_plain_text),
                emptyPlaceholderText = stringResource(id = R.string.no_recent_locations_selected_yet),
                locationList = recents,
                selectLocation = { viewModel.setNewLocation(it) },
                removeFromSpecialList = { viewModel.removeFromRecent(it) },
                clearAll = { viewModel.deleteAllRecent() }
            )
        }


        val permissionLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { permissionGranted ->
                Timber.d("permissionGranted is $permissionGranted")
                if (permissionGranted)
                    viewModel.fetchCurrentLocation(
                        context = context, updateUserLocation = true
                    )
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

        AnimatedVisibility(visible = showSearchTab, enter = fadeIn(), exit = fadeOut()) {
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

                    viewModel.navigateHome()
                },
                addOrRemoveFromFavourite = { viewModel.addOrRemoveFavourite(it) },
                hideSearchTab = {
                    showSearchTab = false
                }
            )
        }


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
    emptyPlaceholderText: String,
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
            containerColor = LocalAppColors.current.distinctSurface,
            contentColor = LocalAppColors.current.onSurface
        ),
        elevation = CardDefaults.elevatedCardElevation(),
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
                letterSpacing = (0.1).sp,
                fontSize = 17.sp
            )

            Button(
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LocalAppColors.current.distinctSurface),
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
                    fontSize = 15.sp
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
                    text = emptyPlaceholderText,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
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
    currentLocationState: String,
    locationInUse: Boolean,
    modifier: Modifier = Modifier,
    useCurrentLocation: (Location) -> Unit
) {
    LaunchedEffect(key1 = currentLocationState) {
        Timber.d("currentLocationState is $currentLocationState")
    }

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
                    if (locationInUse) R.drawable.current_location else R.drawable.radar
                ),
                contentDescription = null,
                tint = LocalAppColors.current.onSurface,
                modifier = Modifier.size(if (locationInUse) 20.dp else 18.dp)
            )

            Surface(
                modifier = Modifier.padding(start = 12.dp),
                color = if (locationInUse) DarkGreen.copy(alpha = 0.8f) else LocalAppColors.current.surfaceVariant,
                shape = CircleShape
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 1.dp),
                    text = currentLocationState,
                    fontFamily = QuickSand,
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    letterSpacing = (0.2).sp,
                    fontWeight = FontWeight.Medium,
                    color = LocalAppColors.current.onSurface,
                )
            }
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