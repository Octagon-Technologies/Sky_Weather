package com.octagon_technologies.sky_weather.models

import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.utils.TimeFormat
import com.octagon_technologies.sky_weather.utils.Units

data class WidgetData(
    val widgetId: Int,
    val location: Location?,
    var transparencyOutOf255: Int,
    var timeFormat: TimeFormat,
    var units: Units
)