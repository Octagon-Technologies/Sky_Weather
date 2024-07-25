package com.octagontechnologies.sky_weather.repository.database.allergy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagontechnologies.sky_weather.domain.Allergy

@Entity(tableName = "localAllergy")
data class LocalAllergy(
    @PrimaryKey(autoGenerate = false)
    val allergyKey: Int = 10,

    @ColumnInfo
    val allergy: Allergy
)