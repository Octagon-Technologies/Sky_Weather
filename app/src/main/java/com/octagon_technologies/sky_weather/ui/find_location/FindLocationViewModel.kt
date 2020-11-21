package com.octagon_technologies.sky_weather.ui.find_location

import android.Manifest
import android.content.Context
import android.location.LocationManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.sky_weather.database.MainDataBase
import com.octagon_technologies.sky_weather.network.location.LocationItem
import com.octagon_technologies.sky_weather.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.ui.shared_code.MainFavouriteLocationsObject
import com.octagon_technologies.sky_weather.ui.shared_code.MainLocationObject
import com.octagon_technologies.sky_weather.ui.shared_code.MainLocationObject.turnOnGPS
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.octagon_technologies.sky_weather.ui.shared_code.MainRecentLocationsObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class FindLocationViewModel(private val context: Context) : ViewModel() {
    val mainDatabase = MainDataBase.getInstance(context)
    val uiScope = CoroutineScope(Dispatchers.Main)

    private var _reversedGeoCodingLocation = MutableLiveData<ReverseGeoCodingLocation>()
    val reversedGeoCodingLocation: LiveData<ReverseGeoCodingLocation>
        get() = _reversedGeoCodingLocation

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private var _favouriteLocationsList = MutableLiveData<ArrayList<LocationItem>?>()
    val favouriteLocationsList: LiveData<ArrayList<LocationItem>?>
        get() = _favouriteLocationsList

    private var _recentLocationsList = MutableLiveData<ArrayList<LocationItem>?>()
    val recentLocationsList: LiveData<ArrayList<LocationItem>?>
        get() = _recentLocationsList

    init {
        getFavouriteLocations()
        getRecentLocations()
    }

    private fun getFavouriteLocations() {
        uiScope.launch {
            _favouriteLocationsList.value = MainFavouriteLocationsObject.getFavouriteLocationsAsync(mainDatabase)
            Timber.d("_favouriteLocationsList.value.size is ${_favouriteLocationsList.value?.size}")
        }
    }

    private fun getRecentLocations() {
        uiScope.launch {
            _recentLocationsList.value = MainRecentLocationsObject.getRecentLocationsAsync(mainDatabase)
            Timber.d("_recentLocationsList.value.size is ${_recentLocationsList.value?.size}")
        }
    }

    fun deleteAllFavourite() {
        uiScope.launch {
            MainFavouriteLocationsObject.removeAllFavouriteLocations(
                mainDatabase, _favouriteLocationsList.value
            )
            _favouriteLocationsList.value = null
        }
    }

    fun deleteAllRecent() {
        uiScope.launch {
            MainRecentLocationsObject.removeAllRecentLocations(
                mainDatabase, _recentLocationsList.value
            )
            _recentLocationsList.value = null
        }
    }


    fun editLocationInDatabase(reverseGeoCodingLocation: ReverseGeoCodingLocation) {
        CoroutineScope(Dispatchers.Main).launch {
            MainLocationObject.insertLocationToLocalStorage(
                mainDatabase, reverseGeoCodingLocation
            )
        }
    }

    fun addCurrentLocationToDatabase() {
        uiScope.launch {
            MainLocationObject.insertLocationToLocalStorage(mainDatabase,
                _reversedGeoCodingLocation.value
                    ?: throw RuntimeException("_reversedGeoCodingLocation.value is ${_reversedGeoCodingLocation.value}")
            )
        }
    }

    private fun getCurrentLocation() {
        viewModelScope.launch {
            _reversedGeoCodingLocation.value = MainLocationObject.getLocationNameFromCoordinatesAsync(turnOnGPS(context), mainDatabase)
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
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    Timber.d("onPermissionGranted called")
                    turnOnCurrentLocation()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Timber.d("onPermissionDenied called")
                    _isLoading.value = false
                }

                override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {
                    Timber.d("onPermissionRationaleShouldBeShown called")
                    p1?.continuePermissionRequest()
                }
            })
            .withErrorListener {
                Timber.e(it.toString())
            }
            .check()
    }
}