package com.octagon_technologies.sky_weather.widgets

import android.content.Context
import androidx.datastore.preferences.*
import com.octagon_technologies.sky_weather.network.WeatherForecastRetrofitItem
import com.octagon_technologies.sky_weather.repository.network.single_forecast.SingleForecast
import com.octagon_technologies.sky_weather.ui.find_location.Coordinates
import com.octagon_technologies.sky_weather.utils.Units
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber

class WidgetSettings(private val context: Context) {
    private val jsonAdapter: JsonAdapter<List<WidgetData>> = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
        .adapter(Types.newParameterizedType(List::class.java, WidgetData::class.java))

    private val widgetListKey = preferencesKey<String>("widget_list_key")
    private val dataStore by lazy {
        context.createDataStore("widget_settings")
    }

    suspend fun getWeatherForecastFromWidgetData(coordinates: Coordinates?, units: Units): SingleForecast? {
        return WeatherForecastRetrofitItem.weatherRetrofitService
            .getCurrentForecastAsync(
                unitSystem = units.value,
                lon = coordinates?.lon ?: return null,
                lat = coordinates.lat
            )
            .await()
    }

    suspend fun addWidgetId(widgetData: WidgetData) {
        updateWidgetList { formerList ->
            Timber.d("formerList is $formerList in removeWidgetId()")
            formerList.apply {
                add(widgetData)
            }
        }
    }

    suspend fun removeWidgetId(widgetId: Int) {
        updateWidgetList { formerList ->
            // Return a new list whose items does not contain the removed widget id.
            Timber.d("formerList is $formerList in removeWidgetId()")
            formerList.filter { it.widgetId != widgetId }
        }
    }


    private suspend fun updateWidgetList(
        addOrRemoveWidgetLambda: (MutableList<WidgetData>) -> List<WidgetData>
    ) {
        withContext(Dispatchers.IO) {
            val newWidgetList = addOrRemoveWidgetLambda(getAllWidgets())
            Timber.d("newWidgetList is $newWidgetList in updateWidgetList()")
            dataStore.edit {
                it[widgetListKey] = jsonAdapter.toJson(newWidgetList) ?: return@edit
            }
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun getAllWidgets() =
        dataStore.data.map {
            val json = it[widgetListKey] ?: return@map null
            return@map jsonAdapter.fromJson(json)?.toMutableList()
        }.flowOn(Dispatchers.IO).first() ?: mutableListOf()

}
