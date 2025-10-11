package com.octagontechnologies.sky_weather.repository.network.location

import com.octagontechnologies.sky_weather.repository.network.location.models.SearchLocationResponse
import com.octagontechnologies.sky_weather.repository.network.location.reverse_geocoding_location.ReverseGeoCodingLocation
import retrofit2.http.GET
import retrofit2.http.Query

const val LOCATION_API_BASE_URL = "https://us1.locationiq.com/v1/"

// TODO: REMOVE THIS AND PLACE IT IN SECRETS
const val LOCATION_AP_KEY = "2a13f417c6d3f3"

interface LocationApi {
    //  https://api.locationiq.com/v1/autocomplete.php?key=2a13f417c6d3f3&q=Miller%20Estate&limit=10
    @GET("autocomplete.php")
    suspend fun getLocationSuggestions(
        @Query("key") key: String = LOCATION_AP_KEY,
        @Query("q") query: String,
        @Query("limit") limit: Int = 10,
    ): List<SearchLocationResponse>

    //  https://us1.locationiq.com/v1/reverse.php?key=2a13f417c6d3f3&lat=-1.3135887888876425&lon=36.81903851535387&zoom=16&format=json
    @GET("reverse.php")
    suspend fun getLocationFromCoordinates(
        @Query("key") key: String = LOCATION_AP_KEY,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("zoom") zoom: Int = 16,
        @Query("format") format: String = "json",
    ): ReverseGeoCodingLocation
}
