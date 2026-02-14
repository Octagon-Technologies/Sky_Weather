package com.octagontechnologies.sky_weather.repository.weather_repo

/**
 * A repository interface for fetching weather data, specifically for WorkManager.
 * Each function returns a [Result] indicating the success or failure of the operation.
 * A failure
 *
 * @see WeatherRepo
 *
 */
interface WeatherRepo {
    suspend fun refreshCurrentForecast(): Result<Boolean>

    suspend fun refreshHourlyForecast(): Result<Boolean>

    suspend fun refreshDailyForecast(): Result<Boolean>

    suspend fun refreshLunarForecast(): Result<Boolean>
}
