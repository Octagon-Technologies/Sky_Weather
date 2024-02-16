package com.octagon_technologies.sky_weather.ui.search_location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.repository.repo.FavouriteLocationRepo
import com.octagon_technologies.sky_weather.repository.repo.LocationRepo
import com.octagon_technologies.sky_weather.repository.repo.RecentLocationsRepo
import com.octagon_technologies.sky_weather.repository.repo.SettingsRepo
import com.octagon_technologies.sky_weather.utils.StatusCode
import com.octagon_technologies.sky_weather.utils.catchNetworkErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchLocationViewModel @Inject constructor(
    private val settingsRepo: SettingsRepo,
    private val locationRepo: LocationRepo,
    private val favouriteLocationRepo: FavouriteLocationRepo,
    private val recentLocationsRepo: RecentLocationsRepo
) :
    ViewModel() {

    val theme = settingsRepo.theme
    val searchLocationSuggestions = locationRepo.searchLocationSuggestions

    val listOfFavouriteLocation = favouriteLocationRepo.listOfFavouriteLocation

    private val _statusCode = MutableLiveData<StatusCode?>()
    val statusCode: LiveData<StatusCode?> = _statusCode

    private val _navigateHome = MutableLiveData<Boolean?>()
    val navigateHome: LiveData<Boolean?> = _navigateHome

    fun getLocationSuggestions(query: String) {
        viewModelScope.launch {
            _statusCode.catchNetworkErrors {
                locationRepo.getLocationSuggestionsFromQuery(query)
            }
        }
    }

    /*
    TODO: Research why we have to launch new coroutines instead of running both in one scope
     */
    fun selectLocation(location: Location) {
        viewModelScope.launch {
            launch { locationRepo.insertLocalLocation(location) }
            launch { recentLocationsRepo.insertLocalRecentLocation(location) }

            _navigateHome.value = true
        }
    }

    val changeFavoriteStatus: (Boolean, Location) -> Unit = { isLikedByUser, location ->
        viewModelScope.launch {
            if (isLikedByUser)
                favouriteLocationRepo.removeLocalFavouriteLocation(location)
            else
                favouriteLocationRepo.insertLocalFavouriteLocation(location)


            Timber.d("addToFavourite called with isLikedByUser as $isLikedByUser")
        }
    }

    fun onStatusCodeDisplayed() {
        _statusCode.value = null
    }

    fun onNavigateHomeDone() {
        _navigateHome.value = null
    }
}