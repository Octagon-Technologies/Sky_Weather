package com.octagontechnologies.sky_weather.ui.hourly_forecast.each_hourly_forecast_item

import com.octagontechnologies.sky_weather.R
import com.octagontechnologies.sky_weather.databinding.EachHourlyForecastItemBinding
import com.octagontechnologies.sky_weather.databinding.HeaderMiniHourlyForecastBinding
import com.octagontechnologies.sky_weather.domain.SingleForecast
import com.octagontechnologies.sky_weather.domain.getFormattedFeelsLike
import com.octagontechnologies.sky_weather.domain.getFormattedHumidity
import com.octagontechnologies.sky_weather.domain.getFormattedTemp
import com.octagontechnologies.sky_weather.utils.TimeFormat
import com.octagontechnologies.sky_weather.utils.Units
import com.octagontechnologies.sky_weather.utils.getHoursAndMins
import com.octagontechnologies.sky_weather.utils.isImperial
import com.octagontechnologies.sky_weather.utils.loadWeatherIcon
import com.xwray.groupie.databinding.BindableItem
import timber.log.Timber

class MiniHourlyForecast(
    val hourlyForecast: SingleForecast,
    val units: Units?,
    private val timeFormat: TimeFormat?,
    private val openSelectedHourlyForecast: () -> Unit
) : BindableItem<EachHourlyForecastItemBinding>() {
    override fun bind(binding: EachHourlyForecastItemBinding, position: Int) {
        binding.root.setOnClickListener { openSelectedHourlyForecast() }

        binding.humidityLevel.text = hourlyForecast.getFormattedHumidity()

        val time = hourlyForecast.timeInEpochMillis.getHoursAndMins(timeFormat)
        Timber.d("Formatted Time is $time")
        binding.timeDisplayText.text = time

        binding.tempText.text = hourlyForecast.getFormattedTemp(units.isImperial())
        binding.feelsLikeText.text = hourlyForecast.getFormattedFeelsLike(units.isImperial())
        binding.weatherIcon.loadWeatherIcon(
            hourlyForecast.timeInEpochMillis,
            hourlyForecast.weatherCode
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
