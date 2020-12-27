package com.octagon_technologies.sky_weather.ui.see_more_current

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.octagon_technologies.sky_weather.utils.Units
import com.octagon_technologies.sky_weather.utils.WindDirectionUnits
import com.octagon_technologies.sky_weather.utils.getAdvancedForecastDescription
import com.octagon_technologies.sky_weather.repository.network.single_forecast.SingleForecast
import com.octagon_technologies.sky_weather.ui.current_forecast.group_items.EachCurrentForecastDescriptionItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder


@BindingAdapter("getCurrentConditionsForSeeMorePage", "addSeeMoreAdapter", "addUnitSystemToSeeMore", "addWindDirectionToSeeMore")
fun RecyclerView.getCurrentConditionsForSeeMorePage(singleForecast: SingleForecast?, groupAdapter: GroupAdapter<GroupieViewHolder>?, units: Units, windDirectionUnits: WindDirectionUnits?) {
    layoutManager = LinearLayoutManager(context)
    adapter = groupAdapter
    val arrayOfWeatherDescriptions = getAdvancedForecastDescription(singleForecast, units, windDirectionUnits)

    arrayOfWeatherDescriptions.forEach {
        groupAdapter?.add(EachCurrentForecastDescriptionItem(30, it))
    }
}

