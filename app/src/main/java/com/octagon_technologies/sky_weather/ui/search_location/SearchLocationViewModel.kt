package com.octagon_technologies.sky_weather.ui.search_location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.repository.repo.FavouriteLocationRepo
import com.octagon_technologies.sky_weather.repository.repo.LocationRepo
import com.octagon_technologies.sky_weather.repository.repo.RecentLocationsRepo
import com.octagon_technologies.sky_weather.repository.repo.SettingsRepo
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


    fun getLocationSuggestions(query: String) {
        viewModelScope.launch {
            locationRepo.getLocationSuggestionsFromQuery(query)
        }
    }

    fun selectLocation(location: Location) {
        viewModelScope.launch {
            locationRepo.insertLocalLocation(location)
            recentLocationsRepo.insertLocalRecentLocation(location)
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
}