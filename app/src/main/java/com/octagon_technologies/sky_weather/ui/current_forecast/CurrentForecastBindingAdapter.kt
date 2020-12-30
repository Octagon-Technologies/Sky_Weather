package com.octagon_technologies.sky_weather.ui.current_forecast

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.models.EachAllergyDescription
import com.octagon_technologies.sky_weather.repository.network.allergy_forecast.Allergy
import com.octagon_technologies.sky_weather.repository.network.lunar_forecast.LunarForecast
import com.octagon_technologies.sky_weather.repository.network.single_forecast.FeelsLike
import com.octagon_technologies.sky_weather.repository.network.single_forecast.SingleForecast
import com.octagon_technologies.sky_weather.repository.network.single_forecast.Temp
import com.octagon_technologies.sky_weather.ui.current_forecast.group_items.EachCurrentForecastAllergyItem
import com.octagon_technologies.sky_weather.ui.current_forecast.group_items.EachCurrentForecastDescriptionItem
import com.octagon_technologies.sky_weather.utils.WindDirectionUnits
import com.octagon_technologies.sky_weather.utils.getBasicForecastConditions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import timber.log.Timber

@BindingAdapter("getCurrentRealFeel")
fun TextView.getCurrentRealFeel(realFeelsLike: FeelsLike?) {
    this.text = resources.getString(
        R.string.feelslike_format,
        realFeelsLike?.value?.toInt()?.toString() ?: "--"
    )
}

@BindingAdapter("getCurrentTemp")
fun TextView.getCurrentTemp(temp: Temp?) {
    this.text = resources.getString(
        R.string.temp_format,
        temp?.value?.toInt()?.toString() ?: "--"
    )
}

@BindingAdapter("getCurrentMoonHours")
fun TextView.getCurrentMoonHours(lunarForecast: LunarForecast?) {
    text = resources.getString(
        R.string.hrs_format, getFinalHours(lunarForecast?.moonRise, lunarForecast?.moonSet)
    )
}

@BindingAdapter("getCurrentMoonMinutes")
fun TextView.getCurrentMoonMinutes(lunarForecast: LunarForecast?) {
    text = resources.getString(
        R.string.mins_format, getFinalMinutes(lunarForecast?.moonRise, lunarForecast?.moonSet)
    )
}

@BindingAdapter("getCurrentSolarHours")
fun TextView.getCurrentSolarHours(lunarForecast: LunarForecast?) {
    text = resources.getString(
        R.string.hrs_format, getFinalHours(lunarForecast?.sunRise, lunarForecast?.sunSet)
    )
}

@BindingAdapter("getCurrentSolarMinutes")
fun TextView.getCurrentSolarMinutes(lunarForecast: LunarForecast?) {
    text = resources.getString(
        R.string.mins_format, getFinalMinutes(lunarForecast?.sunRise, lunarForecast?.sunSet)
    )
}

@BindingAdapter("getCurrentAllergyForecast")
fun RecyclerView.getCurrentAllergyForecast(allergyForecast: Allergy?) {
    layoutManager = LinearLayoutManager(context)
    val groupAdapter = GroupAdapter<GroupieViewHolder>()
    adapter = groupAdapter
    val arrayOfAllergyForecast = ArrayList<EachAllergyDescription>()

    try {
        allergyForecast?.data?.get(0)?.risk?.let {
//        arrayOfAllergyForecast.add(EachAllergyDescription("Overall Risk", it.risk.toString()))
            arrayOfAllergyForecast.add(EachAllergyDescription("Grass Pollen", it.grassPollen))
            arrayOfAllergyForecast.add(EachAllergyDescription("Tree Pollen", it.treePollen))
            arrayOfAllergyForecast.add(EachAllergyDescription("Weed Pollen", it.weedPollen))
        }
    } catch (emptyList: IndexOutOfBoundsException) {
        Timber.i("Selected country does't have pollen data")
    }

    arrayOfAllergyForecast.forEach {
        val currentForecastAllergyItem =
            EachCurrentForecastAllergyItem(arrayOfAllergyForecast.size - 1, it)
        groupAdapter.add(currentForecastAllergyItem)
    }
}


@BindingAdapter("getCurrentConditionsForMainPage", "addWindDirectionForCurrentConditions")
fun RecyclerView.getCurrentConditionsForMainPage(singleForecast: SingleForecast?, windDirectionUnits: WindDirectionUnits?) {
    layoutManager = LinearLayoutManager(context)
    val groupAdapter = GroupAdapter<GroupieViewHolder>()
    adapter = groupAdapter
    val arrayOfWeatherDescriptions = getBasicForecastConditions(singleForecast, windDirectionUnits)

    arrayOfWeatherDescriptions.forEach {
        groupAdapter.add(
            EachCurrentForecastDescriptionItem(
                arrayOfWeatherDescriptions.size - 1,
                it,
                null
            )
        )
    }
}