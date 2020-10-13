package com.example.kotlinweatherapp.ui.see_more_current

import com.example.kotlinweatherapp.network.current_forecast.SingleForecast
import com.example.kotlinweatherapp.ui.current_forecast.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


fun getAdvancedForecastDescription(singleForecast: SingleForecast?): ArrayList<EachWeatherDescription> {
    val arrayOfWeatherDescriptions = ArrayList<EachWeatherDescription>()
    singleForecast?.let {
        getBasicForecastConditions(it)

        // TODO: Add other major things in current conditions fragment
        arrayOfWeatherDescriptions.add(EachWeatherDescription("Dew Point", "${it.dewPoint?.value ?: 0}°"))
        arrayOfWeatherDescriptions.add(EachWeatherDescription("Pressure", "${it.baroPressure?.value?.toInt() ?: 0} mbar"))
        arrayOfWeatherDescriptions.add(EachWeatherDescription("Cloud Cover", "${it.cloudCover?.value?.toInt() ?: 0}%"))
        arrayOfWeatherDescriptions.add(EachWeatherDescription("Visibility", "${it.visibility?.value?.toInt()} km"))
        arrayOfWeatherDescriptions.add(
            EachWeatherDescription(
                "Cloud Ceiling",
                if (it.cloudCeiling?.value == null) "None" else "${it.cloudCeiling.value} m"
            )
        )
        arrayOfWeatherDescriptions.add(EachWeatherDescription("Moon Phase", removeUnderScoreFromString(it.moonPhase?.value ?: "Black Moon")))
    }

    return arrayOfWeatherDescriptions
}

fun removeUnderScoreFromString(input: String): String {
    var outputString = ""
    input.split("_").forEach { eachLetter ->
        outputString += eachLetter.capitalize(Locale.ROOT)
    }
    Timber.d("outputString is $outputString")
    return outputString
}