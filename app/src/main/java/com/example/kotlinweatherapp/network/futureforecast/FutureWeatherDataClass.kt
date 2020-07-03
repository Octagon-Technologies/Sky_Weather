package com.example.kotlinweatherapp.network.futureforecast



data class FutureWeatherDataClass(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<All>,
    val message: Int
)
