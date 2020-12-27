package com.octagon_technologies.sky_weather.ui.current_forecast.group_items

import android.view.View
import com.bumptech.glide.Glide
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.databinding.CurrentForecastAllergyItemBinding
import com.octagon_technologies.sky_weather.models.EachAllergyDescription
import com.xwray.groupie.databinding.BindableItem
import timber.log.Timber

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