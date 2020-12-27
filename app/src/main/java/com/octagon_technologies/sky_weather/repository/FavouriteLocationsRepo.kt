package com.octagon_technologies.sky_weather.repository

import com.octagon_technologies.sky_weather.repository.database.FavouriteLocationDatabaseClass
import com.octagon_technologies.sky_weather.repository.database.WeatherDataBase
import com.octagon_technologies.sky_weather.repository.network.location.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException

object FavouriteLocationsRepo {

    suspend fun insertFavouriteLocationToLocalStorage(
        weatherDataBase: WeatherDataBase?,
        location: Location
    ): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                weatherDataBase?.favouriteLocationDao?.insertFavouriteLocationDatabaseClass(
                    FavouriteLocationDatabaseClass(location.placeId.toString(), location)
                )

                true
            }
        } catch (e: Exception) {
            Timber.e(e)
            false
        }
    }

    suspend fun removeFavouriteLocationToLocalStorage(
        weatherDataBase: WeatherDataBase?,
        location: Location
    ): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                weatherDataBase?.favouriteLocationDao?.deleteFavouriteLocationDatabaseClass(
                    FavouriteLocationDatabaseClass(location.placeId.toString(), location)
                )

                true
            }
        } catch (e: Exception) {
            Timber.e(e)
            false
        }
    }

    suspend fun removeAllFavouriteLocations(
        weatherDataBase: WeatherDataBase?,
        listOfFavouriteLocations: ArrayList<Location>?
    ): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                weatherDataBase?.favouriteLocationDao?.deleteAllFavourite(
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

    suspend fun getFavouriteLocationsAsync(weatherDataBase: WeatherDataBase?): ArrayList<Location>? {
        return try {
            withContext(Dispatchers.IO) {
                val localFavouriteLocations = weatherDataBase?.favouriteLocationDao?.getFavouriteLocationsDatabaseClass()?.map {
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