package com.octagontechnologies.sky_weather.data.local.room.weather.hourly

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagontechnologies.sky_weather.domain.model.SingleForecast

@Entity(tableName = "localHourlyForecast")
data class LocalHourlyForecast(
    @PrimaryKey(autoGenerate = false)
    val hourlyForecastId: Int = 2,
    @ColumnInfo
    val listOfHourlyForecast: List<SingleForecast>,
)
