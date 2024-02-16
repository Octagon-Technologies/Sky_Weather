package com.octagon_technologies.sky_weather.repository.network.location.models


import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.utils.capitalize
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class SearchLocationResponse(
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
) {

    private fun getDisplayNameWithoutCode() =
        if (address != null)
            with(address) {
                countryCode = countryCode?.uppercase(Locale.getDefault())

                if (suburb != null && city != null)
                    "$suburb, $city"
                else if (state != null)
                    "$state"
                else if (displayName?.length != null && displayName.length <= 16) {
                    try {
                        val miniList = displayName.split(",").subList(0, 1)
                        miniList.joinToString(", ") { it.capitalize() }
                    } catch (e: Exception) {
                        displayName
                    }
                } else if (city != null)
                    "$city"
                else
                    country?.capitalize()
            }
                ?: "Unknown Location"
        else
            "Unknown Location"

    fun toLocation() = Location(
        getDisplayNameWithoutCode(),
        lat!!,
        lon!!,
        address?.country ?: "",
        address?.countryCode ?: "--"
    )
}