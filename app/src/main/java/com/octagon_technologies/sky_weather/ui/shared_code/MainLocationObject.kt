package com.octagon_technologies.sky_weather.ui.shared_code

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.octagon_technologies.sky_weather.database.LocationDatabaseClass
import com.octagon_technologies.sky_weather.database.MainDataBase
import com.octagon_technologies.sky_weather.network.LocationRetrofitItem
import com.octagon_technologies.sky_weather.network.location.LocationItem
import com.octagon_technologies.sky_weather.network.mockLat
import com.octagon_technologies.sky_weather.network.mockLon
import com.octagon_technologies.sky_weather.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.ui.find_location.Coordinates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

object MainLocationObject {

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

    suspend fun getLocationSuggestionsFromQuery(query: String): Map<String?, LocationItem> {
        return withContext(Dispatchers.IO) {
            try {
                LocationRetrofitItem.locationRetrofitService.getLocationSuggestionsAsync(query = query)
                    .await().map {
                        (it.address?.suburb ?: it.address?.city ?: it.address?.state) to it
                    }.toMap()
            } catch (noNetworkException: UnknownHostException) {
                mapOf()
            }
        }

    }

    suspend fun getLocationNameFromCoordinatesAsync(
        coordinates: Coordinates,
        mainDataBase: MainDataBase?
    ): ReverseGeoCodingLocation? {
        return withContext(Dispatchers.IO) {
            try {
                val remoteReversedLocation =
                    LocationRetrofitItem.locationRetrofitService.getLocationNameFromCoordinatesAsync(
                        lat = coordinates.lat,
                        lon = coordinates.lon
                    ).await()

                insertLocationToLocalStorage(mainDataBase, remoteReversedLocation)

                remoteReversedLocation
            } catch (e: HttpException) {
                Timber.e(e)
                getLocalLocationAsync(mainDataBase)
            } catch (e: UnknownHostException) {
                Timber.e(e)
                getLocalLocationAsync(mainDataBase)
            } catch (e: Exception) {
                throw e
            }
        }
    }


    suspend fun insertLocationToLocalStorage(
        mainDataBase: MainDataBase?,
        reversedLocation: ReverseGeoCodingLocation
    ) {
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