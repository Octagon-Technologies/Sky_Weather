package com.octagon_technologies.sky_weather.di

import com.octagon_technologies.sky_weather.repository.network.location.LocationApi
import com.octagon_technologies.sky_weather.repository.network.location.LocationBaseUrl
import com.octagon_technologies.sky_weather.repository.network.weather.TomorrowApi
import com.octagon_technologies.sky_weather.repository.network.weather.TomorrowBaseUrl
import com.octagon_technologies.sky_weather.repository.network.allergy.AllergyApi
import com.octagon_technologies.sky_weather.repository.network.allergy.AllergyBaseUrl
import com.octagon_technologies.sky_weather.repository.network.lunar.LunarBaseUrl
import com.octagon_technologies.sky_weather.repository.network.lunar.LunarForecastApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit.Builder
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

@InstallIn(ActivityComponent::class)
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
    fun providesRetrofitBuilder(moshi: Moshi, okHttpClient: OkHttpClient) = Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)

    @Provides
    fun providesTomorrowApi(retrofitBuilder: Builder) = retrofitBuilder
        .baseUrl(TomorrowBaseUrl).build().create<TomorrowApi>()

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