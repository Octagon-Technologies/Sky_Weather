package com.octagontechnologies.sky_weather.data.local.room.weather.current

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagontechnologies.sky_weather.domain.model.SingleForecast

@Entity(tableName = "localCurrentForecast")
data class LocalCurrentForecast(
    @PrimaryKey(autoGenerate = false)
    val currentForecastId: Int = 1,
    @ColumnInfo
    val currentForecast: SingleForecast,
)
