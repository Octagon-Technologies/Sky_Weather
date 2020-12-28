package com.octagon_technologies.sky_weather.widgets

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.databinding.ActivityWidgetConfigureBinding
import com.octagon_technologies.sky_weather.utils.*
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * The configuration screen for the [WeatherWidget] AppWidget.
 */
@AndroidEntryPoint
class WidgetConfigureActivity : AppCompatActivity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    @Inject
    lateinit var widgetRepo: WidgetRepo

    val viewModel by viewModels<WidgetConfigureViewModel>()

    private lateinit var binding: ActivityWidgetConfigureBinding
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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_widget_configure)
        binding.also {
            it.lifecycleOwner = this
            it.viewModel = viewModel

            // Make RemoteViewDisplay's transparency to match the default 70%
            it.remoteViewDisplay.setBackgroundColor(getTransparentColorFromProgress())

            it.locationLayout.setOnClickListener {
                viewModel.navigateToLocationFragment()
                changeStatusBarIcons(true)
                changeStatusBarColor(R.color.color_black)
                changeSystemNavigationBarColor(R.color.dark_black)
            }
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
                    showShortSnackBar("Select location first.")

                    viewModel.shouldCreateWidget.value = false
                    return@observe
                }

                widgetRepo.createWidget(
                    appWidgetId,
                    getTransparentColorFromProgress(),
                    location
                ) { result ->
                    result
                        .onSuccess {
                            // Make sure we pass back the original appWidgetId
                            val resultValue = Intent()
                            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                            setResult(RESULT_OK, resultValue)
                            finish()
                        }
                        .onFailure { throwable ->
                            showShortSnackBar(throwable.message ?: return@onFailure)
                            Timber.d(throwable)
                        }
                }
            }
        }

        viewModel.reverseGeoCodingLocation.observe(this) {
            binding.widgetLocationArea.text = it.getDisplayLocation()
            binding.widgetCountry.text = it?.reverseGeoCodingAddress?.country ?: "--"
        }

        viewModel.navigateToLocationFragment.observe(this) {
            if (it == false) {
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

    private fun showShortSnackBar(message: String) =
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(getResColor(R.color.dark_orange))
            .setTextColor(getResColor(android.R.color.white))
            .show()

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