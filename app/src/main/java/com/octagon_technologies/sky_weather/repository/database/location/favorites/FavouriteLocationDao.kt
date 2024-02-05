package com.octagon_technologies.sky_weather.repository.database.location.favorites

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.octagon_technologies.sky_weather.repository.database.LocalFavouriteLocation

@Dao
interface FavouriteLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalFavouriteLocation(localFavouriteLocation: LocalFavouriteLocation)

    @Query("DELETE FROM localFavoriteLocation WHERE favouriteLocationKey = :favoriteLocationKey")
    suspend fun deleteLocalFavouriteLocation(favoriteLocationKey: LocalFavouriteLocation)

    @Query("SELECT * FROM localFavoriteLocation")
    fun getAllLocalFavouriteLocations(): LiveData<List<LocalFavouriteLocation>>

    @Query("DELETE FROM localFavoriteLocation")
    suspend fun deleteAllFavouriteLocations()
}