package com.octagontechnologies.sky_weather.repository.repo

import androidx.lifecycle.map
import com.octagontechnologies.sky_weather.domain.Location
import com.octagontechnologies.sky_weather.repository.database.location.recent.LocalRecentLocation
import com.octagontechnologies.sky_weather.repository.database.location.recent.RecentLocationDao
import timber.log.Timber
import javax.inject.Inject

class RecentLocationsRepo @Inject constructor(
    private val recentLocationDao: RecentLocationDao
) {

    val listOfRecentLocation =
        recentLocationDao.getAllLocalRecentLocations()
            .map { list -> list?.map { it.location }?.sortedBy { it.displayName } ?: listOf() }

    suspend fun insertLocalRecentLocation(location: Location) {
        Timber.d("insertLocalRecentLocation called")
        recentLocationDao.insertData(
            LocalRecentLocation(location.key, location)
        )
    }

    suspend fun removeLocalRecentLocation(location: Location) {
       recentLocationDao.deleteLocalRecentLocation(LocalRecentLocation(location.key, location))
    }

    suspend fun removeAllRecentLocations() =
        recentLocationDao.deleteAllRecentLocations()

}