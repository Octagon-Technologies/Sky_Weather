package com.octagontechnologies.sky_weather.di

import com.octagontechnologies.sky_weather.data.repository.WeatherRepoImpl
import com.octagontechnologies.sky_weather.domain.repository.WeatherRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindWeatherRepo(weatherRepoImpl: WeatherRepoImpl): WeatherRepo
}
