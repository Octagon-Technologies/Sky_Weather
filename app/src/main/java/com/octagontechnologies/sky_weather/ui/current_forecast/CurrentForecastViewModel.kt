package com.octagontechnologies.sky_weather.ui.current_forecast

//import com.octagontechnologies.sky_weather.ads.AdRepo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.octagontechnologies.sky_weather.domain.Location
import com.octagontechnologies.sky_weather.notification.CustomNotificationCompat
import com.octagontechnologies.sky_weather.repository.repo.CurrentForecastRepo
import com.octagontechnologies.sky_weather.repository.repo.HourlyForecastRepo
import com.octagontechnologies.sky_weather.repository.repo.LocationRepo
import com.octagontechnologies.sky_weather.repository.repo.LunarRepo
import com.octagontechnologies.sky_weather.repository.repo.SettingsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CurrentForecastViewModel @Inject constructor(
    private val currentForecastRepo: CurrentForecastRepo,
    private val hourlyForecastRepo: HourlyForecastRepo,
    private val lunarRepo: LunarRepo,
    private val settingsRepo: SettingsRepo,
    private val locationRepo: LocationRepo,
    private val customNotificationCompat: CustomNotificationCompat
) : ViewModel() {

    val theme = settingsRepo.theme
    val units = settingsRepo.units
    val windDirectionUnits = settingsRepo.windDirectionUnits
    val timeFormat = settingsRepo.timeFormat

    val location = locationRepo.location.stateIn(viewModelScope, SharingStarted.Eagerly, null)


    val currentForecast =
        currentForecastRepo.currentForecast.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val lunarForecast = lunarRepo.currentLunar

    val predictions = hourlyForecastRepo.listOfHourlyForecast.map {
        it?.filterIndexed { index, _ ->
            index in listOf(0, 1, 2)
        }
    }


    init {
        viewModelScope.launch {
            launch {
//                location.collectLatest { location ->
//                    if (location != null) {
//                        currentForecastRepo.refreshCurrentForecast(location)
//                        hourlyForecastRepo.refreshHourlyForecast(location)
//                        lunarRepo.refreshCurrentLunarForecast(location)
//                    }
//                }
            }

            launch {
                currentForecast.collectLatest { currentForecast ->
                    if (currentForecast != null && location.value != null) {
                        updateNotification(location.value!!)
                    }
                }
            }
        }
    }

    private fun updateNotification(location: Location) {
        val isNotificationAllowed = settingsRepo.isNotificationAllowed.value
        Timber.d("updateNotification called: isNotificationAllowed is $isNotificationAllowed")


        if (isNotificationAllowed) {
            customNotificationCompat.createNotification(
                singleForecast = currentForecast.value,
                location = location,
                units = units.value
            )
        }
    }
}