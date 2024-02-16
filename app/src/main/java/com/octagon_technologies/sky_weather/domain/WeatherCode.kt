package com.octagon_technologies.sky_weather.domain

import android.os.Build
import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.octagon_technologies.sky_weather.R
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize


@Parcelize
data class WeatherCode(val code: Int?, val rainProbability: Double?) : Parcelable {
}

internal data class Icon(@DrawableRes val dayIcon: Int, @DrawableRes val nightIcon: Int)

fun WeatherCode?.getWeatherTitle(): String {
    if (this?.code == null)
        return "Fair"
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        codeMap.getOrDefault(code, "Fair")
    } else {
        codeMap[code] ?: "Fair"
    }
}

fun WeatherCode?.getWeatherIcon(isDay: Boolean): Int {
    val defaultIcon =
        Icon(R.drawable._11000_mostly_clear_large, R.drawable._11001_mostly_clear_large)
    val icon = this?.code?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            iconMap.getOrDefault(code, defaultIcon)
        else
            iconMap[code] ?: defaultIcon
    } ?: defaultIcon

    return if (isDay) icon.dayIcon else icon.nightIcon
}


internal val codeMap = mapOf(
    0 to "Unknown",
    1000 to "Clear",
    1100 to "Mostly Clear",
    1101 to "Partly Cloudy",
    1102 to "Mostly Cloudy",
    1001 to "Cloudy",
    2000 to "Fog",
    2100 to "Light Fog",
    4000 to "Drizzle",
    4001 to "Rain",
    4200 to "Light Rain",
    4201 to "Heavy Rain",
    5000 to "Snow",
    5001 to "Flurries",
    5100 to "Light Snow",
    5101 to "Heavy Snow",
    6000 to "Freezing Drizzle",
    6001 to "Freezing Rain",
    6200 to "Light Freezing Rain",
    6201 to "Heavy Freezing Rain",
    7000 to "Ice Pellets",
    7101 to "Heavy Ice Pellets",
    7102 to "Light Ice Pellets",
    8000 to "Thunderstorm"
)

internal val iconMap = mapOf(
    0 to Icon(
        dayIcon = R.drawable._10000_clear_large,
        nightIcon = R.drawable._10001_clear_large
    ),
    1000 to Icon(
        dayIcon = R.drawable._10000_clear_large,
        nightIcon = R.drawable._10001_clear_large
    ),
    1100 to Icon(
        dayIcon = R.drawable._11000_mostly_clear_large,
        nightIcon = R.drawable._11001_mostly_clear_large
    ),
    1101 to Icon(
        dayIcon = R.drawable._11010_partly_cloudy_large,
        nightIcon = R.drawable._11011_partly_cloudy_large
    ),
    1102 to Icon(
        dayIcon = R.drawable._11020_mostly_cloudy_large,
        nightIcon = R.drawable._11021_mostly_cloudy_large
    ),
    1001 to Icon(
        dayIcon = R.drawable._10010_cloudy_large,
        nightIcon = R.drawable._10010_cloudy_large
    ),
    2000 to Icon(
        dayIcon = R.drawable._20000_fog_large,
        nightIcon = R.drawable._10001_clear_large
    ),
    2100 to Icon(
        dayIcon = R.drawable._21000_fog_light_large,
        nightIcon = R.drawable._21000_fog_light_large
    ),
    4000 to Icon(
        dayIcon = R.drawable._40000_drizzle_large,
        nightIcon = R.drawable._40000_drizzle_large
    ),
    4001 to Icon(
        dayIcon = R.drawable._40000_drizzle_large,
        nightIcon = R.drawable._40000_drizzle_large
    ),
    4200 to Icon(
        dayIcon = R.drawable._42000_rain_light_large,
        nightIcon = R.drawable._42000_rain_light_large
    ),
    4201 to Icon(
        dayIcon = R.drawable._42010_rain_heavy_large,
        nightIcon = R.drawable._42010_rain_heavy_large
    ),
    5000 to Icon(
        dayIcon = R.drawable._50000_snow_large,
        nightIcon = R.drawable._50000_snow_large
    ),
    5001 to Icon(
        dayIcon = R.drawable._50010_flurries_large,
        nightIcon = R.drawable._50010_flurries_large
    ),
    5100 to Icon(
        dayIcon = R.drawable._51000_snow_light_large,
        nightIcon = R.drawable._51000_snow_light_large
    ),
    5101 to Icon(
        dayIcon = R.drawable._51010_snow_heavy_large,
        nightIcon = R.drawable._51010_snow_heavy_large
    ),
    6000 to Icon(
        dayIcon = R.drawable._60000_freezing_rain_drizzle_large,
        nightIcon = R.drawable._60000_freezing_rain_drizzle_large
    ),
    6001 to Icon(
        dayIcon = R.drawable._60010_freezing_rain_large,
        nightIcon = R.drawable._60010_freezing_rain_large
    ),
    6200 to Icon(
        dayIcon = R.drawable._62000_freezing_rain_light_large,
        nightIcon = R.drawable._62000_freezing_rain_light_large
    ),
    6201 to Icon(
        dayIcon = R.drawable._62010_freezing_rain_heavy_large,
        nightIcon = R.drawable._62010_freezing_rain_heavy_large
    ),
    7000 to Icon(
        dayIcon = R.drawable._70000_ice_pellets_large,
        nightIcon = R.drawable._70000_ice_pellets_large
    ),
    7101 to Icon(
        dayIcon = R.drawable._71010_ice_pellets_heavy_large,
        nightIcon = R.drawable._71010_ice_pellets_heavy_large
    ),
    7102 to Icon(
        dayIcon = R.drawable._71020_ice_pellets_light_large,
        nightIcon = R.drawable._71020_ice_pellets_light_large
    ),
    8000 to Icon(
        dayIcon = R.drawable._80000_tstorm_large,
        nightIcon = R.drawable._80031_tstorm_partly_cloudy_large
    ),
)