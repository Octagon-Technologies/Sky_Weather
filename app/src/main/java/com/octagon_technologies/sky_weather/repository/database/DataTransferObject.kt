package com.octagon_technologies.sky_weather.repository.database

import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.domain.Allergy
import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.domain.Lunar
import com.octagon_technologies.sky_weather.domain.MiniAllergy
import com.octagon_technologies.sky_weather.repository.database.allergy.LocalAllergy
import com.octagon_technologies.sky_weather.repository.database.location.LocalLocation
import com.octagon_technologies.sky_weather.repository.database.lunar.LocalLunar
import com.octagon_technologies.sky_weather.repository.network.allergy.models.AllergyResponse
import com.octagon_technologies.sky_weather.repository.network.lunar.models.LunarForecastResponse

fun AllergyResponse.toLocalAllergy(): LocalAllergy {
    val risk = this.data?.getOrNull(0)?.risk
    return LocalAllergy(
        allergy = Allergy(
            grassPollen = MiniAllergy("Grass Pollen", risk?.grassPollen ?: "--", R.drawable.grass),
            weedPollen = MiniAllergy("Weed Pollen", risk?.weedPollen ?: "--", R.drawable.leaf),
            treePollen = MiniAllergy("Tree Pollen", risk?.treePollen ?: "--", R.drawable.tree)
        )
    )
}

fun LunarForecastResponse.toLunar() =
    Lunar(sunRise, sunSet, moonRise, moonSet)

fun LunarForecastResponse.toLocalLunar(): LocalLunar =
    LocalLunar(lunarForecast = Lunar(sunRise, sunSet, moonRise, moonSet))

fun Location.toLocalLocation() = LocalLocation(location = this)