package com.octagon_technologies.sky_weather.ui.current_forecast.group_items

import android.view.View
import androidx.core.content.ContextCompat
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.databinding.MiniForecastDescriptionBinding
import com.octagon_technologies.sky_weather.models.EachWeatherDescription
import com.octagon_technologies.sky_weather.utils.Theme
import com.xwray.groupie.databinding.BindableItem

class MiniForecastDescription(
    private val isLastItem: Boolean,
    private val eachWeatherDescription: EachWeatherDescription,
    private val theme: Theme? = null
) : BindableItem<MiniForecastDescriptionBinding>() {
    override fun bind(binding: MiniForecastDescriptionBinding, position: Int) {
        binding.eachWeatherDescription = eachWeatherDescription
        binding.theme = theme ?: Theme.DARK

        if (theme == null) binding.bottomLine.setBackgroundColor(
            ContextCompat.getColor(
                binding.root.context,
                R.color.light_white
            )
        )


        if (isLastItem)
            binding.bottomLine.visibility = View.GONE
    }

    override fun getLayout(): Int = R.layout.mini_forecast_description
}