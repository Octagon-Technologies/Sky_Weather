package com.octagon_technologies.sky_weather.ui.current_forecast.group_items

import android.view.View
import com.bumptech.glide.Glide
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.databinding.CurrentForecastAllergyItemBinding
import com.octagon_technologies.sky_weather.domain.MiniAllergy
import com.xwray.groupie.databinding.BindableItem
import timber.log.Timber

class EachCurrentForecastAllergyItem(private val isLastItem: Boolean, private val miniAllergy: MiniAllergy): BindableItem<CurrentForecastAllergyItemBinding>() {
    override fun bind(binding: CurrentForecastAllergyItemBinding, position: Int) {
        binding.allergyLevel.text = miniAllergy.level
        binding.allergyName.text = miniAllergy.type

        Glide.with(binding.root.context)
            .load(miniAllergy.iconRes)
            .into(binding.allergyImage)

        if (isLastItem)
            binding.bottomLine.visibility = View.GONE
    }

    override fun getLayout(): Int = R.layout.current_forecast_allergy_item
}