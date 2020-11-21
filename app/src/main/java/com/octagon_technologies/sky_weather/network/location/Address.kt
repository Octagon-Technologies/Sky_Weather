package com.octagon_technologies.sky_weather.network.location


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Address(
    @Json(name = "city")
    val city: String?,
    @Json(name = "country")
    val country: String?,
    @Json(name = "country_code")
    val countryCode: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "postcode")
    val postcode: String?,
    @Json(name = "road")
    val road: String?,
    @Json(name = "state")
    val state: String?,
    @Json(name = "suburb")
    val suburb: String?
)