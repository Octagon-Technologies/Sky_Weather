package com.octagontechnologies.sky_weather.data.repository

import androidx.lifecycle.map
import com.octagontechnologies.sky_weather.data.local.room.location.favorites.FavouriteLocationDao
import com.octagontechnologies.sky_weather.data.local.room.location.favorites.LocalFavouriteLocation
import com.octagontechnologies.sky_weather.domain.model.Location
import javax.inject.Inject

class FavouriteLocationRepo
    @Inject
    constructor(
        private val favouriteLocationDao: FavouriteLocationDao,
    ) {
        val listOfFavouriteLocation =
            favouriteLocationDao
                .getAllLocalFavouriteLocations()
                .map { list -> list?.map { it.location }?.sortedBy { it.displayName } ?: listOf() }

        suspend fun addOrRemoveFromFavourites(location: Location) {
            if (location in (listOfFavouriteLocation.value ?: listOf())) {
                removeLocalFavouriteLocation(location)
            } else {
                insertLocalFavouriteLocation(location)
            }
        }

        private suspend fun insertLocalFavouriteLocation(location: Location) {
            favouriteLocationDao.insertData(
                LocalFavouriteLocation(
                    favouriteLocationKey = location.key,
                    location = location,
                ),
            )
        }

        private suspend fun removeLocalFavouriteLocation(location: Location) {
            favouriteLocationDao.deleteLocalFavouriteLocation(
                LocalFavouriteLocation(
                    location.key,
                    location,
                ),
            )
        }

        suspend fun removeAllFavouriteLocations() = favouriteLocationDao.deleteAllFavouriteLocations()
    }
