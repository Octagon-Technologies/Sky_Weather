package com.octagon_technologies.sky_weather.repository.network.location.models


import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.repository.network.location.reverse_geocoding_location.ReverseGeoCodingAddress
import com.octagon_technologies.sky_weather.repository.network.location.reverse_geocoding_location.ReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.utils.capitalize
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

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
    fun toReverseGeoCodingLocation(): ReverseGeoCodingLocation {
        val reverseGeoCodingAddress = with(address!!) {
            ReverseGeoCodingAddress(city, country, countryCode, postcode, null, state, suburb)
        }
        return ReverseGeoCodingLocation(
            reverseGeoCodingAddress,
            boundingbox,
            displayName,
            lat,
            licence,
            lon,
            osmId,
            osmType,
            placeId
        )
    }

    fun toLocation(): Location {
        val displayNameWithoutCountryCode =
            if (address != null)
                with(address) {
                    if (suburb != null && countryCode != null) "$suburb, $countryCode"
                    else if(displayName?.length != null && displayName.length <= 18)
                        displayName.split(",")[0].capitalize()
                    else if(city != null) "$city, $countryCode"
                    else country?.capitalize()
                } ?: "Unknown Location"
            else "Unknown Location"

        return Location(
            displayNameWithoutCountryCode,
            lat!!,
            lon!!,
            address?.country ?: "",
            address?.countryCode ?: "--"
        )
    }
}