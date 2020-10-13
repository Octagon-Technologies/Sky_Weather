package com.example.kotlinweatherapp.network.location


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Location : ArrayList<LocationItem>()