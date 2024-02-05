package com.octagon_technologies.sky_weather.main_activity

import androidx.lifecycle.ViewModel
import com.octagon_technologies.sky_weather.repository.repo.LocationRepo
import com.octagon_technologies.sky_weather.repository.repo.SettingsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val locationRepo: LocationRepo,
    private val settingsRepo: SettingsRepo
): ViewModel() {

    val location = locationRepo.location
    val theme = settingsRepo.theme

}