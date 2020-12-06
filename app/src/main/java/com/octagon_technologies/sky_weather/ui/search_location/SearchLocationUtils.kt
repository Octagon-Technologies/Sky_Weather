package com.octagon_technologies.sky_weather.ui.search_location

import com.octagon_technologies.sky_weather.repository.network.location.Location
import com.octagon_technologies.sky_weather.repository.network.reverse_geocoding_location.ReverseGeoCodingAddress
import com.octagon_technologies.sky_weather.repository.network.reverse_geocoding_location.ReverseGeoCodingLocation

fun Location.toReverseGeoCodingLocation(): ReverseGeoCodingLocation {
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