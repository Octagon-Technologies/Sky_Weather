package com.octagon_technologies.sky_weather.repository

import com.octagon_technologies.sky_weather.repository.database.FavouriteLocationDatabaseClass
import com.octagon_technologies.sky_weather.repository.database.MainDataBase
import com.octagon_technologies.sky_weather.repository.network.location.LocationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException

object FavouriteLocationsRepo {

    suspend fun insertFavouriteLocationToLocalStorage(
        mainDataBase: MainDataBase?,
        locationItem: LocationItem
    ): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                mainDataBase?.favouriteLocationDao?.insertFavouriteLocationDatabaseClass(
                    FavouriteLocationDatabaseClass(locationItem.placeId.toString(), locationItem)
                )

                true
            }
        } catch (e: Exception) {
            Timber.e(e)
            false
        }
    }

    suspend fun removeFavouriteLocationToLocalStorage(
        mainDataBase: MainDataBase?,
        locationItem: LocationItem
    ): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                mainDataBase?.favouriteLocationDao?.deleteFavouriteLocationDatabaseClass(
                    FavouriteLocationDatabaseClass(locationItem.placeId.toString(), locationItem)
                )

                true
            }
        } catch (e: Exception) {
            Timber.e(e)
            false
        }
    }

    suspend fun removeAllFavouriteLocations(
        mainDataBase: MainDataBase?,
        listOfFavouriteLocations: ArrayList<LocationItem>?
    ): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                mainDataBase?.favouriteLocationDao?.deleteAllFavourite(
                    listOfFavouriteLocations?.map {
                        FavouriteLocationDatabaseClass(it.placeId ?: "", it)
                    } ?: listOf()
                )

                true
            }
        } catch (e: Exception) {
            Timber.e(e)
            false
        }
    }

    suspend fun getFavouriteLocationsAsync(mainDataBase: MainDataBase?): ArrayList<LocationItem>? {
        return try {
            withContext(Dispatchers.IO) {
                val localFavouriteLocations = mainDataBase?.favouriteLocationDao?.getFavouriteLocationsDatabaseClass()?.map {
                    it.favouriteLocation
                }?.toMutableList()

                ArrayList(localFavouriteLocations ?: mutableListOf())
            }
        } catch (io: IOException) {
            Timber.e(io)
            return null
        } catch (e: Exception) {
            Timber.e(e)
            throw e
        }
    }

}