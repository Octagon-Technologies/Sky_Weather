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
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.octagon_technologies.sky_weather.database.MainDataBase
import com.octagon_technologies.sky_weather.databinding.ActivityMainBinding
import com.octagon_technologies.sky_weather.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.octagon_technologies.sky_weather.network.single_forecast.SingleForecast
import com.octagon_technologies.sky_weather.ui.shared_code.MainLocationObject
import com.octagon_technologies.sky_weather.ui.shared_code.MainSettings
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val navController: NavController by lazy { findNavController(R.id.nav_host_fragment) }
    private val mainDataBase: MainDataBase? by lazy { MainDataBase.getInstance(applicationContext) }
    private val mainSettings: MainSettings by lazy { MainSettings(applicationContext) }
    val singleForecastJsonAdapter: JsonAdapter<SingleForecast> by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
            .adapter(SingleForecast::class.java)
    }

    private val uiScope = CoroutineScope(Dispatchers.Main)
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

        uiScope.launch {
            liveLocation.value = MainLocationObject.getLocalLocationAsync(mainDataBase)
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
                "Location is needed to get weather forecast.",
                Toast.LENGTH_LONG
            ).show()

            finish()
        } else {
            super.onBackPressed()
        }
    }

}
