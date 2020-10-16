package com.example.kotlinweatherapp.ui.weather_forecast_objects

import com.example.kotlinweatherapp.database.FavouriteLocationDatabaseClass
import com.example.kotlinweatherapp.database.MainDataBase
import com.example.kotlinweatherapp.network.location.LocationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import kotlin.collections.ArrayList

object MainFavouriteLocationsObject {

    suspend fun insertFavouriteLocationToLocalStorage(
        mainDataBase: MainDataBase?,
        locationItem: LocationItem
    ): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val favouriteLocationDatabaseClass =
                    FavouriteLocationDatabaseClass(locationItem.placeId.toString(), locationItem)
                mainDataBase?.favouriteLocationDao?.insertFavouriteLocationDatabaseClass(
                    favouriteLocationDatabaseClass
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
                val favouriteLocationDatabaseClass =
                    FavouriteLocationDatabaseClass(locationItem.placeId.toString(), locationItem)
                mainDataBase?.favouriteLocationDao?.deleteFavouriteLocationDatabaseClass(
                    favouriteLocationDatabaseClass
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
                 val arrayOfLocationItems = ArrayList<LocationItem>()
                 val list = mainDataBase?.favouriteLocationDao?.getFavouriteLocationsDatabaseClass()
                 list?.forEach {
                     arrayOfLocationItems.add(it.favouriteLocation)
                 }

                 arrayOfLocationItems
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