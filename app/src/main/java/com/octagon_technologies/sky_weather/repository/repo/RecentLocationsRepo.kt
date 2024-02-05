package com.octagon_technologies.sky_weather.repository.repo

import androidx.lifecycle.map
import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.repository.database.location.recent.LocalRecentLocation
import com.octagon_technologies.sky_weather.repository.database.location.recent.RecentLocationDao
import javax.inject.Inject

class RecentLocationsRepo @Inject constructor(
    private val recentLocationDao: RecentLocationDao
) {

    val listOfRecentLocation =
        recentLocationDao.getAllLocalRecentLocations()
            .map { list -> list.map { it.location }.sortedBy { it.displayName } }

    suspend fun insertLocalRecentLocation(location: Location) {
        recentLocationDao.insertLocalRecentLocation(
            LocalRecentLocation(location.key, location)
        )
    }

    suspend fun removeLocalRecentLocation(location: Location) {
        recentLocationDao.deleteLocalRecentLocation(location.key)
    }

    suspend fun removeAllRecentLocations() =
        recentLocationDao.deleteAllRecentLocations()

}