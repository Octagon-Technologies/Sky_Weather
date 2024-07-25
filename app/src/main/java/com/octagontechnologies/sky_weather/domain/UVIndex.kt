package com.octagontechnologies.sky_weather.domain

enum class UVIndex {
    Low, Moderate, High, VeryHigh, Extreme;

    override fun toString(): String {
        return when (this) {
            Low -> "Low"
            Moderate -> "Moderate"
            High -> "High"
            VeryHigh -> "Very High"
            Extreme -> "Extreme"
        }
    }

    companion object {
        fun getUVIndexFromNum(uvIndex: Int?): UVIndex = when (uvIndex) {
            null -> Moderate
            0, 1, 2 -> Low
            3, 4, 5 -> Moderate
            6, 7 -> High
            8, 9, 10 -> VeryHigh
            else -> Extreme
        }
    }

    //
}