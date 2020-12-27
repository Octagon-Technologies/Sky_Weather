package com.octagon_technologies.sky_weather.widgets

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.databinding.WidgetConfigureBinding
import com.octagon_technologies.sky_weather.utils.*

/**
 * The configuration screen for the [WeatherWidget] AppWidget.
 */
class WidgetConfigureActivity : AppCompatActivity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private val widgetRepo by lazy { WidgetRepo(applicationContext) }

    val viewModel by lazy {
        ViewModelProvider(
            this,
            WidgetConfigureViewModel.Factory(applicationContext)
        )[WidgetConfigureViewModel::class.java]
    }

    private lateinit var binding: WidgetConfigureBinding
    private lateinit var navController: NavController

    @SuppressLint("NewApi")
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED)

        setUpStatusBarAndNavigationBar()

        // Find the widget id from the intent.
        intent.extras?.let {
            appWidgetId = it.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        binding = DataBindingUtil.setContentView(this, R.layout.widget_configure)
        binding.also {
            it.lifecycleOwner = this
            it.viewModel = viewModel

            // Make RemoteViewDisplay's transparency to match the default 70%
            it.remoteViewDisplay.setBackgroundColor(getTransparentColorFromProgress())
        }
        navController = findNavController(R.id.widget_nav_host_fragment)

        setUpSeekbarWithPercentTag()

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }

        viewModel.shouldCreateWidget.observe(this) {
            if (it == true) {
                val location = viewModel.reverseGeoCodingLocation.value ?: run {
                    Snackbar.make(binding.root, "Select location first.", Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(getResColor(R.color.dark_orange))
                        .setTextColor(getResColor(android.R.color.white))
                        .show()

                    viewModel.shouldCreateWidget.value = false
                    return@observe
                }

                widgetRepo.createWidget(appWidgetId, getTransparentColorFromProgress(), location) {
                    // Make sure we pass back the original appWidgetId
                    val resultValue = Intent()
                    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                    setResult(RESULT_OK, resultValue)
                    finish()
                }
            }
        }

        viewModel.reverseGeoCodingLocation.observe(this) {
            binding.widgetLocationArea.text = it.getDisplayLocation()
            binding.widgetCountry.text = it?.reverseGeoCodingAddress?.country ?: "--"
        }

        viewModel.navigateToLocationFragment.observe(this) {
            if (it == true) {
                changeStatusBarIcons(true)
                changeStatusBarColor(R.color.color_black)
                changeSystemNavigationBarColor(R.color.dark_black)
            } else {
                changeStatusBarIcons(true)
                changeStatusBarColor(R.color.light_blue)
                changeSystemNavigationBarColor(R.color.dark_theme_blue)
            }
        }
    }

    private fun setUpSeekbarWithPercentTag() {
        binding.transparencySeekbar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                @SuppressLint("SetTextI18n")
                override fun onProgressChanged(
                    seekBar: SeekBar?, progress: Int, fromUser: Boolean
                ) {
                    binding.transparencyPercent.text = "$progress%"
                    binding.remoteViewDisplay.setBackgroundColor(getTransparentColorFromProgress())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
    }

    private fun getTransparentColorFromProgress() =
        ColorUtils.setAlphaComponent(
            getResColor(android.R.color.white),
            (binding.transparencySeekbar.progress * 2.55).toInt()
        )

    override fun onBackPressed() {
        if (viewModel.navigateToLocationFragment.value == true) {
            if (navController.currentDestination?.id == R.id.widgetSearchLocationFragment)
                navController.popBackStack()
            else if (navController.currentDestination?.id == R.id.widgetFindLocationFragment)
                viewModel.navigateToLocationFragment.value = false
        } else {
            super.onBackPressed()
        }
    }
}