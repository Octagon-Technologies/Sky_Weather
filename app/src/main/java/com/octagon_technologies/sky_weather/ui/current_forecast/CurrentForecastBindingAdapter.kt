package com.octagon_technologies.sky_weather.ui.current_forecast

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.domain.Allergy
import com.octagon_technologies.sky_weather.domain.Lunar
import com.octagon_technologies.sky_weather.domain.SingleForecast
import com.octagon_technologies.sky_weather.ui.current_forecast.group_items.EachCurrentForecastAllergyItem
import com.octagon_technologies.sky_weather.ui.current_forecast.group_items.MiniForecastDescription
import com.octagon_technologies.sky_weather.utils.Theme
import com.octagon_technologies.sky_weather.utils.Units
import com.octagon_technologies.sky_weather.utils.WindDirectionUnits
import com.octagon_technologies.sky_weather.utils.getBasicForecastConditions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

@BindingAdapter("getCurrentMoonHours")
fun TextView.getCurrentMoonHours(lunar: Lunar?) {
    text = resources.getString(
        R.string.hrs_format, getFinalHours(lunar?.moonRise, lunar?.moonSet)
    )
}

@BindingAdapter("getCurrentMoonMinutes")
fun TextView.getCurrentMoonMinutes(lunar: Lunar?) {
    text = resources.getString(
        R.string.mins_format, getFinalMinutes(lunar?.moonRise, lunar?.moonSet)
    )
}

@BindingAdapter("getCurrentSolarHours")
fun TextView.getCurrentSolarHours(lunar: Lunar?) {
    text = resources.getString(
        R.string.hrs_format, getFinalHours(lunar?.sunRise, lunar?.sunSet)
    )
}


@BindingAdapter("getCurrentSolarMinutes")
fun TextView.getCurrentSolarMinutes(lunar: Lunar?) {
    text = resources.getString(
        R.string.mins_format, getFinalMinutes(lunar?.sunRise, lunar?.sunSet)
    )
}

@BindingAdapter("getCurrentAllergyForecast")
fun RecyclerView.getCurrentAllergyForecast(allergy: Allergy?) {
    val groupAdapter = GroupAdapter<GroupieViewHolder>()
    adapter = groupAdapter

    val listOfMiniAllergy = listOf(allergy?.grassPollen, allergy?.weedPollen, allergy?.treePollen)

    listOfMiniAllergy.forEach { miniAllergy ->
        if (miniAllergy != null) {
            val currentForecastAllergyItem =
                EachCurrentForecastAllergyItem(listOfMiniAllergy.last() == miniAllergy, miniAllergy)
            groupAdapter.add(currentForecastAllergyItem)
        }
    }
}


@BindingAdapter(
    "setUpCurrentConditions",
    "addWindDirectionForCurrentConditions",
    "addUnitsForCurrentConditions",
    "addThemeForCurrentConditions"
)
fun RecyclerView.setUpCurrentConditions(
    singleForecast: SingleForecast?,
    windDirectionUnits: WindDirectionUnits?,
    units: Units?,
    theme: Theme?
) {
    val groupAdapter = GroupAdapter<GroupieViewHolder>()
    adapter = groupAdapter

    val arrayOfWeatherDescriptions =
        getBasicForecastConditions(singleForecast, units, windDirectionUnits)
    val lastIndex = arrayOfWeatherDescriptions.size - 1

    groupAdapter.addAll(
        arrayOfWeatherDescriptions.map { eachWeatherDescription ->
            MiniForecastDescription(
                isLastItem = arrayOfWeatherDescriptions.indexOf(eachWeatherDescription) == lastIndex,
                eachWeatherDescription = eachWeatherDescription,
                theme = null
            )
        }
    )
}