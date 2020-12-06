package com.octagon_technologies.sky_weather.repository

import com.octagon_technologies.sky_weather.repository.database.MainDataBase
import com.octagon_technologies.sky_weather.repository.database.RecentLocationDatabaseClass
import com.octagon_technologies.sky_weather.repository.network.location.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException

object RecentLocationsRepo {

    suspend fun insertRecentLocationToLocalStorage(
        mainDataBase: MainDataBase?,
        location: Location
    ): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                mainDataBase?.recentLocationDao?.insertRecentLocationDatabaseClass(
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
        mainDataBase: MainDataBase?,
        location: Location
    ): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                mainDataBase?.recentLocationDao?.deleteRecentLocationDatabaseClass(
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
        mainDataBase: MainDataBase?,
        listOfRecentLocations: ArrayList<Location>?
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

    suspend fun getRecentLocationsAsync(mainDataBase: MainDataBase?): ArrayList<Location>? {
        return try {
            withContext(Dispatchers.IO) {
                val localRecentLocations =
                    mainDataBase?.recentLocationDao?.getRecentLocationsDatabaseClass()?.map {
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