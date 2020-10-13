package com.example.kotlinweatherapp.ui.weather_forecast_objects

import android.annotation.SuppressLint
import android.content.Context
import com.example.kotlinweatherapp.database.LocationDatabaseClass
import com.example.kotlinweatherapp.database.MainDataBase
import com.example.kotlinweatherapp.network.LocationItem
import com.example.kotlinweatherapp.network.location.Location
import com.example.kotlinweatherapp.network.mockLat
import com.example.kotlinweatherapp.network.mockLon
import com.example.kotlinweatherapp.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.example.kotlinweatherapp.ui.find_location.Coordinates
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

object MainLocationObject{

    @SuppressLint("MissingPermission")
    fun turnOnGPS(context: Context): Coordinates {
        var coordinates: Coordinates? = null
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
            .getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)

        fusedLocationProviderClient.addOnSuccessListener {
            coordinates = Coordinates(lon = it.longitude, lat = it.latitude)
        }

        return coordinates ?: Coordinates(lon = mockLon, lat = mockLat)
    }

    suspend fun getLocationSuggestionsFromQuery(query: String): ArrayList<Location>? {
        return LocationItem.locationRetrofitService.getLocationSuggestionsAsync(query = query).await() as ArrayList<Location>?
    }

    suspend fun getLocationNameFromCoordinatesAsync(coordinates: Coordinates, mainDataBase: MainDataBase?): ReverseGeoCodingLocation? {
        return try {
            val remoteReversedLocation = LocationItem.locationRetrofitService.getLocationNameFromCoordinatesAsync(
                lat = coordinates.lat,
                lon = coordinates.lon
            ).await()

            insertLocationToLocalStorage(mainDataBase, remoteReversedLocation)

            remoteReversedLocation
        }
        catch (e: HttpException) {
            Timber.e(e)
            getLocalLocationAsync(mainDataBase)
        }
        catch (e: UnknownHostException) {
            Timber.e(e)
            getLocalLocationAsync(mainDataBase)
        }
        catch (e: Exception) {
            throw e
        }
    }


    private suspend fun insertLocationToLocalStorage(mainDataBase: MainDataBase?, reversedLocation: ReverseGeoCodingLocation) {
        withContext(Dispatchers.IO) {
            val locationDatabaseClass =
                LocationDatabaseClass(reversedLocation = reversedLocation)
            mainDataBase?.locationDao?.insertLocationDatabaseClass(locationDatabaseClass)
        }
    }

    suspend fun getLocalLocationAsync(mainDataBase: MainDataBase?): ReverseGeoCodingLocation? {
        return withContext(Dispatchers.IO) {
            mainDataBase?.locationDao?.getLocationDatabaseClass()?.reversedLocation
        }
    }

}