package com.octagon_technologies.sky_weather.ui.current_forecast.group_items

import android.view.View
import androidx.core.content.ContextCompat
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.databinding.CurrentForecastDescriptionItemBinding
import com.octagon_technologies.sky_weather.models.EachWeatherDescription
import com.octagon_technologies.sky_weather.utils.Theme
import com.xwray.groupie.databinding.BindableItem
import timber.log.Timber

class EachCurrentForecastDescriptionItem(private val lastPositionOfList: Int, private val eachWeatherDescription: EachWeatherDescription, private val theme: Theme? = null): BindableItem<CurrentForecastDescriptionItemBinding>() {
    override fun bind(binding: CurrentForecastDescriptionItemBinding, position: Int) {
        binding.eachWeatherDescription = eachWeatherDescription
        binding.theme = theme ?: Theme.DARK

        if (theme == null) binding.bottomLine.setBackgroundColor(
            ContextCompat.getColor(
                binding.root.context,
                R.color.light_white
            )
        )
        Timber.d("lastPositionOfList is $lastPositionOfList and position is $position")
        if (lastPositionOfList == position) binding.bottomLine.visibility = View.GONE

    }

    override fun getLayout(): Int = R.layout.current_forecast_description_item
}