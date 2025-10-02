package com.octagontechnologies.sky_weather.repository.database.location.favorites

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.octagontechnologies.sky_weather.repository.database.BaseDao

@Dao
abstract class FavouriteLocationDao: BaseDao<LocalFavouriteLocation> {
//    @Query("DELETE FROM localFavoriteLocation WHERE favouriteLocationKey = :favoriteLocationKey")
    @Delete
    abstract suspend fun deleteLocalFavouriteLocation(localFavouriteLocation: LocalFavouriteLocation)

    @Query("SELECT * FROM localFavoriteLocation")
    abstract fun getAllLocalFavouriteLocations(): LiveData<List<LocalFavouriteLocation>>

    @Query("DELETE FROM localFavoriteLocation")
    abstract suspend fun deleteAllFavouriteLocations()
}