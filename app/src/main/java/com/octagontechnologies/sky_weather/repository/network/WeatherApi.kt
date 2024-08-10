package com.octagontechnologies.sky_weather.repository.network

import com.octagontechnologies.sky_weather.repository.network.weather.current.CurrentForecastResponse
import com.octagontechnologies.sky_weather.repository.network.weather.daily.DailyForecastResponse
import com.octagontechnologies.sky_weather.repository.network.weather.hourly.HourlyForecastResponse
import org.joda.time.Instant
import retrofit2.http.GET
import retrofit2.http.Query


const val WEATHER_BASE_URL = "https://api.open-meteo.com/v1/"
const val WEATHER_TIME_FORMAT = "unixtime"
const val CURRENT_WEATHER_FIELDS =
    "is_day,temperature_2m,relative_humidity_2m,dew_point_2m,apparent_temperature,precipitation_probability,precipitation,snow_depth,weather_code,pressure_msl,surface_pressure,cloud_cover,visibility,evapotranspiration,wind_speed_10m,wind_speed_80m,wind_direction_10m,wind_gusts_10m,soil_temperature_0cm,soil_moisture_0_to_1cm,uv_index,terrestrial_radiation"
const val HOURLY_WEATHER_FIELDS =
    "temperature_2m,relative_humidity_2m,dew_point_2m,apparent_temperature,precipitation_probability,precipitation,snow_depth,weather_code,pressure_msl,surface_pressure,cloud_cover,visibility,evapotranspiration,wind_speed_10m,wind_direction_10m,wind_gusts_10m,soil_temperature_0cm,soil_moisture_0_to_1cm,uv_index,terrestrial_radiation,is_day"
const val DAILY_WEATHER_FIELDS =
    "weather_code,temperature_2m_max,temperature_2m_min,apparent_temperature_max,apparent_temperature_min,sunrise,sunset,uv_index_max,precipitation_probability_max,precipitation_probability_min,wind_speed_10m_max,wind_speed_10m_min,wind_gusts_10m_max,wind_gusts_10m_min,relative_humidity_2m_max,relative_humidity_2m_min,pressure_msl_min,surface_pressure_min,cloud_cover_min,pressure_msl_max,surface_pressure_max,cloud_cover_max"

interface WeatherApi {

    //    https://api.open-meteo.com/v1/forecast?latitude=-1.3&longitude=36.8167&current=is_day,temperature_2m,relative_humidity_2m,dew_point_2m,apparent_temperature,precipitation_probability,precipitation,snow_depth,weather_code,pressure_msl,surface_pressure,cloud_cover,visibility,evapotranspiration,wind_speed_10m,wind_speed_80m,wind_direction_10m,wind_gusts_10m,soil_temperature_0cm,soil_moisture_0_to_1cm,uv_index,terrestrial_radiation&timezone=auto
    /*    https://api.open-meteo.com/v1/forecast?latitude=-1.3&longitude=36.8167
            &current=is_day,temperature_2m,relative_humidity_2m,dew_point_2m,apparent_temperature,
            precipitation_probability,precipitation,snow_depth,weather_code,pressure_msl,surface_pressure,
            cloud_cover,visibility,evapotranspiration,wind_speed_10m,wind_speed_80m,wind_direction_10m,
            wind_gusts_10m,soil_temperature_0cm,soil_moisture_0_to_1cm,uv_index,
            terrestrial_radiation&timezone=auto

     */
    @GET("forecast")
    suspend fun getCurrentForecast(
        @Query("latitude") lat: String,
        @Query("longitude") lon: String,
        @Query("current") fields: String = CURRENT_WEATHER_FIELDS,
//        @Query("timeformat") timeFormat: String = WEATHER_TIME_FORMAT,
        @Query("timezone") timeZone: String = "auto"
    ): CurrentForecastResponse

    // https://api.open-meteo.com/v1/forecast?latitude=-1.3&longitude=36.8167&hourly=temperature_2m,relative_humidity_2m,dew_point_2m,apparent_temperature,precipitation_probability,precipitation,snow_depth,weather_code,pressure_msl,surface_pressure,cloud_cover,visibility,evapotranspiration,wind_speed_10m,wind_direction_10m,wind_gusts_10m,soil_temperature_0cm,soil_moisture_0_to_1cm,uv_index,terrestrial_radiation,is_day&timezone=auto
    /*
    https://api.open-meteo.com/v1/forecast?latitude=-1.3&longitude=36.8167&hourly=
    temperature_2m,relative_humidity_2m,dew_point_2m,apparent_temperature,precipitation_probability,
    precipitation,snow_depth,weather_code,pressure_msl,surface_pressure,cloud_cover,visibility,
    evapotranspiration,wind_speed_10m,wind_direction_10m,wind_gusts_10m,soil_temperature_0cm,
    soil_moisture_0_to_1cm,uv_index,terrestrial_radiation,is_day&timeformat=unixtime
     */
    @GET("forecast")
    suspend fun getHourlyForecast(
        @Query("latitude") lat: String,
        @Query("longitude") lon: String,
        @Query("hourly") fields: String = HOURLY_WEATHER_FIELDS,
//        @Query("timeformat") timeFormat: String = WEATHER_TIME_FORMAT,
        @Query("timezone") timeZone: String = "auto",
        @Query("start_hour") startHour: String = Instant.now().toDateTime().toString("yyyy-MM-dd'T'HH:mm"), // ("yyyy-mm-ddThh:mm")
        @Query("end_hour") endHour: String = Instant.now().toDateTime().plusDays(4).toString("yyyy-MM-dd'T'HH:mm") // ("yyyy-mm-ddThh:mm")
    ): HourlyForecastResponse


    //    https://api.open-meteo.com/v1/forecast?latitude=-1.3&longitude=36.8167&daily=weather_code,temperature_2m_max,temperature_2m_min,apparent_temperature_max,apparent_temperature_min,sunrise,sunset,uv_index_max,precipitation_probability_max,precipitation_probability_min,wind_speed_10m_max,wind_speed_10m_min,wind_gusts_10m_max,wind_gusts_10m_min,relative_humidity_2m_max,relative_humidity_2m_min,pressure_msl_min,surface_pressure_min,cloud_cover_min,pressure_msl_max,surface_pressure_max,cloud_cover_max&forecast_days=14
    /*
        https://api.open-meteo.com/v1/forecast?latitude=-1.3&longitude=36.8167&daily=
        weather_code,temperature_2m_max,temperature_2m_min,apparent_temperature_max,apparent_temperature_min,
        sunrise,sunset,uv_index_max,precipitation_probability_max,precipitation_probability_min,
        wind_speed_10m_max,wind_speed_10m_min,wind_gusts_10m_max,wind_gusts_10m_min,relative_humidity_2m_max,
        relative_humidity_2m_min,pressure_msl_min,surface_pressure_min,cloud_cover_min,pressure_msl_max,
        surface_pressure_max,cloud_cover_max&forecast_days=14
     */
    @GET("forecast")
    suspend fun getDailyForecast(
        @Query("latitude") lat: String,
        @Query("longitude") lon: String,
        @Query("daily") fields: String = DAILY_WEATHER_FIELDS,
//        @Query("timeformat") timeFormat: String = WEATHER_TIME_FORMAT,
        @Query("timezone") timeZone: String = "auto",
        @Query("forecast_days") forecastDays: Int = 14
    ): DailyForecastResponse
}