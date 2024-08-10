package com.octagontechnologies.sky_weather.ui.find_location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.octagontechnologies.sky_weather.R
import com.octagontechnologies.sky_weather.domain.Location
import com.octagontechnologies.sky_weather.domain.location.CurrentLocationState
import com.octagontechnologies.sky_weather.repository.repo.FavouriteLocationRepo
import com.octagontechnologies.sky_weather.repository.repo.LocationRepo
import com.octagontechnologies.sky_weather.repository.repo.RecentLocationsRepo
import com.octagontechnologies.sky_weather.repository.repo.SettingsRepo
import com.octagontechnologies.sky_weather.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FindLocationViewModel @Inject constructor(
    private val locationRepo: LocationRepo,
    private val favouriteLocationRepo: FavouriteLocationRepo,
    private val recentLocationsRepo: RecentLocationsRepo,
    private val settingsRepo: SettingsRepo
) : ViewModel() {

    val theme = settingsRepo.theme
    val units = settingsRepo.units
    val windDirectionUnits = settingsRepo.windDirectionUnits
    val timeFormat = settingsRepo.timeFormat

    val currentLocation =
        locationRepo.currentLocation.asFlow().stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val location =
        locationRepo.location.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _isInitLocationSet = MutableStateFlow(false)
    val isInitLocationSet: StateFlow<Boolean> = _isInitLocationSet

    private val _errorMessage = MutableStateFlow<Int?>(null)
    val errorMessage: StateFlow<Int?> = _errorMessage

    val favouriteLocationsList: LiveData<List<Location>> =
        favouriteLocationRepo.listOfFavouriteLocation
    val recentLocationsList: LiveData<List<Location>> = recentLocationsRepo.listOfRecentLocation

    private val _navigateHome = MutableLiveData<Boolean>()
    val navigateHome: LiveData<Boolean> = _navigateHome

    private val _currentLocationState = MutableStateFlow(CurrentLocationState.Refreshing)
    val currentLocationState: StateFlow<CurrentLocationState> = _currentLocationState

    /**
     * In Use
     * Refreshing
     * Not In Use
     * No Network
     */
    fun refreshCurrentLocationIfGPSIsOn(context: Context) {
        val lastLocation = location.value
        val isUserUsingGPS = lastLocation?.isGps == true

        if (context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fetchCurrentLocation(context, isUserUsingGPS)
        } else
            _currentLocationState.value = CurrentLocationState.NotInUse
    }


    /*
     Called when a user selects a location from either his/her favorites or recent locations.
     The selected location is set as the main location in the database
     */
    fun setNewLocation(newLocation: Location) {
        viewModelScope.launch {
            launch { recentLocationsRepo.insertLocalRecentLocation(newLocation) }
            launch {
                locationRepo.setUserLocation(newLocation)
                _isInitLocationSet.value = true

//                // As soon as the location is updated, navigate home
                locationRepo.location.firstOrNull()
                navigateHome()
//                location.collectLatest {
//                    Timber.d("About to navigate home: location is $location")
//                    navigateHome()
//                }
            }
        }
    }

    fun setLocationAsCurrentLocation() {
        viewModelScope.launch {
            currentLocation.value?.let { locationRepo.setUserLocation(it) }

            // As soon as the location is updated, navigate home
            locationRepo.location.firstOrNull()
            navigateHome()
        }
    }


    fun fetchCurrentLocation(context: Context, updateUserLocation: Boolean) =
        viewModelScope.launch {
            locationRepo.getGPSCoordinates(
                context = context,
                onGPSReceived = { deviceLatLng ->
                    viewModelScope.launch {
                        val networkResponse =
                            locationRepo.updateCurrentLocationInDatabase(deviceLatLng)

                        Timber.d("updateUserLocation is $updateUserLocation")
                        Timber.d("networkResponse is ${networkResponse.data}")


                        // If we are not updating the device location, show current location "Not In Use"
                        if (!updateUserLocation) {
                            _currentLocationState.value = CurrentLocationState.NotInUse
                        } else {
                            // Update the device location if the network fetch was SUCCESSFUL
                            if (networkResponse is Resource.Success) {
                                _currentLocationState.value = CurrentLocationState.InUse
                                locationRepo.setUserLocation(networkResponse.data!!)
                            }
                            // Show "No Network" if an error occurs
                            else if (networkResponse is Resource.Error) {
                                _currentLocationState.value = CurrentLocationState.NoNetwork
                                _errorMessage.value = networkResponse.resMessage
                            }
                        }

                        Timber.d("currentLocationState.value is ${currentLocationState.value}")
                    }
                },
                requestLocationBeTurnedOn = {
                    _currentLocationState.value = CurrentLocationState.LocationOff
                    _errorMessage.value = R.string.turn_location_on
                }
            )
        }


    val suggestions = locationRepo.suggestions
    val listOfFavouriteLocation = favouriteLocationRepo.listOfFavouriteLocation

    private var searchJob: Job? = null

    fun getLocationSuggestions(query: String) = viewModelScope.launch {
        searchJob?.cancel()

        searchJob = launch {
            delay(800)
            locationRepo.getLocationSuggestionsFromQuery(query)
        }
    }


    fun addOrRemoveFavourite(location: Location) = viewModelScope.launch {
        favouriteLocationRepo.addOrRemoveFromFavourites(location)
    }


    fun deleteAllFavourite() =
        viewModelScope.launch { favouriteLocationRepo.removeAllFavouriteLocations() }

    fun deleteAllRecent() = viewModelScope.launch { recentLocationsRepo.removeAllRecentLocations() }

    fun removeFromRecent(location: Location) =
        viewModelScope.launch { recentLocationsRepo.removeLocalRecentLocation(location) }

    fun removeFromFavourites(location: Location) =
        viewModelScope.launch { favouriteLocationRepo.addOrRemoveFromFavourites(location) }

    fun navigateHome() {
        _navigateHome.value = true
    }

    fun resetErrorMessage() {
        _errorMessage.value = null
    }

    fun resetNavigateHome() {
        _navigateHome.value = false
    }
}