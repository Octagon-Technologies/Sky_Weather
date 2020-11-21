package com.octagon_technologies.sky_weather.ui.shared_code

import com.octagon_technologies.sky_weather.database.FavouriteLocationDatabaseClass
import com.octagon_technologies.sky_weather.database.MainDataBase
import com.octagon_technologies.sky_weather.database.RecentLocationDatabaseClass
import com.octagon_technologies.sky_weather.network.location.LocationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException

object MainRecentLocationsObject {

    suspend fun insertRecentLocationToLocalStorage(
        mainDataBase: MainDataBase?,
        locationItem: LocationItem
    ): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                mainDataBase?.recentLocationDao?.insertRecentLocationDatabaseClass(
                    RecentLocationDatabaseClass(locationItem.placeId.toString(), locationItem)
                )

                true
            }
        } catch (e: Exception) {
            Timber.e(e)
            false
        }
    }

    suspend fun removeRecentLocationToLocalStorage(
        mainDataBase: MainDataBase?,
        locationItem: LocationItem
    ): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                mainDataBase?.recentLocationDao?.deleteRecentLocationDatabaseClass(
                    RecentLocationDatabaseClass(locationItem.placeId.toString(), locationItem)
                )

                true
            }
        } catch (e: Exception) {
            Timber.e(e)
            false
        }
    }

    suspend fun removeAllRecentLocations(
        mainDataBase: MainDataBase?,
        listOfRecentLocations: ArrayList<LocationItem>?
    ): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                mainDataBase?.recentLocationDao?.deleteAllRecent(
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

    suspend fun getRecentLocationsAsync(mainDataBase: MainDataBase?): ArrayList<LocationItem>? {
        return try {
            withContext(Dispatchers.IO) {
                val localRecentLocations = mainDataBase?.recentLocationDao?.getRecentLocationsDatabaseClass()?.map {
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