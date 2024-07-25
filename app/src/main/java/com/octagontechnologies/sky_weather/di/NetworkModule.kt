package com.octagontechnologies.sky_weather.di

import com.octagontechnologies.sky_weather.repository.network.WEATHER_BASE_URL
import com.octagontechnologies.sky_weather.repository.network.WeatherApi
import com.octagontechnologies.sky_weather.repository.network.allergy.AllergyApi
import com.octagontechnologies.sky_weather.repository.network.allergy.AllergyBaseUrl
import com.octagontechnologies.sky_weather.repository.network.location.LocationApi
import com.octagontechnologies.sky_weather.repository.network.location.LocationBaseUrl
import com.octagontechnologies.sky_weather.repository.network.lunar.LunarBaseUrl
import com.octagontechnologies.sky_weather.repository.network.lunar.LunarForecastApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit.Builder
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    fun providesMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    fun providesOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .callTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .build()

    @Provides
    fun providesRetrofitBuilder(moshi: Moshi, okHttpClient: OkHttpClient): Builder = Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)

    @Provides
    fun providesWeatherApi(retrofitBuilder: Builder) = retrofitBuilder
        .baseUrl(WEATHER_BASE_URL).build().create<WeatherApi>()

    @Provides
    fun providesLocationApi(retrofitBuilder: Builder) = retrofitBuilder
        .baseUrl(LocationBaseUrl).build().create<LocationApi>()

    @Provides
    fun providesAllergyApi(retrofitBuilder: Builder) = retrofitBuilder
        .baseUrl(AllergyBaseUrl).build().create<AllergyApi>()

    @Provides
    fun providesLunarForecastApi(retrofitBuilder: Builder) = retrofitBuilder
        .baseUrl(LunarBaseUrl).build().create<LunarForecastApi>()
}