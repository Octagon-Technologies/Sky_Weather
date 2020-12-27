package com.octagon_technologies.sky_weather.di

import android.content.Context
import com.octagon_technologies.sky_weather.repository.database.WeatherDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun providesWeatherDatabase(@ApplicationContext context: Context) =
        WeatherDataBase.getInstance(context)

}