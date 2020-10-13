package com.example.kotlinweatherapp.ui.see_more_current

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweatherapp.network.current_forecast.SingleForecast
import com.example.kotlinweatherapp.ui.current_forecast.EachCurrentForecastDescriptionItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder


@BindingAdapter("getCurrentConditionsForSeeMorePage")
fun RecyclerView.getCurrentConditionsForSeeMorePage(singleForecast: SingleForecast?) {
    layoutManager = LinearLayoutManager(context)
    val groupAdapter = GroupAdapter<GroupieViewHolder>()
    adapter = groupAdapter
    val arrayOfWeatherDescriptions = getAdvancedForecastDescription(singleForecast)

    arrayOfWeatherDescriptions.forEach {
        groupAdapter.add(EachCurrentForecastDescriptionItem(arrayOfWeatherDescriptions.size - 1, it))
    }
}

