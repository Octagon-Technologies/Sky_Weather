package com.example.kotlinweatherapp.ui.current_forecast

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweatherapp.network.allergy_forecast.Allergy
import com.example.kotlinweatherapp.network.current_forecast.FeelsLike
import com.example.kotlinweatherapp.network.current_forecast.SingleForecast
import com.example.kotlinweatherapp.network.lunar_forecast.LunarForecast
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

@BindingAdapter("getCurrentRealFeel")
fun TextView.getCurrentRealFeel(realFeelsLike: FeelsLike?) {
    this.text = "RealFeel© ${realFeelsLike?.value?.toInt()}°"
}

// "moonSet": "6:44", -> 644 -> 3044
// "moonRise": "19:02", -> 1902
// moonHours: 1142
@BindingAdapter("getCurrentMoonHours")
fun TextView.getCurrentMoonHours(lunarForecast: LunarForecast?) {
    val basicTime = getLunarBasicTime(lunarForecast)

    text = "${basicTime.toString().take(2)} hrs"
}

@BindingAdapter("getCurrentMoonMinutes")
fun TextView.getCurrentMoonMinutes(lunarForecast: LunarForecast?) {
    val basicTime = getLunarBasicTime(lunarForecast)

    text = "${basicTime.toString().takeLast(2)} mins"
}

@BindingAdapter("getCurrentSolarHours")
fun TextView.getCurrentSolarHours(lunarForecast: LunarForecast?) {
    val basicTime = getSolarBasicTime(lunarForecast)

    text = "${basicTime.toString().take(2)} hrs"
}


@BindingAdapter("getCurrentSolarMinutes")
fun TextView.getCurrentSolarMinutes(lunarForecast: LunarForecast?) {
    val basicTime = getSolarBasicTime(lunarForecast)

    text = "${basicTime.toString().takeLast(2)} mins"
}

@BindingAdapter("getCurrentAllergyForecast")
fun RecyclerView.getCurrentAllergyForecast(allergyForecast: Allergy?) {
    layoutManager = LinearLayoutManager(context)
    val groupAdapter = GroupAdapter<GroupieViewHolder>()
    adapter = groupAdapter
    val arrayOfAllergyForecast = ArrayList<EachAllergyDescription>()

    allergyForecast?.data?.let {
        arrayOfAllergyForecast.add(EachAllergyDescription("Overall Risk", it.risk.toString()))
        arrayOfAllergyForecast.add(EachAllergyDescription("Grass Pollen", getGrassPollen(it.grassPollen)))
        arrayOfAllergyForecast.add(EachAllergyDescription("Tree Pollen", getTreePollen(it.treePollen)))
        arrayOfAllergyForecast.add(EachAllergyDescription("Weed Pollen", getWeedPollen(it.weedPollen)))
    }

    arrayOfAllergyForecast.forEach {
        val currentForecastAllergyItem = EachCurrentForecastAllergyItem(arrayOfAllergyForecast.size - 1, it)
        groupAdapter.add(currentForecastAllergyItem)
    }
}


@BindingAdapter("getCurrentConditionsForMainPage")
fun RecyclerView.getCurrentConditionsForMainPage(singleForecast: SingleForecast?) {
    layoutManager = LinearLayoutManager(context)
    val groupAdapter = GroupAdapter<GroupieViewHolder>()
    adapter = groupAdapter
    val arrayOfWeatherDescriptions = getBasicForecastConditions(singleForecast)

    arrayOfWeatherDescriptions.forEach {
        groupAdapter.add(EachCurrentForecastDescriptionItem(arrayOfWeatherDescriptions.size - 1, it))
    }
}