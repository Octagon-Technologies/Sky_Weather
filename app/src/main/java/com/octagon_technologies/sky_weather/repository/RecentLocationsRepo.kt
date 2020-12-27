package com.octagon_technologies.sky_weather.repository

import com.octagon_technologies.sky_weather.repository.database.WeatherDataBase
import com.octagon_technologies.sky_weather.repository.database.RecentLocationDatabaseClass
import com.octagon_technologies.sky_weather.repository.network.location.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException

object RecentLocationsRepo {

    suspend fun insertRecentLocationToLocalStorage(
        weatherDataBase: WeatherDataBase?,
        location: Location
    ): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                weatherDataBase?.recentLocationDao?.insertRecentLocationDatabaseClass(
                    RecentLocationDatabaseClass(location.placeId.toString(), location)
                )

                true
            }
        } catch (e: Exception) {
            Timber.e(e)
            false
        }
    }

    suspend fun removeRecentLocationToLocalStorage(
        weatherDataBase: WeatherDataBase?,
        location: Location
    ): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                weatherDataBase?.recentLocationDao?.deleteRecentLocationDatabaseClass(
                    RecentLocationDatabaseClass(location.placeId.toString(), location)
                )

                true
            }
        } catch (e: Exception) {
            Timber.e(e)
            false
        }
    }

    suspend fun removeAllRecentLocations(
        weatherDataBase: WeatherDataBase?,
        listOfRecentLocations: ArrayList<Location>?
    ): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                weatherDataBase?.recentLocationDao?.deleteAllRecent(
                    listOfRecentLocations?.map {
                        RecentLocationDatabaseClass(it.placeId ?: "", it)
                    } ?: listOf()
                )

                true
            }
        } catch (e: Exception) {
            Timber.e(e)
            false
        }
    }

    suspend fun getRecentLocationsAsync(weatherDataBase: WeatherDataBase?): ArrayList<Location>? {
        return try {
            withContext(Dispatchers.IO) {
                val localRecentLocations =
                    weatherDataBase?.recentLocationDao?.getRecentLocationsDatabaseClass()?.map {
                        it.recentLocation
                    }?.toMutableList()

                ArrayList(localRecentLocations ?: mutableListOf())
            }
        } catch (io: IOException) {
            Timber.e(io)
            null
        } catch (e: Exception) {
            Timber.e(e)
            throw e
        }
    }

}