package com.octagontechnologies.sky_weather.domain.location

enum class CurrentLocationState(val title: String) {
    NotInUse("NOT IN USE"),
    Refreshing("REFRESHING"),
    NoNetwork("NO NETWORK"),
    LocationOff("DEVICE LOCATION IS OFF"),
    InUse("IN USE")
}

fun CurrentLocationState.getTagName() =
    when (this) {
        CurrentLocationState.NotInUse -> "NOT IN USE"
        CurrentLocationState.Refreshing -> "REFRESHING"
        CurrentLocationState.NoNetwork -> "NO NETWORK"
        CurrentLocationState.LocationOff -> "DEVICE LOCATION IS OFF"
        CurrentLocationState.InUse -> "IN USE"
    }