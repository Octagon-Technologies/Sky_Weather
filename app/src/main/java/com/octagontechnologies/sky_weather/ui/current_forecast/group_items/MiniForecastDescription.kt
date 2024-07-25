package com.octagontechnologies.sky_weather.ui.current_forecast.group_items

import android.view.View
import com.octagontechnologies.sky_weather.R
import com.octagontechnologies.sky_weather.databinding.MiniForecastDescriptionBinding
import com.octagontechnologies.sky_weather.models.EachWeatherDescription
import com.xwray.groupie.databinding.BindableItem

class MiniForecastDescription(
    private val isLastItem: Boolean,
    private val eachWeatherDescription: EachWeatherDescription
) : BindableItem<MiniForecastDescriptionBinding>() {
    override fun bind(binding: MiniForecastDescriptionBinding, position: Int) {
        binding.eachWeatherDescription = eachWeatherDescription

        if (isLastItem)
            binding.bottomLine.visibility = View.GONE
    }

    override fun getLayout(): Int = R.layout.mini_forecast_description
}