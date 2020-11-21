package com.octagon_technologies.sky_weather.ui.hourly_forecast.each_hourly_forecast_item

import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.TimeFormat
import com.octagon_technologies.sky_weather.Units
import com.octagon_technologies.sky_weather.databinding.EachDayTextItemBinding
import com.octagon_technologies.sky_weather.databinding.EachHourlyForecastItemBinding
import com.octagon_technologies.sky_weather.network.hourly_forecast.EachHourlyForecast
import com.xwray.groupie.databinding.BindableItem
import timber.log.Timber

class EachHourlyForecastItem(
    val eachHourlyForecast: EachHourlyForecast,
    private val timeFormat: TimeFormat?
) : BindableItem<EachHourlyForecastItemBinding>() {
    override fun bind(binding: EachHourlyForecastItemBinding, position: Int) {
        binding.eachHourlyForecast = eachHourlyForecast
        binding.timeFormat = timeFormat
        Timber.d("feelsLike.temp is ${eachHourlyForecast.feelsLike?.value}")
    }

    override fun getLayout(): Int = R.layout.each_hourly_forecast_item
}

class EachDayTextItem(private val dayString: String) : BindableItem<EachDayTextItemBinding>() {
    override fun bind(binding: EachDayTextItemBinding, position: Int) {
        binding.dayText.text = dayString
    }

    override fun getLayout(): Int = R.layout.each_day_text_item
}

fun Double.celsiusToFahrenheit(): Double = ((this * 1.8) + 32f)
fun Double.fahrenheitToCelsius(): Double = ((this - 32) * (5 / 9))