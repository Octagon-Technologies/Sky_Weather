package com.octagon_technologies.sky_weather.ui.daily_forecast

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.sky_weather.Units
import com.octagon_technologies.sky_weather.database.MainDataBase
import com.octagon_technologies.sky_weather.network.daily_forecast.EachDailyForecast
import com.octagon_technologies.sky_weather.network.lunar_forecast.LunarForecast
import com.octagon_technologies.sky_weather.network.mockLat
import com.octagon_technologies.sky_weather.network.mockLon
import com.octagon_technologies.sky_weather.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.network.selected_daily_forecast.SelectedDailyForecast
import com.octagon_technologies.sky_weather.ui.find_location.Coordinates
import com.octagon_technologies.sky_weather.ui.shared_code.MainDailyForecastObject
import com.octagon_technologies.sky_weather.ui.shared_code.MainLunarForecastObject
import com.octagon_technologies.sky_weather.ui.shared_code.MainSelectedDailyForecastObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class DailyForecastViewModel(context: Context) : ViewModel() {
    private val mainDataBase = MainDataBase.getInstance(context)
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _dailyForecast = MutableLiveData<ArrayList<EachDailyForecast>>()
    val dailyForecast: LiveData<ArrayList<EachDailyForecast>>
        get() = _dailyForecast

    private var _selectedDailyForecast = MutableLiveData<SelectedDailyForecast>()
    val selectedDailyForecast: LiveData<SelectedDailyForecast>
        get() = _selectedDailyForecast

    private var _lunarForecast = MutableLiveData<LunarForecast>()
    val lunarForecast: LiveData<LunarForecast>
        get() = _lunarForecast

    fun getDailyForecastAsync(
        location: ReverseGeoCodingLocation?,
        units: Units?
    ) {
        val coordinates = Coordinates(
            location?.lon?.toDouble() ?: mockLon,
            location?.lat?.toDouble() ?: mockLat
        )

        uiScope.launch {
            _dailyForecast.value =
                MainDailyForecastObject.getDailyForecastAsync(mainDataBase, coordinates, units)
                    .apply {
                        getSelectedDailyForecast(
                            coordinates,
                            this?.get(0) ?: return@apply,
                            units
                        )
                    }
        }
    }

    fun getSelectedDailyForecast(
        coordinates: Coordinates,
        eachDailyForecast: EachDailyForecast,
        units: Units?
    ) {
        // 2020-11-18
        viewModelScope.launch {
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(eachDailyForecast.observationTime?.value ?: return@launch)
            Timber.d("properFormattedDate is ${eachDailyForecast.observationTime.value}")

            _selectedDailyForecast.value =
                MainSelectedDailyForecastObject.getSelectedDailyForecastAsync(
                    coordinates,
                    eachDailyForecast.observationTime.value.toString(),
                    units
                )
            _lunarForecast.value =
                MainLunarForecastObject.getLunarForecastAsync(mainDataBase, coordinates, date?.time ?: 0)
        }

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}