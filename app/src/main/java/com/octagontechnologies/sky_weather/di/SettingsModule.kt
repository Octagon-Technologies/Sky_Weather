package com.octagontechnologies.sky_weather.di

import android.content.Context
import com.octagontechnologies.sky_weather.repository.repo.SettingsRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {

    @Singleton
    @Provides
    fun provideSettingsRepo(@ApplicationContext context: Context) =
        SettingsRepo(context)

}