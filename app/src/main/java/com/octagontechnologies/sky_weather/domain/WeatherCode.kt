package com.octagontechnologies.sky_weather.domain

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.octagontechnologies.sky_weather.R
import kotlinx.parcelize.Parcelize
import org.joda.time.DateTime


@Parcelize
data class WeatherCode(val code: Int?, val rainProbability: Int?) : Parcelable {
}

internal data class Icon(@DrawableRes val dayIcon: Int, @DrawableRes val nightIcon: Int)

fun WeatherCode?.getWeatherTitle(): String {
    if (this?.code == null)
        return "Fair"
    return codeMap.getOrDefault(code, "Fair")
}

fun WeatherCode?.getWeatherIcon(): Int {
    val defaultIcon =
        Icon(R.drawable._11000_mostly_clear_large, R.drawable._11001_mostly_clear_large)
    val icon = this?.code?.let {
        iconMap.getOrDefault(code, defaultIcon)
    } ?: defaultIcon

    val isDay = DateTime.now().hourOfDay in 6..17
    return if (isDay) icon.dayIcon else icon.nightIcon
}

internal val codeMap = mapOf(
    0 to "Clear Sky",
    1 to "Mostly Clear",
    2 to "Partly Cloudy",
    3 to "Mostly Cloudy",
    48 to "Fog",
    45 to "Light Fog",
    51 to "Light Drizzle",
    53 to "Moderate Drizzle",
    55 to "Intense Drizzle",
    56 to "Freezing Drizzle - Light",
    57 to "Freezing Drizzle - Heavy",
    61 to "Light Rain",
    63 to "Rain",
    65 to "Heavy Rain",
    66 to "Freezing Rain - Light",
    67 to "Freezing Rain - Heavy",
    71 to "Light Snow",
    73 to "Snow",
    75 to "Heavy Snow",
    77 to "Snow grains",
    80 to "Light Rain Showers",
    81 to "Rain Showers",
    82 to "Violent Rain Showers",
    85 to "Light Snow Showers",
    86 to "Heavy Snow Showers",
    95 to "Thunderstorm",
    96 to "Thunderstorm with light hail",
    96 to "Thunderstorm with heavy hail"
)


/*
0	Clear sky
1, 2, 3	Mainly clear, partly cloudy, and overcast
45, 48	Fog and depositing rime fog
51, 53, 55	Drizzle: Light, moderate, and dense intensity
56, 57	Freezing Drizzle: Light and dense intensity
61, 63, 65	Rain: Slight, moderate and heavy intensity
66, 67	Freezing Rain: Light and heavy intensity
71, 73, 75	Snow fall: Slight, moderate, and heavy intensity
77	Snow grains
80, 81, 82	Rain showers: Slight, moderate, and violent
85, 86	Snow showers slight and heavy
95 *	Thunderstorm: Slight or moderate
96, 99 *	Thunderstorm with slight and heavy hail
 */

internal val iconMap = mapOf(
    0 to Icon(
        dayIcon = R.drawable._10000_clear_large,
        nightIcon = R.drawable._10001_clear_large
    ),
    1 to Icon(
        dayIcon = R.drawable._11000_mostly_clear_large,
        nightIcon = R.drawable._11001_mostly_clear_large
    ),
    2 to Icon(
        dayIcon = R.drawable._11010_partly_cloudy_large,
        nightIcon = R.drawable._11011_partly_cloudy_large
    ),
    3 to Icon(
        dayIcon = R.drawable._11020_mostly_cloudy_large,
        nightIcon = R.drawable._11021_mostly_cloudy_large
    ),
    45 to Icon(
        dayIcon = R.drawable._21000_fog_light_large,
        nightIcon = R.drawable._21000_fog_light_large
    ),
    48 to Icon(
        dayIcon = R.drawable._20000_fog_large,
        nightIcon = R.drawable._20000_fog_large
    ),
    51 to Icon(
        dayIcon = R.drawable._40000_drizzle_large,
        nightIcon = R.drawable._40000_drizzle_large
    ),
    53 to Icon(
        dayIcon = R.drawable._40000_drizzle_large,
        nightIcon = R.drawable._40000_drizzle_large
    ),
    55 to Icon(
        dayIcon = R.drawable._40000_drizzle_large,
        nightIcon = R.drawable._40000_drizzle_large
    ),
    56 to Icon(
        dayIcon = R.drawable._60000_freezing_rain_drizzle_large,
        nightIcon = R.drawable._60000_freezing_rain_drizzle_large
    ),
    57 to Icon(
        dayIcon = R.drawable._60000_freezing_rain_drizzle_large,
        nightIcon = R.drawable._60000_freezing_rain_drizzle_large
    ),
    61 to Icon(
        dayIcon = R.drawable._42000_rain_light_large,
        nightIcon = R.drawable._42000_rain_light_large
    ),
    63 to Icon(
        dayIcon = R.drawable._40010_rain_large,
        nightIcon = R.drawable._40010_rain_large
    ),
    65 to Icon(
        dayIcon = R.drawable._42010_rain_heavy_large,
        nightIcon = R.drawable._42010_rain_heavy_large
    ),
    66 to Icon(
        dayIcon = R.drawable._42010_rain_heavy_large,
        nightIcon = R.drawable._42010_rain_heavy_large
    ),
    67 to Icon(
        dayIcon = R.drawable._42010_rain_heavy_large,
        nightIcon = R.drawable._42010_rain_heavy_large
    ),
    71 to Icon(
        dayIcon = R.drawable._50010_flurries_large,
        nightIcon = R.drawable._50010_flurries_large
    ),
    73 to Icon(
        dayIcon = R.drawable._51000_snow_light_large,
        nightIcon = R.drawable._51000_snow_light_large
    ),
    75 to Icon(
        dayIcon = R.drawable._51010_snow_heavy_large,
        nightIcon = R.drawable._51010_snow_heavy_large
    ),
    77 to Icon(
        dayIcon = R.drawable._50010_flurries_large,
        nightIcon = R.drawable._50010_flurries_large
    ),
    80 to Icon(
        dayIcon = R.drawable._42000_rain_light_large,
        nightIcon = R.drawable._42000_rain_light_large
    ),
    81 to Icon(
        dayIcon = R.drawable._40010_rain_large,
        nightIcon = R.drawable._40010_rain_large
    ),
    82 to Icon(
        dayIcon = R.drawable._42010_rain_heavy_large,
        nightIcon = R.drawable._42010_rain_heavy_large
    ),
    85 to Icon(
        dayIcon = R.drawable._51000_snow_light_large,
        nightIcon = R.drawable._51000_snow_light_large
    ),
    86 to Icon(
        dayIcon = R.drawable._51010_snow_heavy_large,
        nightIcon = R.drawable._51010_snow_heavy_large
    ),
    95 to Icon(
        dayIcon = R.drawable._80000_tstorm_large,
        nightIcon = R.drawable._80031_tstorm_partly_cloudy_large
    ),
    96 to Icon(
        dayIcon = R.drawable._80000_tstorm_large,
        nightIcon = R.drawable._80031_tstorm_partly_cloudy_large
    ),
    99 to Icon(
        dayIcon = R.drawable._80000_tstorm_large,
        nightIcon = R.drawable._80031_tstorm_partly_cloudy_large
    ),
)