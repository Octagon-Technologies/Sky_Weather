package com.octagon_technologies.sky_weather.repository.repo

import androidx.lifecycle.map
import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.repository.database.location.favorites.FavouriteLocationDao
import com.octagon_technologies.sky_weather.repository.database.location.favorites.LocalFavouriteLocation
import javax.inject.Inject

class FavouriteLocationRepo @Inject constructor(
    private val favouriteLocationDao: FavouriteLocationDao
) {

    val listOfFavouriteLocation =
        favouriteLocationDao.getAllLocalFavouriteLocations()
            .map { list -> list?.map { it.location }?.sortedBy { it.displayName } ?: listOf() }

    suspend fun insertLocalFavouriteLocation(
        location: Location
    ) {
        favouriteLocationDao.insertData(
            LocalFavouriteLocation(
                favouriteLocationKey = "${location.lat}${location.lon}",
                location = location
            )
        )
    }

    suspend fun removeLocalFavouriteLocation(
        location: Location
    ) {
        favouriteLocationDao.deleteLocalFavouriteLocation(
            LocalFavouriteLocation(
                favouriteLocationKey = "${location.lat}${location.lon}",
                location = location
            )
        )
    }

    suspend fun removeAllFavouriteLocations() =
        favouriteLocationDao.deleteAllFavouriteLocations()

}