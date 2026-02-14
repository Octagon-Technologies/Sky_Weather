package com.octagontechnologies.sky_weather.data.local.room.location.favorites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagontechnologies.sky_weather.domain.model.Location

@Entity(tableName = "localFavoriteLocation")
data class LocalFavouriteLocation(
    @PrimaryKey(autoGenerate = false)
    val favouriteLocationKey: String,
    @ColumnInfo
    val location: Location,
)
