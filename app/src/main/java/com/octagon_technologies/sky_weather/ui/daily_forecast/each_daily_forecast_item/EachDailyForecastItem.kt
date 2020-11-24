package com.octagon_technologies.sky_weather.ui.daily_forecast.each_daily_forecast_item

import android.annotation.SuppressLint
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.Units
import com.octagon_technologies.sky_weather.databinding.EachDailyForecastItemBinding
import com.octagon_technologies.sky_weather.network.daily_forecast.EachDailyForecast
import com.octagon_technologies.sky_weather.ui.shared_code.getWeatherIconFrom
import com.xwray.groupie.databinding.BindableItem
import timber.log.Timber

class EachDailyForecastItem(val eachDailyForecast: EachDailyForecast) :
    BindableItem<EachDailyForecastItemBinding>() {
    @SuppressLint("SetTextI18n")
    override fun bind(binding: EachDailyForecastItemBinding, position: Int) {
        binding.eachDailyForecast = eachDailyForecast
        binding.units = try {
            Units.valueOf(eachDailyForecast.temp?.get(0)?.max?.units!!)
        } catch (e: Exception) {
            Units.METRIC
        }

        binding.weatherImage.getWeatherIconFrom(
            eachDailyForecast.weatherCode, eachDailyForecast.observationTime
        )

        eachDailyForecast.apply {
            binding.minTempOfTheDay.text = "${try {temp?.get(0)?.min?.value?.toInt()?.toString()!!} catch (e: Exception) { "--" }}°"
            binding.maxTempOfTheDay.text = "${try {temp?.get(1)?.max?.value?.toInt()?.toString()!!} catch (e: Exception) { "--" }}°"
        }
    }

    override fun getLayout() = R.layout.each_daily_forecast_item
}
