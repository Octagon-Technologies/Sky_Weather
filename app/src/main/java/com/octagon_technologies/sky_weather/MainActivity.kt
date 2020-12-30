package com.octagon_technologies.sky_weather

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.octagon_technologies.sky_weather.databinding.ActivityMainBinding
import com.octagon_technologies.sky_weather.repository.LocationRepo
import com.octagon_technologies.sky_weather.repository.SettingsRepo
import com.octagon_technologies.sky_weather.repository.database.WeatherDataBase
import com.octagon_technologies.sky_weather.repository.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.repository.network.single_forecast.SingleForecast
import com.octagon_technologies.sky_weather.utils.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var weatherDataBase: WeatherDataBase
    @Inject
    lateinit var settingsRepo: SettingsRepo

    private val navController by lazy { findNavController(R.id.nav_host_fragment) } // TODO - There is a new extension method you can use
    val singleForecastJsonAdapter: JsonAdapter<SingleForecast> by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
            .adapter(SingleForecast::class.java)
    }

    var hasNotificationChanged = false

    var liveLocation = MutableLiveData<ReverseGeoCodingLocation>()
    var liveTheme = MutableLiveData<Theme>()
    var liveWindDirectionUnits = MutableLiveData<WindDirectionUnits>()
    var liveTimeFormat = MutableLiveData<TimeFormat>()
    var liveUnits = MutableLiveData<Units>()
    var liveNotificationAllowed = MutableLiveData(true)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpStatusBarAndNavigationBar()

        ActivityOptionsCompat.makeCustomAnimation(
            applicationContext,
            R.animator.nav_default_enter_anim,
            R.animator.nav_default_exit_anim
        )

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.theme = liveTheme

        GlobalScope.launch(Dispatchers.Main) {
            liveLocation.value = LocationRepo.getLocalLocationAsync(weatherDataBase)
            if (liveLocation.value == null) navController.navigate(R.id.findLocationFragment)

            liveTheme.value = settingsRepo.getTheme()
            liveWindDirectionUnits.value = settingsRepo.getWindDirections()
            liveTimeFormat.value = settingsRepo.getTimeFormat()
            liveUnits.value = settingsRepo.getUnits()
            liveNotificationAllowed.value = settingsRepo.getNotificationAllowed()
        }

        binding.locationName.setOnClickListener { navController.navigate(R.id.findLocationFragment) }
        binding.menuBtn.setOnClickListener { navController.navigate(R.id.settingsFragment) }

        NavigationUI.setupWithNavController(binding.navView, navController)

        liveLocation.observe(this) {
            binding.locationName.text = it?.getDisplayLocation()
        }
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    // TODO - Setup Reactive Permission request (https://github.com/permissions-dispatcher/PermissionsDispatcher)
    // https://gist.github.com/michaelbukachi/95f6b6cf70900101523a1f6e3e04b6d7
    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.findLocationFragment) {
            liveLocation.value?.let {
                navController.popBackStack()
                return
            }

            Toast.makeText(
                applicationContext,
                resources.getString(R.string.location_is_needed_to_get_weather_forecast),
                Toast.LENGTH_SHORT
            ).show()

            finish()
        } else {
            super.onBackPressed()
        }
    }

}
