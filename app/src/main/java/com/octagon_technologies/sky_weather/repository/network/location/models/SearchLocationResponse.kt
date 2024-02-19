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

                // General format: Display Place, Suburb(if there), City, State (optional), Country
                // If the full API display name is less than 25 letters, use it
                this@SearchLocationResponse.displayName

                // User is searching for a city e.g Nairobi
                if (displayPlace == city)
                    displayPlace

                // e.g: Limuru Road, Parklands, Nairobi
                if (suburb != null && city != null)
                    "$displayPlace, $suburb, $city"

                // The city name is repeated in the state; do not display the state
                if (state != null && city != null) {
                    if (state.split(" ").contains(city))
                    // Limuru Road, Kiambu
                        "$displayPlace, $city"
                    else
                    // Limuru Road, Ruaka, Kiambu
                        "$displayPlace, $city, $state"
                }

                // The city name is null; display the state
                if (state != null && city == null)
                // Limuru, Kiambu
                    "$displayPlace, $state"

                // If everything fails, return the display name... but remove the country
                displayName?.split(", ")?.dropLast(1)?.joinToString(", ")
            } ?: "Unknown Location"
    else "Unknown Location"

    fun toLocation() = Location(
        getDisplayNameWithoutCode(),
        lat!!,
        lon!!,
        address?.country ?: "",
        address?.countryCode ?: "--"
    )
}