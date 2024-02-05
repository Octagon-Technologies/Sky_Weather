package com.octagon_technologies.sky_weather.di

import android.content.Context
import com.octagon_technologies.sky_weather.repository.repo.SettingsRepo
import com.octagon_technologies.sky_weather.widgets.WidgetRepo
import com.octagon_technologies.sky_weather.widgets.WidgetSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object WidgetRepoModule {

    @Singleton
    @Provides
    fun providesWidgetRepo(
        @ApplicationContext context: Context,
        widgetSettings: WidgetSettings,
        settingsRepo: SettingsRepo
    ) =
        WidgetRepo(context, widgetSettings, settingsRepo)

}