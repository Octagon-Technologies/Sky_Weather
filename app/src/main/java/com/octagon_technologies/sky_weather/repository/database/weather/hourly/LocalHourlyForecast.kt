package com.octagon_technologies.sky_weather.repository.database.weather.hourly

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagon_technologies.sky_weather.domain.SingleForecast

@Entity(tableName = "localHourlyForecast")
data class LocalHourlyForecast(
        @PrimaryKey(autoGenerate = false)
        val hourlyForecastId: Int = 2,

        @ColumnInfo
        val listOfHourlyForecast: List<SingleForecast>

)

