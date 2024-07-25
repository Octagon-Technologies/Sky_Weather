package com.octagontechnologies.sky_weather.di

import android.content.Context
import com.octagontechnologies.sky_weather.repository.database.WeatherDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun providesWeatherDatabase(@ApplicationContext context: Context) =
        WeatherDataBase.getInstance(context)

    @Provides
    fun providesCurrentForecastDao(weatherDataBase: WeatherDataBase) =
        weatherDataBase.currentForecastDao

    @Provides
    fun providesHourlyForecastDao(weatherDataBase: WeatherDataBase) =
        weatherDataBase.hourlyDao

    @Provides
    fun providesDailyForecastDao(weatherDataBase: WeatherDataBase) =
        weatherDataBase.dailyDao

    @Provides
    fun providesLocationDao(weatherDataBase: WeatherDataBase) =
        weatherDataBase.locationDao
    @Provides
    fun providesCurrentLocationDao(weatherDataBase: WeatherDataBase) =
        weatherDataBase.currentLocationDao
    @Provides
    fun providesFavoriteLocationDao(weatherDataBase: WeatherDataBase) =
        weatherDataBase.favouriteLocationDao
    @Provides
    fun providesRecentLocationDao(weatherDataBase: WeatherDataBase) =
        weatherDataBase.recentLocationDao

    @Provides
    fun providesAllergyDao(weatherDataBase: WeatherDataBase) =
        weatherDataBase.allergyDao

    @Provides
    fun providesLunarForecastDao(weatherDataBase: WeatherDataBase) =
        weatherDataBase.lunarDao

}