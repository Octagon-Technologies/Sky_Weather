package com.octagon_technologies.sky_weather.ui.search_location

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.sky_weather.repository.FavouriteLocationsRepo
import com.octagon_technologies.sky_weather.repository.LocationRepo
import com.octagon_technologies.sky_weather.repository.database.WeatherDataBase
import com.octagon_technologies.sky_weather.repository.network.location.Location
import com.octagon_technologies.sky_weather.ui.search_location.each_search_result_item.EachSearchResultItem
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchLocationViewModel @ViewModelInject constructor(val weatherDataBase: WeatherDataBase) :
    ViewModel() {

    private var _locationSuggestions = MutableLiveData<Map<String?, Location>>()
    val locationSuggestions: LiveData<Map<String?, Location>> = _locationSuggestions

    var favouriteItemsMap: MutableLiveData<Map<String?, Location>> = MutableLiveData(mapOf())

    init {
        viewModelScope.launch {
            favouriteItemsMap.value =
                FavouriteLocationsRepo.getFavouriteLocationsAsync(weatherDataBase)
                    ?.sortedBy { it.displayName }?.associateBy { it.placeId }
        }
    }

    fun getLocationSuggestions(query: String) {
        if (query.isEmpty()) return

        viewModelScope.launch {
            _locationSuggestions.value = LocationRepo.getLocationSuggestionsFromQuery(query)
        }
    }

    val addToFavourite = { eachSearchResultItem: EachSearchResultItem ->
        viewModelScope.launch {
            if (eachSearchResultItem.isLikedByUser) {
                FavouriteLocationsRepo.removeFavouriteLocationToLocalStorage(
                    weatherDataBase,
                    eachSearchResultItem.location
                )
            } else {
                FavouriteLocationsRepo.insertFavouriteLocationToLocalStorage(
                    weatherDataBase,
                    eachSearchResultItem.location
                )
            }

            Timber.d("addToFavourite called with isLikedByUser as ${eachSearchResultItem.isLikedByUser}")
        }

        Unit
    }
}