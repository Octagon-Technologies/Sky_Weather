package com.octagon_technologies.sky_weather.repository

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.octagon_technologies.sky_weather.models.Coordinates
import com.octagon_technologies.sky_weather.repository.database.LocationDatabaseClass
import com.octagon_technologies.sky_weather.repository.database.WeatherDataBase
import com.octagon_technologies.sky_weather.repository.network.LocationRetrofitItem
import com.octagon_technologies.sky_weather.repository.network.location.Location
import com.octagon_technologies.sky_weather.repository.network.mockLat
import com.octagon_technologies.sky_weather.repository.network.mockLon
import com.octagon_technologies.sky_weather.repository.network.reverse_geocoding_location.ReverseGeoCodingLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

object LocationRepo {

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

    suspend fun getLocationSuggestionsFromQuery(query: String): Map<String?, Location> {
        return withContext(Dispatchers.IO) {
            try {
                LocationRetrofitItem.locationRetrofitService.getLocationSuggestionsAsync(query = query)
                    .map {
                        (it.address?.suburb ?: it.address?.city ?: it.address?.state) to it
                    }.toMap()
            } catch (noNetworkException: UnknownHostException) {
                mapOf()
            }
        }

    }

    suspend fun getLocationNameFromCoordinatesAsync(
        coordinates: Coordinates,
        weatherDataBase: WeatherDataBase?
    ): ReverseGeoCodingLocation? {
        return withContext(Dispatchers.IO) {
            try {
                val remoteReversedLocation =
                    LocationRetrofitItem.locationRetrofitService.getLocationNameFromCoordinatesAsync(
                        lat = coordinates.lat,
                        lon = coordinates.lon
                    )

                insertLocationToLocalStorage(weatherDataBase, remoteReversedLocation)

                remoteReversedLocation
            } catch (e: HttpException) {
                Timber.e(e)
                getLocalLocationAsync(weatherDataBase)
            } catch (e: UnknownHostException) {
                Timber.e(e)
                getLocalLocationAsync(weatherDataBase)
            } catch (e: Exception) {
                throw e
            }
        }
    }


    suspend fun insertLocationToLocalStorage(
        weatherDataBase: WeatherDataBase?,
        reversedLocation: ReverseGeoCodingLocation
    ) {
        withContext(Dispatchers.IO) {
            val locationDatabaseClass =
                LocationDatabaseClass(reversedLocation = reversedLocation)
            weatherDataBase?.locationDao?.insertLocationDatabaseClass(locationDatabaseClass)
        }
    }

    suspend fun getLocalLocationAsync(weatherDataBase: WeatherDataBase?): ReverseGeoCodingLocation? {
        return withContext(Dispatchers.IO) {
            weatherDataBase?.locationDao?.getLocationDatabaseClass()?.reversedLocation
        }
    }

}