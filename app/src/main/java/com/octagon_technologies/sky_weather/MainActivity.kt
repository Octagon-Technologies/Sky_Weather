package com.octagon_technologies.sky_weather

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.octagon_technologies.sky_weather.databinding.ActivityMainBinding
import com.octagon_technologies.sky_weather.repository.LocationRepo
import com.octagon_technologies.sky_weather.repository.database.MainDataBase
import com.octagon_technologies.sky_weather.repository.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.repository.network.single_forecast.SingleForecast
import com.octagon_technologies.sky_weather.ui.shared_code.SettingsRepo
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }
    private val mainDataBase by lazy { MainDataBase.getInstance(applicationContext) }
    private val mainSettings by lazy { SettingsRepo(applicationContext) }
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.decorView.systemUiVisibility = whiteStatusIcons
                }
                window.statusBarColor =
                    ResourcesCompat.getColor(resources, R.color.dark_theme_blue, null)
            }

        ActivityOptionsCompat.makeCustomAnimation(
            applicationContext,
            R.animator.nav_default_enter_anim,
            R.animator.nav_default_exit_anim
        )

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.theme = liveTheme

        GlobalScope.launch(Dispatchers.Main) {
            liveLocation.value = LocationRepo.getLocalLocationAsync(mainDataBase)
            if (liveLocation.value == null) navController.navigate(R.id.findLocationFragment)

            liveTheme.value = mainSettings.getTheme()
            liveWindDirectionUnits.value = mainSettings.getWindDirections()
            liveTimeFormat.value = mainSettings.getTimeFormat()
            liveUnits.value = mainSettings.getUnits()
            liveNotificationAllowed.value = mainSettings.getNotificationAllowed()
        }

        binding.locationName.setOnClickListener { navController.navigate(R.id.findLocationFragment) }
        binding.menuBtn.setOnClickListener { navController.navigate(R.id.settingsFragment) }

        NavigationUI.setupWithNavController(binding.navView, navController)

        liveLocation.observe(this, {
            binding.locationName.text = it?.getDisplayLocation()
        })
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

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
