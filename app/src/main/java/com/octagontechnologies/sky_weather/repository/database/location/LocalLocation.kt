package com.octagontechnologies.sky_weather.repository.database.location

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagontechnologies.sky_weather.domain.Location

@Entity(tableName = "localLocation")
data class LocalLocation(
        @PrimaryKey(autoGenerate = false)
        val locationKey: Int = 40,

        @ColumnInfo
        val location: Location
)