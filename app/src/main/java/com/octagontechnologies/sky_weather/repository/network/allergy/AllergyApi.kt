package com.octagontechnologies.sky_weather.repository.network.allergy

import com.octagontechnologies.sky_weather.repository.network.allergy.models.AllergyResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


const val AllergyBaseUrl = "https://api.ambeedata.com/"
const val AllergyApiKey = "c33399e38f0ef34bf95530b31071e4405a95ee3102d5b609895d6b0995e3f078"

interface AllergyApi {
    @Headers(
        "Content-Type: application/json",
        "x-api-key: $AllergyApiKey"
    )
    @GET("latest/pollen/by-lat-lng")
    suspend fun getAllergyResponse(
        @Query("lat") lat: Double,
        @Query("lng") lon: Double
    ): AllergyResponse
}