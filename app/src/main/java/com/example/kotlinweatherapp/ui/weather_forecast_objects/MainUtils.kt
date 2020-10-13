package com.example.kotlinweatherapp.ui.weather_forecast_objects

import androidx.datastore.preferences.Preferences

object AppConstants {
    const val LOCATION_REQUEST = 1000
    const val GPS_REQUEST = 1001
}

enum class Units {IMPERIAL, METRIC}
enum class WindDirectionUnits {CARDINAL, DEGREES}
enum class TimeFormat { HALF_DAY, FULL_DAY }
enum class DisplayMode {LIGHT, DARK}


data class EachDataStoreItem(val preferencesName: String, val newValue: Any)