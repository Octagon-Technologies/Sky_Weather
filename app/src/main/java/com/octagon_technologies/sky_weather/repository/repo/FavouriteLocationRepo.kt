package com.octagon_technologies.sky_weather.repository.repo

import androidx.lifecycle.map
import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.repository.database.LocalFavouriteLocation
import com.octagon_technologies.sky_weather.repository.database.location.favorites.FavouriteLocationDao
import javax.inject.Inject

class FavouriteLocationRepo @Inject constructor(
    private val favouriteLocationDao: FavouriteLocationDao
) {

    val listOfFavouriteLocation =
        favouriteLocationDao.getAllLocalFavouriteLocations()
            .map { list -> list.map { it.location }.sortedBy { it.displayName } }

    suspend fun insertLocalFavouriteLocation(
        location: Location
    ) {
        favouriteLocationDao.insertLocalFavouriteLocation(
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