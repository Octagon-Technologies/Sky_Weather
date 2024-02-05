package com.octagon_technologies.sky_weather.ui.hourly_forecast.each_hourly_forecast_item

import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.databinding.EachHourlyForecastItemBinding
import com.octagon_technologies.sky_weather.databinding.HeaderMiniHourlyForecastBinding
import com.octagon_technologies.sky_weather.domain.SingleForecast
import com.octagon_technologies.sky_weather.domain.getFormattedFeelsLike
import com.octagon_technologies.sky_weather.domain.getFormattedHumidity
import com.octagon_technologies.sky_weather.domain.getFormattedTemp
import com.octagon_technologies.sky_weather.utils.TimeFormat
import com.octagon_technologies.sky_weather.utils.getHoursAndMins
import com.octagon_technologies.sky_weather.utils.getHoursOfDay
import com.xwray.groupie.databinding.BindableItem

class MiniHourlyForecast(
    val hourlyForecast: SingleForecast,
    private val timeFormat: TimeFormat?,
    private val openSelectedHourlyForecast: () -> Unit
) : BindableItem<EachHourlyForecastItemBinding>() {
    override fun bind(binding: EachHourlyForecastItemBinding, position: Int) {
        binding.root.setOnClickListener { openSelectedHourlyForecast() }

        binding.humidityLevel.text = hourlyForecast.getFormattedHumidity()
        binding.timeDisplayText.text = hourlyForecast.timeInMillis.getHoursAndMins(timeFormat)

        binding.tempText.text = hourlyForecast.getFormattedTemp()
        binding.feelsLikeText.text = hourlyForecast.getFormattedFeelsLike()

        val hrs = hourlyForecast.timeInMillis.getHoursOfDay()
        val isDay = hrs in 6..19
        binding.weatherIcon.setImageResource(
            if (isDay) R.drawable.yellow_sun
            else R.drawable.moon
        )
    }

    override fun getLayout(): Int = R.layout.each_hourly_forecast_item
}

class HeaderMiniHourlyForecast(private val dayString: String) :
    BindableItem<HeaderMiniHourlyForecastBinding>() {
    override fun bind(binding: HeaderMiniHourlyForecastBinding, position: Int) {
        binding.dayText.text = dayString
    }

    override fun getLayout(): Int = R.layout.header_mini_hourly_forecast
}

fun Double.celsiusToFahrenheit(): Double = ((this * 1.8) + 32f)
fun Int.fahrenheitToCelsius(): Int = (this - 32) * 5 / 9