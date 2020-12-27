package com.octagon_technologies.sky_weather

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.octagon_technologies.sky_weather.utils.changeStatusBarColor
import com.octagon_technologies.sky_weather.utils.changeStatusBarIcons
import com.octagon_technologies.sky_weather.utils.changeSystemNavigationBarColor
import com.octagon_technologies.sky_weather.utils.setUpStatusBarAndNavigationBar

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpStatusBarAndNavigationBar()
    }

    override fun onStart() {
        super.onStart()
        changeStatusBarIcons(true)
        changeStatusBarColor(R.color.light_blue)
        changeSystemNavigationBarColor(R.color.dark_theme_blue)

        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}