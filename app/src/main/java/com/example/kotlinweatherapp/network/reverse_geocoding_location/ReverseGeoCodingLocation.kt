package com.example.kotlinweatherapp.network.reverse_geocoding_location


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class ReverseGeoCodingLocation(
    @Json(name = "address")
    val address: Address?,
    @Json(name = "boundingbox")
    val boundingbox: List<String>?,
    @Json(name = "display_name")
    val displayName: String?,
    @Json(name = "lat")
    val lat: String?,
    @Json(name = "licence")
    val licence: String?,
    @Json(name = "lon")
    val lon: String?,
    @Json(name = "osm_id")
    val osmId: String?,
    @Json(name = "osm_type")
    val osmType: String?,
    @Json(name = "place_id")
    val placeId: String?
)