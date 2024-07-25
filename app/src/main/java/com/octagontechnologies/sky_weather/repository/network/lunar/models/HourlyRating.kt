package com.octagontechnologies.sky_weather.repository.network.lunar.models


import com.squareup.moshi.Json

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HourlyRating(
    @Json(name = "0")
    val x0: Int?,
    @Json(name = "1")
    val x1: Int?,
    @Json(name = "10")
    val x10: Int?,
    @Json(name = "11")
    val x11: Int?,
    @Json(name = "12")
    val x12: Int?,
    @Json(name = "13")
    val x13: Int?,
    @Json(name = "14")
    val x14: Int?,
    @Json(name = "15")
    val x15: Int?,
    @Json(name = "16")
    val x16: Int?,
    @Json(name = "17")
    val x17: Int?,
    @Json(name = "18")
    val x18: Int?,
    @Json(name = "19")
    val x19: Int?,
    @Json(name = "2")
    val x2: Int?,
    @Json(name = "20")
    val x20: Int?,
    @Json(name = "21")
    val x21: Int?,
    @Json(name = "22")
    val x22: Int?,
    @Json(name = "23")
    val x23: Int?,
    @Json(name = "3")
    val x3: Int?,
    @Json(name = "4")
    val x4: Int?,
    @Json(name = "5")
    val x5: Int?,
    @Json(name = "6")
    val x6: Int?,
    @Json(name = "7")
    val x7: Int?,
    @Json(name = "8")
    val x8: Int?,
    @Json(name = "9")
    val x9: Int?
)