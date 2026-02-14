package com.octagontechnologies.sky_weather.data.local.room.location

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagontechnologies.sky_weather.domain.model.Location

@Entity(tableName = "localLocation")
data class LocalLocation(
    @PrimaryKey(autoGenerate = false)
    val locationKey: Int = 40,
    @ColumnInfo
    val location: Location,
)
