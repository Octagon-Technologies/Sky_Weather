package com.octagon_technologies.sky_weather.repository.database

import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.domain.Allergy
import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.domain.Lunar
import com.octagon_technologies.sky_weather.domain.MiniAllergy
import com.octagon_technologies.sky_weather.domain.SingleForecast
import com.octagon_technologies.sky_weather.domain.UVIndex
import com.octagon_technologies.sky_weather.domain.WeatherCode
import com.octagon_technologies.sky_weather.domain.Wind
import com.octagon_technologies.sky_weather.domain.daily.DailyForecast
import com.octagon_technologies.sky_weather.domain.daily.TimePeriod
import com.octagon_technologies.sky_weather.repository.database.allergy.LocalAllergy
import com.octagon_technologies.sky_weather.repository.database.location.LocalLocation
import com.octagon_technologies.sky_weather.repository.database.lunar.LocalLunar
import com.octagon_technologies.sky_weather.repository.database.weather.current.LocalCurrentForecast
import com.octagon_technologies.sky_weather.repository.database.weather.daily.LocalDailyForecast
import com.octagon_technologies.sky_weather.repository.database.weather.hourly.LocalHourlyForecast
import com.octagon_technologies.sky_weather.repository.network.allergy.models.AllergyResponse
import com.octagon_technologies.sky_weather.repository.network.lunar.models.LunarForecastResponse
import com.octagon_technologies.sky_weather.repository.network.weather.current_forecast.CurrentForecastResponse
import com.octagon_technologies.sky_weather.repository.network.weather.daily_forecast.Daily
import com.octagon_technologies.sky_weather.repository.network.weather.daily_forecast.DailyForecastResponse
import com.octagon_technologies.sky_weather.repository.network.weather.hourly_forecast.Hourly
import com.octagon_technologies.sky_weather.repository.network.weather.hourly_forecast.HourlyForecastResponse
import org.joda.time.Instant

fun CurrentForecastResponse.toLocalCurrentForecast() =
    LocalCurrentForecast(currentForecast = toSingleForecast())

fun CurrentForecastResponse.toSingleForecast() =
    with(data.values) {
        SingleForecast(
            temp = temperature,
            feelsLike = temperatureApparent,
            weatherCode = WeatherCode(weatherCode, precipitationProbability),
            uvIndex = UVIndex.getUVIndexFromNum(uvIndex),
            wind = Wind(windSpeed, windGust, windDirection),
            cloudCover = cloudCover,
            // 2024-02-02T17:02:00Z
            timeInMillis = Instant.parse(data.time).millis,
            humidity = humidity,
            cloudCeiling = cloudCeiling,
            visibility = visibility,
            dewPoint = dewPoint,
            pressure = pressureSurfaceLevel
        )
    }

fun HourlyForecastResponse.toLocalHourlyForecast() =
    LocalHourlyForecast(listOfHourlyForecast =
    timelines.hourly.map { hourly -> hourly.toHourlyForecast() })

fun Hourly.toHourlyForecast() =
    with(values) {
        SingleForecast(
            temp = temperature,
            feelsLike = temperatureApparent,
            weatherCode = WeatherCode(weatherCode, precipitationProbability),
            uvIndex = UVIndex.getUVIndexFromNum(uvIndex),
            wind = Wind(windSpeed, windGust, windDirection),
            cloudCover = cloudCover,
            // 2024-02-02T17:02:00Z
            timeInMillis = Instant.parse(time).millis,
            humidity = humidity,
            cloudCeiling = cloudCeiling,
            visibility = visibility,
            dewPoint = dewPoint,
            pressure = pressureSurfaceLevel
        )
    }


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

fun DailyForecastResponse.toLocalDailyForecast() =
    LocalDailyForecast(
        listOfDailyForecast = timelines.daily.map { it.toDailyForecast() }
    )

fun Daily.toDailyForecast(): DailyForecast {
    with(values) {
        val lunar = Lunar(sunriseTime, sunsetTime, moonriseTime, moonsetTime)

        val dayTime =
            TimePeriod(
                temp = temperatureMax,
                feelsLike = temperatureApparentMax,
                weatherCode = WeatherCode(weatherCodeMax, precipitationProbabilityAvg),
                uvIndex = UVIndex.getUVIndexFromNum(uvIndexAvg),
                wind = Wind(windSpeedMin, windGustMin, windDirectionAvg),
                cloudCover = cloudCoverAvg,
                cloudCeiling = cloudCeilingMin.toDouble(),
                dewPoint = dewPointMin,
                pressure = pressureSurfaceLevelMin,
                humidity = humidityMax,
                isDay = true,
                lunar = lunar
            )

        val nightTime =
            TimePeriod(
                temp = temperatureMin,
                feelsLike = temperatureApparentMin,
                weatherCode = WeatherCode(weatherCodeMin, precipitationProbabilityAvg),
                uvIndex = UVIndex.getUVIndexFromNum(uvIndexMin),
                wind = Wind(windSpeedMax, windGustMax, windDirectionAvg),
                cloudCover = cloudCoverAvg,
                cloudCeiling = cloudCeilingMax,
                dewPoint = dewPointMax,
                pressure = pressureSurfaceLevelMax,
                humidity = humidityMin,
                isDay = false,
                lunar = lunar
            )

        return DailyForecast(
            time,
            dayTime,
            nightTime
        )
    }
}