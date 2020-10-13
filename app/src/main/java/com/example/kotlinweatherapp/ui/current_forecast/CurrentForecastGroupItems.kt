package com.example.kotlinweatherapp.ui.current_forecast

import android.view.View
import com.bumptech.glide.Glide
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.databinding.CurrentForecastAllergyItemBinding
import com.example.kotlinweatherapp.databinding.CurrentForecastDescriptionItemBinding
import com.example.kotlinweatherapp.network.current_forecast.WindDirection
import com.example.kotlinweatherapp.network.current_forecast.WindGust
import com.example.kotlinweatherapp.network.current_forecast.WindSpeed
import com.xwray.groupie.databinding.BindableItem
import timber.log.Timber

class EachCurrentForecastDescriptionItem(private val lastPositionOfList: Int, private val eachWeatherDescription: EachWeatherDescription, private val isWhite: Boolean = false): BindableItem<CurrentForecastDescriptionItemBinding>() {
    override fun bind(binding: CurrentForecastDescriptionItemBinding, position: Int) {
        binding.descriptionName.text = eachWeatherDescription.descriptionName
        binding.descriptionData.text = eachWeatherDescription.descriptionData

        Timber.d("lastPositionOfList is $lastPositionOfList and position is $position")
        if (lastPositionOfList == position) binding.bottomLine.visibility = View.GONE

        if (isWhite) {
            binding.root.setBackgroundColor(binding.root.resources.getColor(android.R.color.white))
            binding.descriptionData.let {
                it.setTextColor(it.resources.getColor(R.color.color_black))
            }
            binding.descriptionName.setTextColor(binding.root.resources.getColor(R.color.dark_black))
        }
    }

    override fun getLayout(): Int = R.layout.current_forecast_description_item
}

class EachCurrentForecastAllergyItem(private val lastPositionOfList: Int, private val eachAllergyDescription: EachAllergyDescription): BindableItem<CurrentForecastAllergyItemBinding>() {
    override fun bind(binding: CurrentForecastAllergyItemBinding, position: Int) {
        binding.allergyLevel.text = eachAllergyDescription.allergyLevel
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

data class EachWeatherDescription(val descriptionName: String, val descriptionData: String)
data class EachAllergyDescription(val allergyName: String, val allergyLevel: String)

data class MainWind(val windDirection: WindDirection?, val windSpeed: WindSpeed?, val windGust: WindGust?)
data class UVClass(val uvIndex: Int, val uvLevelString: String)