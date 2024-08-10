package com.octagontechnologies.sky_weather.repository.database.location.recent

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.octagontechnologies.sky_weather.repository.database.BaseDao


@Dao
abstract class RecentLocationDao: BaseDao<LocalRecentLocation> {
    @Delete
    abstract suspend fun deleteLocalRecentLocation(recentLocation: LocalRecentLocation)

    @Query("SELECT * FROM localRecentLocation")
    abstract fun getAllLocalRecentLocations(): LiveData<List<LocalRecentLocation>?>

    @Query("DELETE FROM localRecentLocation")
    abstract suspend fun deleteAllRecentLocations()
}