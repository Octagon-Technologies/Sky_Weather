package com.example.kotlinweatherapp

import android.content.res.ColorStateList
import android.os.Build
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.kotlinweatherapp.database.MainDataBase
import com.example.kotlinweatherapp.databinding.ActivityMainBinding
import com.example.kotlinweatherapp.network.reverse_geocoding_location.ReverseGeoCodingLocation
import com.example.kotlinweatherapp.ui.weather_forecast_objects.MainLocationObject
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController

    var reverseLocation: ReverseGeoCodingLocation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each

        binding.locationNameLayout.setOnClickListener {
            navController.navigate(R.id.findLocationFragment)
        }
        NavigationUI.setupWithNavController(navView, navController)

        binding.menuBtn.setOnClickListener { navController.navigate(R.id.settingsFragment) }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor =
                ResourcesCompat.getColor(resources, R.color.current_forecast_night_time, null)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    fun menuImageOnClick(view: View) {
        Timber.d("menuImageOnClick called")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        CoroutineScope(Dispatchers.Main).launch {

            val mainDataBase = MainDataBase.getInstance(applicationContext)
            reverseLocation = MainLocationObject.getLocalLocationAsync(mainDataBase)
            Timber.d("reverseLocation is $reverseLocation")
            Timber.d("navController.currentDestination is ${navController.currentDestination?.navigatorName}")

            if (navController.currentDestination == NavDestination("com.example.kotlinweatherapp.ui.find_location.FindLocationFragment")) {
                Timber.d("NavDestination is com.example.kotlinweatherapp.ui.find_location.FindLocationFragment")
                if (reverseLocation != null) {
                    navController.popBackStack()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Location is needed to get weather forecast.",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            }
            else {
                super.onBackPressed()
            }

        }
    }


}