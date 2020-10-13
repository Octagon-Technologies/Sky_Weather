package com.example.kotlinweatherapp.ui.search_location.each_search_result_item

import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.databinding.EachSearchResultItemBinding
import com.example.kotlinweatherapp.ui.search_location.EachAdapterLocationItem
import com.xwray.groupie.databinding.BindableItem

class EachSearchResultItem(private val eachAdapterLocationItem: EachAdapterLocationItem): BindableItem<EachSearchResultItemBinding>() {
    override fun bind(binding: EachSearchResultItemBinding, position: Int) {
        binding.locationItem = eachAdapterLocationItem.locationItem

        binding.addToFavouritesBtn.setImageResource(if (eachAdapterLocationItem.isFavourite) R.drawable.ic_check_circle_black else R.drawable.ic_add_circle_outline_black)
    }

    override fun getLayout(): Int = R.layout.each_search_result_item
}