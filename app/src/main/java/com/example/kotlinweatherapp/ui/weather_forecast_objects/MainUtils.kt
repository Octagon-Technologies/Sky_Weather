package com.example.kotlinweatherapp.ui.weather_forecast_objects

enum class Units {IMPERIAL, METRIC}
enum class WindDirectionUnits {CARDINAL, DEGREES}
enum class TimeFormat { HALF_DAY, FULL_DAY }
enum class DisplayMode {LIGHT, DARK}


data class EachDataStoreItem(val preferencesName: String, val newValue: Any)