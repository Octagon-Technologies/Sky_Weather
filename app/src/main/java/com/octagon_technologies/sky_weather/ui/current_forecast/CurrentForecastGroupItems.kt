package com.octagon_technologies.sky_weather.ui.current_forecast

import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.Theme
import com.octagon_technologies.sky_weather.databinding.CurrentForecastAllergyItemBinding
import com.octagon_technologies.sky_weather.databinding.CurrentForecastDescriptionItemBinding
import com.octagon_technologies.sky_weather.network.single_forecast.WindDirection
import com.octagon_technologies.sky_weather.network.single_forecast.WindGust
import com.octagon_technologies.sky_weather.network.single_forecast.WindSpeed
import com.xwray.groupie.databinding.BindableItem
import timber.log.Timber

class EachCurrentForecastDescriptionItem(private val lastPositionOfList: Int, private val eachWeatherDescription: EachWeatherDescription, private val theme: Theme? = null): BindableItem<CurrentForecastDescriptionItemBinding>() {
    override fun bind(binding: CurrentForecastDescriptionItemBinding, position: Int) {
        binding.eachWeatherDescription = eachWeatherDescription
        binding.theme = theme ?: Theme.DARK

        if (theme == null) binding.bottomLine.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.light_white))
        Timber.d("lastPositionOfList is $lastPositionOfList and position is $position")
        if (lastPositionOfList == position) binding.bottomLine.visibility = View.GONE

    }

    override fun getLayout(): Int = R.layout.current_forecast_description_item
}

class EachCurrentForecastAllergyItem(private val lastPositionOfList: Int, private val eachAllergyDescription: EachAllergyDescription): BindableItem<CurrentForecastAllergyItemBinding>() {
    override fun bind(binding: CurrentForecastAllergyItemBinding, position: Int) {
        binding.allergyLevel.text = eachAllergyDescription.allergyLevel ?: "--"
        binding.allergyName.text = eachAllergyDescription.allergyName

        val imageDrawable = when (eachAllergyDescription.allergyName) {
            "Overall Risk" -> R.drawable.clock
            "Grass Pollen" -> R.drawable.grass
            "Tree Pollen" -> R.drawable.tree
            "Weed Pollen" -> R.drawable.leaf
            else -> throw RuntimeException("Unexpected name: eachAllergyDescription.allergyName is ${eachAllergyDescription.allergyName}")
        }

        Glide.with(binding.root.context)
            .load(imageDrawable)
            .into(binding.allergyImage)

        Timber.d("lastPositionOfList is $lastPositionOfList and position is $position")
        if (lastPositionOfList == position) binding.bottomLine.visibility = View.GONE
    }

    override fun getLayout(): Int = R.layout.current_forecast_allergy_item
}

data class EachWeatherDescription(val descriptionName: String, val descriptionData: String?)
data class EachAllergyDescription(val allergyName: String, val allergyLevel: String?)

data class MainWind(val windDirection: WindDirection?, val windSpeed: WindSpeed?, val windGust: WindGust?)
data class UVClass(val uvIndex: Int, val uvLevelString: String)