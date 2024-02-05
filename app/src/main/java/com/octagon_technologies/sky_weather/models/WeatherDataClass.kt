package com.octagon_technologies.sky_weather.models

import com.octagon_technologies.sky_weather.repository.network.single_forecast.WindDirection
import com.octagon_technologies.sky_weather.repository.network.single_forecast.WindGust
import com.octagon_technologies.sky_weather.repository.network.single_forecast.WindSpeed

data class EachWeatherDescription(val descriptionName: String, val descriptionData: String?)

data class MainWind(val windDirection: WindDirection?, val windSpeed: WindSpeed?, val windGust: WindGust?)
