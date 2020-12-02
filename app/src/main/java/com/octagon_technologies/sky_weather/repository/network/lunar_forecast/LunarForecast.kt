package com.octagon_technologies.sky_weather.repository.network.lunar_forecast

import com.octagon_technologies.sky_weather.network.lunar_forecast.HourlyRating
import com.squareup.moshi.Json

data class LunarForecast(
    @Json(name = "dayRating")
    val dayRating: Int?,

    @Json(name = "hourlyRating")
    val hourlyRating: HourlyRating?,

    @Json(name = "major1Start")
    val major1Start: String?,

    @Json(name = "major1StartDec")
    val major1StartDec: Double?,

    @Json(name = "major1Stop")
    val major1Stop: String?,

    @Json(name = "major1StopDec")
    val major1StopDec: Double?,

    @Json(name = "major2Start")
    val major2Start: String?,

    @Json(name = "major2StartDec")
    val major2StartDec: Double?,

    @Json(name = "major2Stop")
    val major2Stop: String?,

    @Json(name = "major2StopDec")
    val major2StopDec: Double?,

    @Json(name = "minor1Start")
    val minor1Start: String?,

    @Json(name = "minor1StartDec")
    val minor1StartDec: Double?,

    @Json(name = "minor1Stop")
    val minor1Stop: String?,

    @Json(name = "minor1StopDec")
    val minor1StopDec: Double?,

    @Json(name = "minor2Start")
    val minor2Start: String?,

    @Json(name = "minor2StartDec")
    val minor2StartDec: Double?,

    @Json(name = "minor2Stop")
    val minor2Stop: String?,

    @Json(name = "minor2StopDec")
    val minor2StopDec: Double?,

    @Json(name = "moonIllumination")
    val moonIllumination: Double?,

    @Json(name = "moonPhase")
    val moonPhase: String?,

    @Json(name = "moonRise")
    val moonRise: String?,

    @Json(name = "moonRiseDec")
    val moonRiseDec: Double?,

    @Json(name = "moonSet")
    val moonSet: String?,

    @Json(name = "moonSetDec")
    val moonSetDec: Double?,

    @Json(name = "moonTransit")
    val moonTransit: String?,

    @Json(name = "moonTransitDec")
    val moonTransitDec: Double?,

    @Json(name = "moonUnder")
    val moonUnder: String?,

    @Json(name = "moonUnderDec")
    val moonUnderDec: Double?,

    @Json(name = "sunRise")
    val sunRise: String?,

    @Json(name = "sunRiseDec")
    val sunRiseDec: Double?,

    @Json(name = "sunSet")
    val sunSet: String?,

    @Json(name = "sunSetDec")
    val sunSetDec: Double?,

    @Json(name = "sunTransit")
    val sunTransit: String?,

    @Json(name = "sunTransitDec")
    val sunTransitDec: Double?
)