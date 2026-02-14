package com.octagontechnologies.sky_weather.data.local.room.location.current

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagontechnologies.sky_weather.domain.model.Location

@Entity(tableName = "localCurrentLocation")
data class CurrentLocation(
    @PrimaryKey(autoGenerate = false)
    val currentLocationKey: Int = 10,
    @ColumnInfo
    val currentLocation: Location,
)
