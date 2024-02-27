package com.octagon_technologies.sky_weather.main_activity

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityOptionsCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.databinding.ActivityMainBinding
import com.octagon_technologies.sky_weather.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()
    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setUpDarkMode()
        setUpStatusBarAndNavigationBar()

        ActivityOptionsCompat.makeCustomAnimation(
            applicationContext,
            R.animator.nav_default_enter_anim,
            R.animator.nav_default_exit_anim
        )

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

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


    private fun setUpDarkMode() {
        viewModel.theme.observe(this@MainActivity) { theme ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                setUpDarkModeForAndroidS(theme)
            } else {
                setUpDarkModeCompat(theme)
            }
        }
    }

    private fun setUpDarkModeCompat(theme: Theme) {
        AppCompatDelegate.setDefaultNightMode(
            when (theme) {
                Theme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                Theme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun setUpDarkModeForAndroidS(uiMode: Theme) {
        val uiModeManager =
            baseContext.getSystemService(UI_MODE_SERVICE) as UiModeManager

        when (uiMode) {
            Theme.LIGHT ->
                uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_NO)

            Theme.DARK ->
                uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES)
//            else ->
//                uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_AUTO)
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
