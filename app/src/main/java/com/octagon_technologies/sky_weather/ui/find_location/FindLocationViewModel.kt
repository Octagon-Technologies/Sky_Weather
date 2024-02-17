package com.octagon_technologies.sky_weather.ui.find_location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.BasePermissionListener
import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.repository.repo.FavouriteLocationRepo
import com.octagon_technologies.sky_weather.repository.repo.LocationRepo
import com.octagon_technologies.sky_weather.repository.repo.RecentLocationsRepo
import com.octagon_technologies.sky_weather.repository.repo.SettingsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
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

    val location = locationRepo.location

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val favouriteLocationsList: LiveData<List<Location>> =
        favouriteLocationRepo.listOfFavouriteLocation
    val recentLocationsList: LiveData<List<Location>> = recentLocationsRepo.listOfRecentLocation

    private val _navigateHome = MutableLiveData<Boolean?>()
    val navigateHome: LiveData<Boolean?> = _navigateHome

    init {
        /*
        We want to check if the user is currently using his/her location or choose a location in the search.
        map. If he choose current location, fetch his current location
         */
        viewModelScope.launch {
            location.asFlow().first { location ->
                try {
                    val saveLocationToDatabase = location?.isGps == true
                    val isGpsOn = settingsRepo.isGpsOn.first()
                    Timber.d("settingsRepo.isGpsOn.value is $isGpsOn and saveLocationToDatabase is $saveLocationToDatabase")

                    if (isGpsOn)
                        locationRepo.useGPSLocation(
                            context = context,
                            saveLocationToDatabase = saveLocationToDatabase,
                            userShouldTurnLocationOn = {})
                } catch (e: Exception) {
                    Timber.e(e)
                }

                true
            }
        }
    }

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
            launch { recentLocationsRepo.insertLocalRecentLocation(location) }
            launch { locationRepo.insertLocalLocation(location) }

            _navigateHome.value = true
        }
    }

    fun useAndSaveGPSLocation(context: Context) {
        locationRepo.useGPSLocation(context, true) {
            Toast.makeText(context, "Turn on location in your settings", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkIfPermissionIsGranted(context: Context) {
        _isLoading.value = true
        Timber.d("checkIfPermissionIsGranted has been called.")

        Dexter.withContext(context)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : BasePermissionListener() {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    Timber.d("onPermissionGranted called")
                    viewModelScope.launch {
                        useAndSaveGPSLocation(context)
                        settingsRepo.changeIsGpsOn(true)
                    }
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

    fun onNavigateHomeDone() {
        _navigateHome.value = null
    }
}