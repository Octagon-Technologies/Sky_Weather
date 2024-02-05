package com.octagon_technologies.sky_weather.widgets

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import com.octagon_technologies.sky_weather.domain.Location
import com.octagon_technologies.sky_weather.domain.SingleForecast
import com.octagon_technologies.sky_weather.models.WidgetData
import com.octagon_technologies.sky_weather.repository.database.toSingleForecast
import com.octagon_technologies.sky_weather.repository.network.weather.TomorrowApi
import com.octagon_technologies.sky_weather.utils.Units
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class WidgetSettings @Inject constructor(
    private val tomorrowApi: TomorrowApi,
    private val context: Context
) {
    private val jsonAdapter: JsonAdapter<List<WidgetData>> = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
        .adapter(Types.newParameterizedType(List::class.java, WidgetData::class.java))

    private val widgetListKey = preferencesKey<String>("widget_list_key")
    private val dataStore by lazy {
        context.createDataStore("widget_settings")
    }

    suspend fun getWeatherForecastFromWidgetData(location: Location, units: Units?): SingleForecast =
        tomorrowApi
            .getCurrentForecast(location = location.getCoordinates(), units = (units ?: Units.METRIC).value)
            .toSingleForecast()

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

    suspend fun getAllWidgets() =
        dataStore.data.map {
            val json = it[widgetListKey] ?: return@map null
            return@map jsonAdapter.fromJson(json)?.toMutableList()
        }.flowOn(Dispatchers.IO).lastOrNull() ?: mutableListOf()

}
