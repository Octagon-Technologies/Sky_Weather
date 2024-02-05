package com.octagon_technologies.sky_weather.ui.find_location

import android.Manifest
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.BasePermissionListener
import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.repository.repo.SettingsRepo
import com.octagon_technologies.sky_weather.repository.repo.FavouriteLocationRepo
import com.octagon_technologies.sky_weather.repository.repo.LocationRepo
import com.octagon_technologies.sky_weather.repository.repo.RecentLocationsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
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

    val location = locationRepo.location

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val favouriteLocationsList: LiveData<List<Location>> =
        favouriteLocationRepo.listOfFavouriteLocation
    val recentLocationsList: LiveData<List<Location>> = recentLocationsRepo.listOfRecentLocation

    fun setIsLoadingAsFalse() {
        if (isLoading.value == true) {
            _isLoading.value = false
        }
    }


    fun deleteAllFavourite() {
        viewModelScope.launch { favouriteLocationRepo.removeAllFavouriteLocations() }
    }

    fun deleteAllRecent() {
        viewModelScope.launch { recentLocationsRepo.removeAllRecentLocations() }
    }

    fun removeFromRecent(location: Location) {
        viewModelScope.launch { recentLocationsRepo.removeLocalRecentLocation(location) }
    }

    fun removeFromFavourites(location: Location) {
        viewModelScope.launch { favouriteLocationRepo.removeLocalFavouriteLocation(location) }
    }

    /*
     Called when a user selects a location from either his/her favorites or recent locations.
     The selected location is set as the main location in the database
     */
    fun setNewLocation(location: Location) {
        viewModelScope.launch {
//            (activity as? MainActivity)?.hasNotificationChanged = false
            locationRepo.insertLocalLocation(location)
        }
    }

    fun useGPSLocation(context: Context) {
        locationRepo.useGPSLocation(context)
    }

    fun checkIfPermissionIsGranted(context: Context) {
        _isLoading.value = true
        Timber.d("checkIfPermissionIsGranted has been called.")

        Dexter.withContext(context)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : BasePermissionListener() {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    Timber.d("onPermissionGranted called")
                    useGPSLocation(context)
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Timber.d("onPermissionDenied called")
                    _isLoading.value = false
                }
            })
            .withErrorListener {
                Timber.e(it.toString())
            }
            .check()
    }
}