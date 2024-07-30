package com.octagontechnologies.sky_weather.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.octagontechnologies.sky_weather.R
import com.octagontechnologies.sky_weather.main_activity.MainActivity
import timber.log.Timber

fun checkBuildVersionFrom(minimumSdkVersion: Int) = Build.VERSION.SDK_INT >= minimumSdkVersion

fun Context.getResColor(@ColorRes colorRes: Int) =
    ResourcesCompat.getColor(resources, colorRes, null)

private fun Fragment.showToast(message: String, length: Int) {
    Toast.makeText(requireContext(), message, length).show()
}

fun Fragment.showLongToast(message: String) = showToast(message, Toast.LENGTH_LONG)
fun Fragment.showShortToast(message: String) = showToast(message, Toast.LENGTH_SHORT)

fun Fragment.getStringResource(@StringRes stringRes: Int) = resources.getString(stringRes)

fun Fragment.removeToolbarAndBottomNav() {
    if (activity is MainActivity) {
        val mainActivity = (activity as MainActivity)
        val gone = View.GONE

        mainActivity.binding.apply {
            navView.visibility = gone
            topToolbarConstraint.visibility = gone
            topLineDivider.visibility = gone
        }
    }

//    activity?.apply {
//        changeStatusBarColor(statusBarColor)
//        changeStatusBarIcons(isWhiteIcon)
//    }
}

fun Fragment.addToolbarAndBottomNav(theme: Theme?, includeBottomNavView: Boolean = true) =
    (activity as MainActivity).addToolbarAndBottomNav(theme, includeBottomNavView)


fun MainActivity.addToolbarAndBottomNav(theme: Theme?, includeBottomNavView: Boolean = true) {
    val visible = View.VISIBLE
    val defaultColor =
        if (theme == Theme.LIGHT) R.color.current_forecast_night_time else R.color.dark_theme_blue
    Timber.d("Theme is $theme")

    binding.apply {
        if (includeBottomNavView) navView.visibility = visible
        topToolbarConstraint.visibility = visible
        topLineDivider.visibility = visible
    }

//    changeStatusBarIcons(true)
//    changeStatusBarColor(defaultColor)
}

fun Activity.changeStatusBarIcons(isWhite: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window?.decorView?.systemUiVisibility = if (isWhite) whiteStatusIcons else darkStatusIcons
    }
}

fun Activity.changeStatusBarColor(@ColorRes colorRes: Int) {
    window?.statusBarColor = getResColor(colorRes)
}

fun Activity.changeSystemNavigationBarColor(@ColorRes colorRes: Int) {
    window?.navigationBarColor = getResColor(colorRes)
}

fun Fragment.changeSystemNavigationBarColor(@ColorRes colorRes: Int) {
    activity?.changeSystemNavigationBarColor(colorRes)
}

fun Activity.setUpStatusBarAndNavigationBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        window.decorView.systemUiVisibility = whiteStatusIcons
    }
}
