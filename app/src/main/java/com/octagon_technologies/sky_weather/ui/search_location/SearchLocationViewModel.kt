package com.octagon_technologies.sky_weather.ui.search_location

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.sky_weather.repository.database.MainDataBase
import com.octagon_technologies.sky_weather.repository.network.location.LocationItem
import com.octagon_technologies.sky_weather.ui.search_location.each_search_result_item.EachSearchResultItem
import com.octagon_technologies.sky_weather.repository.FavouriteLocationsRepo
import com.octagon_technologies.sky_weather.repository.LocationRepo
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchLocationViewModel(val context: Context) : ViewModel() {
    val mainDataBase by lazy { MainDataBase.getInstance(context) }

    private var _locationSuggestions = MutableLiveData<Map<String?, LocationItem>>()
    val locationSuggestions: LiveData<Map<String?, LocationItem>> = _locationSuggestions

    var favouriteItemsMap: MutableLiveData<Map<String?, LocationItem>> = MutableLiveData(mapOf())

    init {
        viewModelScope.launch {
            favouriteItemsMap.value = FavouriteLocationsRepo.getFavouriteLocationsAsync(mainDataBase)
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
                    mainDataBase,
                    eachSearchResultItem.locationItem
                )
            } else {
                FavouriteLocationsRepo.insertFavouriteLocationToLocalStorage(
                    mainDataBase,
                    eachSearchResultItem.locationItem
                )
            }

            Timber.d("addToFavourite called with isLikedByUser as ${eachSearchResultItem.isLikedByUser}")
        }

        Unit
    }
}