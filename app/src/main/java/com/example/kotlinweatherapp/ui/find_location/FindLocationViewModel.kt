package com.example.kotlinweatherapp.ui.find_location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinweatherapp.database.LocationDatabaseClass
import com.example.kotlinweatherapp.database.MainDataBase
import com.example.kotlinweatherapp.network.location.LocationItem
import com.example.kotlinweatherapp.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.example.kotlinweatherapp.ui.weather_forecast_objects.MainFavouriteLocationsObject
import com.example.kotlinweatherapp.ui.weather_forecast_objects.MainLocationObject
import com.example.kotlinweatherapp.ui.weather_forecast_objects.MainLocationObject.turnOnGPS
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class FindLocationViewModel(private val context: Context) : ViewModel() {
    private val mainDatabase = MainDataBase.getInstance(context)
    private val uiScope = CoroutineScope(Dispatchers.Main)

    private var _reversedGeoCodingLocation = MutableLiveData<ReverseGeoCodingLocation>()
    val reversedGeoCodingLocation: LiveData<ReverseGeoCodingLocation>
        get() = _reversedGeoCodingLocation

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private var _favouriteLocationsList = MutableLiveData<ArrayList<LocationItem>?>()
    val favouriteLocationsList: LiveData<ArrayList<LocationItem>?>
        get() = _favouriteLocationsList

    init {
        _isLoading.value = false
    }

    fun getFavouriteLocations() {
        uiScope.launch {
            _favouriteLocationsList.value = MainFavouriteLocationsObject.getFavouriteLocationsAsync(mainDatabase)
            Timber.d("_favouriteLocationsList.value.size is ${_favouriteLocationsList.value?.size}")
        }
    }

    fun addLocationToDatabase() {
        MainLocationObject.addSelectedLocation(mainDatabase, _reversedGeoCodingLocation.value ?: throw RuntimeException("_reversedGeoCodingLocation.value is ${_reversedGeoCodingLocation.value}"))
    }

    private fun getLocation() {
        uiScope.launch {
            _reversedGeoCodingLocation.value = MainLocationObject.getLocationNameFromCoordinatesAsync(turnOnGPS(context), mainDatabase)
            _reversedGeoCodingLocation.value?.also {

                Timber.d("_coordinates.value is ${_reversedGeoCodingLocation.value}")
                _isLoading.value = false

            }
        }

    }

    fun turnOnLocation() {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        Timber.d("gpsEnabled is $gpsEnabled")
        getLocation()
    }

    fun checkIfPermissionIsGranted() {
        _isLoading.value = true
        Dexter.withContext(context)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    Timber.d("onPermissionGranted called")
                    turnOnLocation()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Timber.d("onPermissionDenied called")
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    Timber.d("onPermissionDenied called")
                }
            })
            .withErrorListener {
                Timber.e(it.toString())
            }
            .check()
    }
}