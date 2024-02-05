package com.octagon_technologies.sky_weather.repository.database.location.recent

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagon_technologies.sky_weather.domain.Location

@Entity(tableName = "localRecentLocation")
data class LocalRecentLocation(
    @PrimaryKey(autoGenerate = false)
    val recentLocationKey: String,

    @ColumnInfo
    val location: Location
)