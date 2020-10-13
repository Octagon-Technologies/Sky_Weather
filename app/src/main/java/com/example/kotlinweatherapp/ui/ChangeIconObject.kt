package com.example.kotlinweatherapp.ui

import android.content.Context
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.kotlinweatherapp.MainActivity
import com.example.kotlinweatherapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

object ChangeIconObject {

    fun changeIconDrawable(fragment: Fragment) {
        val bottomNavigationBar = (fragment.activity as MainActivity).findViewById<BottomNavigationView>(R.id.nav_view)

//        when (bottomNavigationBar.selectedItemId) {
//            R.id.currentForecastFragment -> {
//                bottomNavigationBar[0]
//            }
//            R.id.hourlyFragment -> {}
//            R.id.dailyForecast -> {}
//            R.id.radarFragment -> {}
//        }
    }

}