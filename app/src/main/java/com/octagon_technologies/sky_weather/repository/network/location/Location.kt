package com.octagon_technologies.sky_weather.repository.network.location


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Location : ArrayList<LocationItem>()