package com.octagon_technologies.sky_weather.main_activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.databinding.ActivityMainBinding
import com.octagon_technologies.sky_weather.repository.database.WeatherDataBase
import com.octagon_technologies.sky_weather.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()
    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setUpStatusBarAndNavigationBar()

        ActivityOptionsCompat.makeCustomAnimation(
            applicationContext,
            R.animator.nav_default_enter_anim,
            R.animator.nav_default_exit_anim
        )

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.theme = viewModel.theme

        binding.locationName.setOnClickListener { navController.navigate(R.id.findLocationFragment) }
        binding.menuBtn.setOnClickListener { navController.navigate(R.id.settingsFragment) }

        NavigationUI.setupWithNavController(binding.navView, navController)

        viewModel.location.observe(this) { location ->
            if (location == null)
                navController.navigate(R.id.findLocationFragment)
            else
                binding.locationName.text = location.displayName
        }
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.findLocationFragment) {
            viewModel.location.value?.let {
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
