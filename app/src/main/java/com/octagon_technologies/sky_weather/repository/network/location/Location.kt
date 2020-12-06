package com.octagon_technologies.sky_weather.repository.network.location


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "address")
    val address: Address?,
    @Json(name = "boundingbox")
    val boundingbox: List<String>?,
    @Json(name = "class")
    val classX: String?,
    @Json(name = "display_address")
    val displayAddress: String?,
    @Json(name = "display_name")
    val displayName: String?,
    @Json(name = "display_place")
    val displayPlace: String?,
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
    val placeId: String?,
    @Json(name = "type")
    val type: String?
)