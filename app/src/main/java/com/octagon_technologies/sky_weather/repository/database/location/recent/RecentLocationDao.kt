package com.octagon_technologies.sky_weather.repository.database.location.recent

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.octagon_technologies.sky_weather.repository.database.BaseDao


@Dao
abstract class RecentLocationDao: BaseDao<LocalRecentLocation> {
    @Query("DELETE FROM localRecentLocation WHERE recentLocationKey = :recentLocationKey")
    abstract suspend fun deleteLocalRecentLocation(recentLocationKey: String)

    @Query("SELECT * FROM localRecentLocation")
    abstract fun getAllLocalRecentLocations(): LiveData<List<LocalRecentLocation>?>

    @Query("DELETE FROM localRecentLocation")
    abstract suspend fun deleteAllRecentLocations()
}