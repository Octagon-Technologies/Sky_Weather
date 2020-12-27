package com.octagon_technologies.sky_weather.ui.find_location

import android.Manifest
import android.content.Context
import android.location.LocationManager
import androidx.fragment.app.FragmentActivity
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.BasePermissionListener
import com.octagon_technologies.sky_weather.MainActivity
import com.octagon_technologies.sky_weather.repository.FavouriteLocationsRepo
import com.octagon_technologies.sky_weather.repository.LocationRepo
import com.octagon_technologies.sky_weather.repository.LocationRepo.turnOnGPS
import com.octagon_technologies.sky_weather.repository.RecentLocationsRepo
import com.octagon_technologies.sky_weather.repository.database.WeatherDataBase
import com.octagon_technologies.sky_weather.repository.network.location.Location
import com.octagon_technologies.sky_weather.repository.network.reverse_geocoding_location.ReverseGeoCodingLocation
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import timber.log.Timber

class FindLocationViewModel @ViewModelInject constructor(
    @ApplicationContext private val context: Context,
    private val weatherDataBase: WeatherDataBase
) : ViewModel() {

    private var _reversedGeoCodingLocation = MutableLiveData<ReverseGeoCodingLocation>()
    val reversedGeoCodingLocation: LiveData<ReverseGeoCodingLocation>
        get() = _reversedGeoCodingLocation

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private var _favouriteLocationsList = MutableLiveData<ArrayList<Location>?>()
    val favouriteLocationsList: LiveData<ArrayList<Location>?>
        get() = _favouriteLocationsList

    private var _recentLocationsList = MutableLiveData<ArrayList<Location>?>()
    val recentLocationsList: LiveData<ArrayList<Location>?>
        get() = _recentLocationsList

    init {
        getFavouriteLocations()
        getRecentLocations()
    }

    fun setIsLoadingAsFalse() {
        if (isLoading.value == true) {
            _isLoading.value = false
        }
    }

    private fun getFavouriteLocations() {
        viewModelScope.launch {
            _favouriteLocationsList.value =
                FavouriteLocationsRepo.getFavouriteLocationsAsync(this@FindLocationViewModel.weatherDataBase)
            Timber.d("_favouriteLocationsList.value.size is ${_favouriteLocationsList.value?.size}")
        }
    }

    private fun getRecentLocations() {
        viewModelScope.launch {
            _recentLocationsList.value =
                RecentLocationsRepo.getRecentLocationsAsync(this@FindLocationViewModel.weatherDataBase)
            Timber.d("_recentLocationsList.value.size is ${_recentLocationsList.value?.size}")
        }
    }

    fun deleteAllFavourite() {
        viewModelScope.launch {
            FavouriteLocationsRepo.removeAllFavouriteLocations(
                this@FindLocationViewModel.weatherDataBase, _favouriteLocationsList.value
            )
            _favouriteLocationsList.value = null
        }
    }

    fun deleteAllRecent() {
        viewModelScope.launch {
            RecentLocationsRepo.removeAllRecentLocations(
                this@FindLocationViewModel.weatherDataBase, _recentLocationsList.value
            )
            _recentLocationsList.value = null
        }
    }

    fun removeFromRecent(location: Location) {
        viewModelScope.launch {
            RecentLocationsRepo.removeRecentLocationToLocalStorage(
                this@FindLocationViewModel.weatherDataBase,
                location
            )
        }
    }

    fun removeFromFavourites(location: Location) {
        viewModelScope.launch {
            FavouriteLocationsRepo.removeFavouriteLocationToLocalStorage(
                this@FindLocationViewModel.weatherDataBase,
                location
            )
        }
    }

    fun editLocationInDatabase(
        activity: FragmentActivity?,
        reverseGeoCodingLocation: ReverseGeoCodingLocation
    ) {
        viewModelScope.launch {
            (activity as? MainActivity)?.hasNotificationChanged = false
            LocationRepo.insertLocationToLocalStorage(
                this@FindLocationViewModel.weatherDataBase, reverseGeoCodingLocation
            )
        }
    }

    fun addCurrentLocationToDatabase(activity: FragmentActivity?) {
        viewModelScope.launch {
            // We only want to save the location to database if the user did it on the app and not through a widget.
            (activity as? MainActivity)?.let {
                it.hasNotificationChanged = false
                LocationRepo.insertLocationToLocalStorage(
                    this@FindLocationViewModel.weatherDataBase,
                    _reversedGeoCodingLocation.value
                        ?: throw RuntimeException("_reversedGeoCodingLocation.value is ${_reversedGeoCodingLocation.value}")
                )
            }
        }
    }

    private fun getCurrentLocation() {
        viewModelScope.launch {
            _reversedGeoCodingLocation.value =
                LocationRepo.getLocationNameFromCoordinatesAsync(
                    turnOnGPS(context),
                    this@FindLocationViewModel.weatherDataBase
                )
                    ?.also {
                        Timber.d("_coordinates.value is ${_reversedGeoCodingLocation.value}")
                        _isLoading.value = false
                    }
        }
    }

    fun turnOnCurrentLocation() {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        Timber.d("gpsEnabled is $gpsEnabled")
        getCurrentLocation()
    }

    fun checkIfPermissionIsGranted() {
        _isLoading.value = true
        Timber.d("checkIfPermissionIsGranted has been called.")
        Dexter.withContext(context)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : BasePermissionListener() {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    Timber.d("onPermissionGranted called")
                    turnOnCurrentLocation()
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