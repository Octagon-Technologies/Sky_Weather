package com.octagon_technologies.sky_weather.repository.network

/*
precipitation, precipitation_type, temp, feels_like, dewpoint, wind_speed, wind_gust, baro_pressure, visibility, humidity, wind_direction, sunrise, sunset, cloud_cover, cloud_ceiling, cloud_base, surface_shortwave_radiation, moon_phase, weather_code
 */

// TODO - Move to Build Config (https://blog.mindorks.com/using-local-properties-file-to-avoid-api-keys-check-in-into-version-control-system)
const val MAIN_API_KEY = "KcZ9fTuH4YC6YIIlqC1OWfei4NFfivwA"
const val mockLat = -1.3
const val mockLon = 36.8

const val fullAttrsString =
    "precipitation%2Cprecipitation_type%2Ctemp%2Cfeels_like%2Cdewpoint%2Cwind_speed%2Cwind_gust%2Cbaro_pressure%2Cvisibility%2Chumidity%2Cwind_direction%2Csunrise%2Csunset%2Ccloud_cover%2Ccloud_ceiling%2Ccloud_base%2Csurface_shortwave_radiation%2Cmoon_phase%2Cweather_code"
// Core Field Values: precipitation%2Cprecipitation_type%2Ctemp%2Cfeels_like%2Cdewpoint%2Cwind_speed%2Cwind_gust%2Cbaro_pressure%2Cvisibility%2Chumidity%2Cwind_direction%2Csunrise%2Csunset%2Ccloud_cover%2Ccloud_ceiling%2Ccloud_base%2Csurface_shortwave_radiation%2Cmoon_phase%2Cweather_code

// Current Forecast: https://api.climacell.co/v3/weather/realtime?apikey=KcZ9fTuH4YC6YIIlqC1OWfei4NFfivwA&unit_system=si&lat=1.336&lon=36.7804&fields=precipitation%2Cprecipitation_type%2Ctemp%2Cfeels_like%2Cdewpoint%2Cwind_speed%2Cwind_gust%2Cbaro_pressure%2Cvisibility%2Chumidity%2Cwind_direction%2Csunrise%2Csunset%2Ccloud_cover%2Ccloud_ceiling%2Ccloud_base%2Csurface_shortwave_radiation%2Cmoon_phase%2Cweather_code


// Hourly Forecast: https://api.climacell.co/v3/weather/forecast/hourly?lat=1&lon=36&unit_system=si&start_time=now&end_time=2020-10-03T11%3A39%3A08Z&&apikey=KcZ9fTuH4YC6YIIlqC1OWfei4NFfivwA&fields=precipitation%2Cprecipitation_type%2Ctemp%2Cfeels_like%2Cdewpoint%2Cwind_speed%2Cwind_gust%2Cbaro_pressure%2Cvisibility%2Chumidity%2Cwind_direction%2Csunrise%2Csunset%2Ccloud_cover%2Ccloud_ceiling%2Ccloud_base%2Csurface_shortwave_radiation%2Cmoon_phase%2Cweather_code

const val hourlyForecastAttrsString = "precipitation%2Ctemp%2Chumidity%2Cfeels_like%2Cweather_code"


// https://api.climacell.co/v3/weather/forecast/daily?lat=40&lon=44&unit_system=si&start_time=2020-11-17T06:00:00Z&end_time=2020-11-17T06:00:00Z&fields=precipitation%2Cprecipitation_accumulation%2Ctemp%2Cfeels_like%2Cwind_speed%2Cbaro_pressure%2Cvisibility%2Chumidity%2Cwind_direction%2Csunrise%2Csunset%2Cmoon_phase%2Cweather_code&apikey=KcZ9fTuH4YC6YIIlqC1OWfei4NFfivwA
const val selectedDailyForecastAttrsString =
    "precipitation%2Cprecipitation_accumulation%2Ctemp%2Cfeels_like%2Cwind_speed%2Cbaro_pressure%2Cvisibility%2Chumidity%2Cwind_direction%2Csunrise%2Csunset%2Cmoon_phase%2Cweather_code"
const val basicDailyForecastAttrsString = "temp%2Chumidity%2Cweather_code"

//https://api.climacell.co/v3/weather/forecast/daily?lat=40&lon=44&unit_system=si&start_time=now&fields=temp%2Chumidity%2Cweather_code&apikey=KcZ9fTuH4YC6YIIlqC1OWfei4NFfivwA
