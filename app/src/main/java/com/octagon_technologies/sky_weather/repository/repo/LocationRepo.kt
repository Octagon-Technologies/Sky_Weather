package com.octagon_technologies.sky_weather.repository.repo

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.repository.database.location.LocalLocation
import com.octagon_technologies.sky_weather.repository.database.location.LocationDao
import com.octagon_technologies.sky_weather.repository.database.toLocalLocation
import com.octagon_technologies.sky_weather.repository.network.location.LocationApi
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class LocationRepo @Inject constructor(
    private val locationDao: LocationDao,
    private val locationApi: LocationApi
) {

    val location = locationDao.getLocalLocation().map { it?.location }

    private val _searchLocationSuggestions = MutableLiveData<List<Location>>()
    val searchLocationSuggestions: LiveData<List<Location>> = _searchLocationSuggestions

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("MissingPermission")
    fun useGPSLocation(context: Context) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        Timber.d("gpsEnabled is $gpsEnabled")

        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)

        fusedLocationProviderClient
            .addOnSuccessListener {
                GlobalScope.launch {
                    getLocationNameFromCoordinates(lat = it.latitude, lon = it.longitude)
                }
            }
            .addOnCompleteListener {
                Timber.d("fusedLocationProviderClient.addOnCompleteListener called")
            }
    }

    suspend fun insertLocalLocation(location: Location) {
        locationDao.insertData(LocalLocation(location = location))
    }

    suspend fun getLocationSuggestionsFromQuery(query: String) {
        if (query.isEmpty() || query.isBlank())
            _searchLocationSuggestions.value = listOf()
        else _searchLocationSuggestions.value =
            locationApi.getLocationSuggestions(query = query).map { it.toLocation() }
    }


    private suspend fun getLocationNameFromCoordinates(lat: Double, lon: Double) {
        val location =
            locationApi.getLocationFromCoordinates(lat = lat, lon = lon)
                .toLocation()
        locationDao.insertData(location.toLocalLocation())
    }
}
