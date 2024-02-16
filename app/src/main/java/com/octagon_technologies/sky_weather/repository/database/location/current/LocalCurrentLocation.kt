package com.octagon_technologies.sky_weather.repository.database.location.current

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagon_technologies.sky_weather.domain.Location

@Entity(tableName = "localCurrentLocation")
data class LocalCurrentLocation(
    @PrimaryKey(autoGenerate = false)
    val currentLocationKey: Int = 10,

    @ColumnInfo
    val currentLocation: Location
)
