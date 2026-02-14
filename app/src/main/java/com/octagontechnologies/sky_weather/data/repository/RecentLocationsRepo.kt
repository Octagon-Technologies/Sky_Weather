package com.octagontechnologies.sky_weather.data.repository

import androidx.lifecycle.map
import com.octagontechnologies.sky_weather.data.local.room.location.recent.LocalRecentLocation
import com.octagontechnologies.sky_weather.data.local.room.location.recent.RecentLocationDao
import com.octagontechnologies.sky_weather.domain.model.Location
import timber.log.Timber
import javax.inject.Inject

class RecentLocationsRepo
    @Inject
    constructor(
        private val recentLocationDao: RecentLocationDao,
    ) {
        val listOfRecentLocation =
            recentLocationDao
                .getAllLocalRecentLocations()
                .map { list -> list?.map { it.location }?.sortedBy { it.displayName } ?: listOf() }

        suspend fun insertLocalRecentLocation(location: Location) {
            Timber.d("insertLocalRecentLocation called")
            recentLocationDao.insertData(
                LocalRecentLocation(location.key, location),
            )
        }

        suspend fun removeLocalRecentLocation(location: Location) {
            recentLocationDao.deleteLocalRecentLocation(LocalRecentLocation(location.key, location))
        }

        suspend fun removeAllRecentLocations() = recentLocationDao.deleteAllRecentLocations()
    }
