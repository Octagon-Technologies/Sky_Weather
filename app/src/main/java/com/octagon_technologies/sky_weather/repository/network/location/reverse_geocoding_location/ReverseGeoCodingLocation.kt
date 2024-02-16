package com.octagon_technologies.sky_weather.repository.network.location.reverse_geocoding_location


import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.utils.capitalize
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class ReverseGeoCodingLocation(
    @Json(name = "address")
    val reverseGeoCodingAddress: ReverseGeoCodingAddress?,
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
) {

    private fun getDisplayNameWithoutCode() =
            if (reverseGeoCodingAddress != null)
                with(reverseGeoCodingAddress) {
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
                    }
                    else if (city != null)
                        "$city"
                    else
                        country?.capitalize()
                }
                    ?: "Unknown Location"
            else
                "Unknown Location"


    fun toLocation(): Location {
        // Formatting the location to something small enough to fit but still adequately descriptive
        val displayNameWithoutCountryCode = getDisplayNameWithoutCode()

        return Location(
            displayNameWithoutCountryCode = displayNameWithoutCountryCode,
            lat = lat!!,
            lon = lon!!,
            country = reverseGeoCodingAddress?.country ?: "",
            countryCode = reverseGeoCodingAddress?.countryCode ?: "--",
            isGps = true
        )
    }
}
