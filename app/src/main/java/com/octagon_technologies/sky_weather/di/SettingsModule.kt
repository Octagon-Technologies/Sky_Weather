package com.octagon_technologies.sky_weather.di

import android.content.Context
import com.octagon_technologies.sky_weather.repository.SettingsRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object SettingsModule {

    @Singleton
    @Provides
    fun provideSettingsRepo(@ApplicationContext context: Context) =
        SettingsRepo(context)

}