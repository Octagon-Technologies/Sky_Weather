package com.example.kotlinweatherapp.network

data class WeatherDataClass(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<All>,
    val message: Int
)
