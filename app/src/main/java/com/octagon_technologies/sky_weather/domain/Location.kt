package com.octagon_technologies.sky_weather.domain

import com.octagon_technologies.sky_weather.utils.capitalize

data class Location(
    val displayNameWithoutCountryCode: String,
    val lat: String,
    val lon: String,
    val country: String,
    val countryCode: String,
    val isGps: Boolean = false
) {
    val key = "_$lat$lon"
    val displayName = displayNameWithoutCountryCode + ", " + countryCode.uppercase()
    val displayNameWithCountry = "$displayNameWithoutCountryCode, ${country.capitalize()}"

    fun getCoordinates() = "$lat,$lon"

}


//@Json(name = "city")
//val city: String?,
//@Json(name = "country")
//val country: String?,
//@Json(name = "country_code")
//val countryCode: String?,

//@Json(name = "address")
//val reverseGeoCodingAddress: ReverseGeoCodingAddress?,
//@Json(name = "boundingbox")
//val boundingbox: List<String>?,
//@Json(name = "display_name")
//val displayName: String?,
//@Json(name = "lat")
//val lat: String?,
//@Json(name = "licence")
//val licence: String?,
//@Json(name = "lon")
//val lon: String?,
//@Json(name = "osm_id")
//val osmId: String?,
//@Json(name = "osm_type")
//val osmType: String?,
//@Json(name = "place_id")
//val placeId: String?