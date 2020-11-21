package com.octagon_technologies.sky_weather.ui.search_location

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.sky_weather.database.MainDataBase
import com.octagon_technologies.sky_weather.network.location.LocationItem
import com.octagon_technologies.sky_weather.ui.search_location.each_search_result_item.EachSearchResultItem
import com.octagon_technologies.sky_weather.ui.shared_code.MainFavouriteLocationsObject
import com.octagon_technologies.sky_weather.ui.shared_code.MainLocationObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchLocationViewModel(val context: Context) : ViewModel() {
    val mainDataBase by lazy { MainDataBase.getInstance(context) }

    private var _locationSuggestions = MutableLiveData<Map<String?, LocationItem>>()
    val locationSuggestions: LiveData<Map<String?, LocationItem>> = _locationSuggestions

    var favouriteItemsMap: MutableLiveData<Map<String?, LocationItem>> = MutableLiveData(mapOf())

    init {
        viewModelScope.launch {
            favouriteItemsMap.value = MainFavouriteLocationsObject.getFavouriteLocationsAsync(mainDataBase)
                ?.sortedBy { it.displayName }?.associateBy { it.placeId }
        }
    }

    fun getLocationSuggestions(query: String) {
        if (query.isEmpty()) return

        viewModelScope.launch {
            _locationSuggestions.value = MainLocationObject.getLocationSuggestionsFromQuery(query)
        }
    }

    val addToFavourite = { eachSearchResultItem: EachSearchResultItem ->
        viewModelScope.launch {
            if (eachSearchResultItem.isLikedByUser) {
                MainFavouriteLocationsObject.removeFavouriteLocationToLocalStorage(
                    mainDataBase,
                    eachSearchResultItem.locationItem
                )
            } else {
                MainFavouriteLocationsObject.insertFavouriteLocationToLocalStorage(
                    mainDataBase,
                    eachSearchResultItem.locationItem
                )
            }

            Timber.d("addToFavourite called with isLikedByUser as ${eachSearchResultItem.isLikedByUser}")
        }
    }
}