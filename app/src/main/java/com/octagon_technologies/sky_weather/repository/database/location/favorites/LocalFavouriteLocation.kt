package com.octagon_technologies.sky_weather.repository.database.location.favorites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagon_technologies.sky_weather.domain.Location

@Entity(tableName = "localFavoriteLocation")
data class LocalFavouriteLocation(
        @PrimaryKey(autoGenerate = false)
        val favouriteLocationKey: String,

        @ColumnInfo
        val location: Location
)