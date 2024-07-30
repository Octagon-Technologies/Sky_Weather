package com.octagontechnologies.sky_weather.ui.find_location

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.octagontechnologies.sky_weather.R
import com.octagontechnologies.sky_weather.domain.Location
import com.octagontechnologies.sky_weather.repository.repo.FavouriteLocationRepo
import com.octagontechnologies.sky_weather.repository.repo.LocationRepo
import com.octagontechnologies.sky_weather.repository.repo.RecentLocationsRepo
import com.octagontechnologies.sky_weather.repository.repo.SettingsRepo
import com.octagontechnologies.sky_weather.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindLocationViewModel @Inject constructor(
    private val locationRepo: LocationRepo,
    private val favouriteLocationRepo: FavouriteLocationRepo,
    private val recentLocationsRepo: RecentLocationsRepo,
    private val settingsRepo: SettingsRepo,
    @SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context
) : ViewModel() {

    val theme = settingsRepo.theme
    val units = settingsRepo.units
    val windDirectionUnits = settingsRepo.windDirectionUnits
    val timeFormat = settingsRepo.timeFormat

    val currentLocation =
        locationRepo.currentLocation.asFlow().stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val location =
        locationRepo.location.asFlow().stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _errorMessage = MutableStateFlow<Int?>(null)
    val errorMessage: StateFlow<Int?> = _errorMessage

    val favouriteLocationsList: LiveData<List<Location>> =
        favouriteLocationRepo.listOfFavouriteLocation
    val recentLocationsList: LiveData<List<Location>> = recentLocationsRepo.listOfRecentLocation

    private val _navigateHome = MutableLiveData<Boolean?>()
    val navigateHome: LiveData<Boolean?> = _navigateHome


    private val _isRefreshingCurrentLocation = MutableStateFlow(true)
    val isRefreshingCurrentLocation: StateFlow<Boolean> = _isRefreshingCurrentLocation

    fun refreshCurrentLocationIfGPSIsOn(context: Context) {
        val lastLocation = location.value
        val isGPSOn = lastLocation?.isGps == true

        if (isGPSOn)
            fetchCurrentLocation(context)

        _isRefreshingCurrentLocation.value = false
    }


    /*
     Called when a user selects a location from either his/her favorites or recent locations.
     The selected location is set as the main location in the database
     */
    fun setNewLocation(location: Location) {
        viewModelScope.launch {
            launch { recentLocationsRepo.insertLocalRecentLocation(location) }
            launch { locationRepo.insertLocalLocation(location) }

            _navigateHome.value = true
        }
    }

    fun setLocationAsCurrentLocation() {
        viewModelScope.launch {
            currentLocation.value?.let { locationRepo.insertLocalLocation(it) }

            _navigateHome.value = true
        }
    }


    fun fetchCurrentLocation(context: Context) = viewModelScope.launch {
        locationRepo.getGPSCoordinates(
            context = context,
            onGPSReceived = { deviceLatLng ->
                viewModelScope.launch {
                    val networkResponse = locationRepo.fetchCurrentLocationDetails(deviceLatLng)
                    if (networkResponse is Resource.Error)
                        _errorMessage.value = networkResponse.resMessage
                }
            },
            requestLocationBeTurnedOn = { _errorMessage.value = R.string.turn_location_on }
        )
//        settingsRepo.changeIsGpsOn(true)
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


    fun resetErrorMessage() { _errorMessage.value = null }
    fun resetNavigateHome() {
        _navigateHome.value = null
    }
}