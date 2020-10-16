package com.example.kotlinweatherapp.ui.search_location

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinweatherapp.network.location.Location
import com.example.kotlinweatherapp.network.location.LocationItem
import com.example.kotlinweatherapp.ui.weather_forecast_objects.MainLocationObject
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchLocationViewModel(val context: Context) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _locationSuggestions = MutableLiveData<ArrayList<LocationItem>>()
    val locationSuggestions: LiveData<ArrayList<LocationItem>>
        get() = _locationSuggestions

    fun getLocationSuggestions(query: String) {
        if (query.isEmpty()) return

        uiScope.launch {
            _locationSuggestions.apply {
                value = MainLocationObject.getLocationSuggestionsFromQuery(query)
                Timber.d("locationSuggestions.size is $value")
                value?.forEach {
                    Timber.d("Each location in viewModel is $it")
                }

                val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
                val type = Types.newParameterizedType(List::class.java, LocationItem::class.java)
                val adapter: JsonAdapter<List<LocationItem>> = moshi.adapter(type)

                Timber.d("_locationSuggestions in json format is ${adapter.toJson(value) }")
            }
        }
    }
}