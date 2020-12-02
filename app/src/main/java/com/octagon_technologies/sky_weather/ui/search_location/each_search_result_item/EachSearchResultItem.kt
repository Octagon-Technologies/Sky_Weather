package com.octagon_technologies.sky_weather.ui.search_location.each_search_result_item

import android.annotation.SuppressLint
import android.widget.ImageView
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.Theme
import com.octagon_technologies.sky_weather.databinding.EachSearchResultItemBinding
import com.octagon_technologies.sky_weather.repository.network.location.LocationItem
import com.xwray.groupie.databinding.BindableItem
import java.util.*

class EachSearchResultItem(
    val theme: Theme?,
    var isLikedByUser: Boolean = false,
    var locationItem: LocationItem,
    val lambda: (EachSearchResultItem) -> Unit
) : BindableItem<EachSearchResultItemBinding>() {

    var actualPosition: Int = 0

    @SuppressLint("SetTextI18n")
    override fun bind(binding: EachSearchResultItemBinding, position: Int) {
        actualPosition = position

        locationItem.apply {
            binding.locationItem = this
            binding.theme = theme

            binding.searchCityName.text = "${
                when {
                    address?.suburb != null -> address.suburb
                    address?.city != null -> address.suburb
                    displayName != null -> displayName.split(",")[0].capitalize(Locale.getDefault())
                    else -> "--"
                }
            }, ${address?.countryCode?.toUpperCase(Locale.getDefault())}"
        }

        binding.addToFavouritesBtn.setImageResource(
            if (isLikedByUser) R.drawable.ic_check_circle_black
            else R.drawable.ic_add_circle_outline_black
        )
        binding.addToFavouritesBtn.setOnClickListener {
            lambda.invoke(this)
            isLikedByUser = !isLikedByUser

            (it as ImageView)
                .setImageResource(
                    if (isLikedByUser) R.drawable.ic_check_circle_black
                    else R.drawable.ic_add_circle_outline_black
                )
        }
    }

    override fun bind(
        viewBinding: EachSearchResultItemBinding,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            actualPosition = payloads[0] as Int
        } else super.bind(viewBinding, position, payloads)
    }

    override fun getLayout(): Int = R.layout.each_search_result_item

}