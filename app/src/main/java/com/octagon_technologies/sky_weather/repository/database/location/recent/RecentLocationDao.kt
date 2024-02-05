package com.octagon_technologies.sky_weather.repository.database.location.recent

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.octagon_technologies.sky_weather.repository.database.LocalRecentLocation


@Dao
interface RecentLocationDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalRecentLocation(localRecentLocation: LocalRecentLocation)

    @Query("DELETE FROM localRecentLocation WHERE recentLocationKey = :recentLocationKey")
    suspend fun deleteLocalRecentLocation(recentLocationKey: String)

    @Query("SELECT * FROM localRecentLocation")
    fun getAllLocalRecentLocations(): LiveData<List<LocalRecentLocation>>

    @Query("DELETE FROM localRecentLocation")
    suspend fun deleteAllRecentLocations()
}