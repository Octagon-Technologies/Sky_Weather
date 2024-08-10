package com.octagontechnologies.sky_weather.repository.database.weather.daily

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagontechnologies.sky_weather.domain.daily.DailyForecast

@Entity(tableName = "localDailyForecast")
data class LocalDailyForecast(
    @PrimaryKey(autoGenerate = false)
    val dailyForecastId: Int = 2,

    @ColumnInfo
    val listOfDailyForecast: List<DailyForecast>
)
