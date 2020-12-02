package com.octagon_technologies.sky_weather.repository.network.reverse_geocoding_location


import com.squareup.moshi.Json

data class ReverseGeoCodingAddress(
    @Json(name = "city")
    val city: String?,
    @Json(name = "country")
    val country: String?,
    @Json(name = "country_code")
    val countryCode: String?,
    @Json(name = "postcode")
    val postcode: String?,
    @Json(name = "residential")
    val residential: String?,
    @Json(name = "state")
    val state: String?,
    @Json(name = "suburb")
    val suburb: String?
)