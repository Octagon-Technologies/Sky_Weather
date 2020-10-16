package com.example.kotlinweatherapp.ui.search_location

import com.example.kotlinweatherapp.network.location.LocationItem
import com.example.kotlinweatherapp.network.reverse_geocoding_location.ReverseGeoCodingAddress
import com.example.kotlinweatherapp.network.reverse_geocoding_location.ReverseGeoCodingLocation

data class EachAdapterLocationItem(var isFavourite: Boolean = false, var locationItem: LocationItem)

fun LocationItem.toReverseGeoCodingLocation(): ReverseGeoCodingLocation {
    val reverseGeoCodingAddress = with(address!!) {
        ReverseGeoCodingAddress(city,country, countryCode, postcode, null, state, suburb)
    }
    return ReverseGeoCodingLocation(reverseGeoCodingAddress, boundingbox, displayName, lat, licence, lon, osmId, osmType, placeId)
}