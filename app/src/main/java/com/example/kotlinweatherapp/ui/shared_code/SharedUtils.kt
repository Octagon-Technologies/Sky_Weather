package com.example.kotlinweatherapp.ui.shared_code

import android.os.Build
import android.os.Build.VERSION_CODES.M
import android.view.View
import android.view.WindowManager
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.kotlinweatherapp.MainActivity
import com.example.kotlinweatherapp.R
import timber.log.Timber

fun checkIfItemIsAtTheBottom(list: ArrayList<*>, itemPosition: Int): Boolean {
    val lastItemPosition = list.size - 1
    Timber.d("lastItemPosition is $lastItemPosition and itemPosition is $itemPosition")
    return lastItemPosition == itemPosition
}

fun Fragment.removeToolbarAndBottomNav(colorInt: Int = R.color.line_grey) {
    val mainActivity = (this.activity as MainActivity)
    val gone = View.GONE

    mainActivity.binding.contentLayout.navView.visibility = gone
    mainActivity.binding.topToolbarConstraint.visibility = gone

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        mainActivity.apply {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (Build.VERSION.SDK_INT >= M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            window.statusBarColor =
                ResourcesCompat.getColor(resources, colorInt, null)
        }
    }
}

fun Fragment.addToolbarAndBottomNav() {
    val mainActivity = (this.activity as MainActivity)
    val visible = View.VISIBLE

    mainActivity.binding.contentLayout.navView.visibility = visible
    mainActivity.binding.topToolbarConstraint.visibility = visible

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        mainActivity.apply {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (Build.VERSION.SDK_INT >= M) {
                window.decorView.systemUiVisibility = 0
            }
            window.statusBarColor =
                ResourcesCompat.getColor(resources, R.color.current_forecast_night_time, null)
        }
    }
}