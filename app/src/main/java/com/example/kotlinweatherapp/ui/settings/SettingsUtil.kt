package com.example.kotlinweatherapp.ui.settings

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toColor
import androidx.databinding.BindingAdapter
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.databinding.SettingsFragmentBinding
import com.example.kotlinweatherapp.ui.weather_forecast_objects.*
import timber.log.Timber

data class BasicSettingsDataClass(
    val units: Units?,
    val windDirectionUnits: WindDirectionUnits?,
    val timeFormat: TimeFormat?,
    val displayMode: DisplayMode?,
    val mainSettings: MainSettings
)

fun ConstraintLayout.doUnitsChange(viewModel: SettingsViewModel, binding: SettingsFragmentBinding) {
    val basicSettingsDataClass = viewModel.basicSettingsDataClass.value
    if (basicSettingsDataClass?.units == Units.METRIC) {
        if (this == binding.unitsLeftLayout) setBackgroundResource(R.drawable.left_selected_each_settings_item_background)
        else setBackgroundResource(R.drawable.right_each_settings_item_background)
    }
    else {
        if (this == binding.unitsRightLayout) setBackgroundResource(R.drawable.right_selected_each_settings_item_background)
        else setBackgroundResource(R.drawable.left_each_settings_item_background)
    }

    setOnClickListener {

        basicSettingsDataClass?.mainSettings?.editDataStore(when (this) {
            binding.unitsLeftLayout -> {
                setBackgroundResource(R.drawable.left_selected_each_settings_item_background)
                binding.unitsRightLayout.setBackgroundResource(R.drawable.right_each_settings_item_background)
                Units.METRIC
            }
            binding.unitsRightLayout -> {
                setBackgroundResource(R.drawable.right_selected_each_settings_item_background)
                binding.unitsLeftLayout.setBackgroundResource(R.drawable.left_each_settings_item_background)
                Units.IMPERIAL
            }
            else -> throw RuntimeException("Layout Id is $id")
        })

    }
}

fun ConstraintLayout.doWindDirectionChange(viewModel: SettingsViewModel, binding: SettingsFragmentBinding) {
    val basicSettingsDataClass = viewModel.basicSettingsDataClass.value
    if (basicSettingsDataClass?.windDirectionUnits == WindDirectionUnits.CARDINAL) {
        if (this == binding.windLeftLayout) setBackgroundResource(R.drawable.left_selected_each_settings_item_background)
        else setBackgroundResource(R.drawable.right_each_settings_item_background)
    }
    else {
        if (this == binding.windRightLayout) setBackgroundResource(R.drawable.right_selected_each_settings_item_background)
        else setBackgroundResource(R.drawable.left_each_settings_item_background)
    }

    setOnClickListener {

        basicSettingsDataClass?.mainSettings?.editDataStore(when (this) {
            binding.windLeftLayout -> {
                setBackgroundResource(R.drawable.left_selected_each_settings_item_background)
                binding.windRightLayout.setBackgroundResource(R.drawable.right_each_settings_item_background)
                WindDirectionUnits.CARDINAL
            }
            binding.windRightLayout -> {
                setBackgroundResource(R.drawable.right_selected_each_settings_item_background)
                binding.windLeftLayout.setBackgroundResource(R.drawable.left_each_settings_item_background)
                WindDirectionUnits.DEGREES
            }
            else -> throw RuntimeException("Layout Id is $id")
        })

    }
}

fun ConstraintLayout.doTimeFormatChange(viewModel: SettingsViewModel, binding: SettingsFragmentBinding) {
    val basicSettingsDataClass = viewModel.basicSettingsDataClass.value
    if (basicSettingsDataClass?.timeFormat == TimeFormat.HALF_DAY) {
        if (this == binding.timeLeftLayout) setBackgroundResource(R.drawable.left_selected_each_settings_item_background)
        else setBackgroundResource(R.drawable.right_each_settings_item_background)
    }
    else {
        if (this == binding.timeRightLayout) setBackgroundResource(R.drawable.right_selected_each_settings_item_background)
        else setBackgroundResource(R.drawable.left_each_settings_item_background)
    }

    setOnClickListener {

        basicSettingsDataClass?.mainSettings?.editDataStore(when (this) {
           binding.timeLeftLayout -> {
                setBackgroundResource(R.drawable.left_selected_each_settings_item_background)
               binding.timeRightLayout.setBackgroundResource(R.drawable.right_each_settings_item_background)
                TimeFormat.HALF_DAY
            }
            binding.timeRightLayout -> {
                setBackgroundResource(R.drawable.right_selected_each_settings_item_background)
                binding.timeLeftLayout.setBackgroundResource(R.drawable.left_each_settings_item_background)
                TimeFormat.FULL_DAY
            }
            else -> throw RuntimeException("Layout Id is $id")
        })

    }
}

//@BindingAdapter("doDisplayModeChange")
fun ConstraintLayout.doDisplayModeChange(viewModel: SettingsViewModel, binding: SettingsFragmentBinding) {
    val basicSettingsDataClass = viewModel.basicSettingsDataClass.value
    if (basicSettingsDataClass?.windDirectionUnits == WindDirectionUnits.CARDINAL) {
        if (this == binding.displayLeftLayout) setBackgroundResource(R.drawable.left_selected_each_settings_item_background)
        else setBackgroundResource(R.drawable.right_each_settings_item_background)
    }
    else {
        if (this == binding.displayRightLayout) setBackgroundResource(R.drawable.right_selected_each_settings_item_background)
        else setBackgroundResource(R.drawable.left_each_settings_item_background)
    }

    setOnClickListener {

        basicSettingsDataClass?.mainSettings?.editDataStore(when (this) {
            binding.displayLeftLayout -> {
                setBackgroundResource(R.drawable.left_selected_each_settings_item_background)
                binding.displayRightLayout.setBackgroundResource(R.drawable.right_each_settings_item_background)
                DisplayMode.LIGHT
            }
            binding.displayRightLayout -> {
                setBackgroundResource(R.drawable.right_selected_each_settings_item_background)
                binding.displayLeftLayout.setBackgroundResource(R.drawable.left_each_settings_item_background)
                DisplayMode.DARK
            }
            else -> throw RuntimeException("Layout Id is $id")
        })

    }
}

fun TextView.doTextChange(isSelected: Boolean, isTextTitle: Boolean) {
    // isSelected should have a grey theme if true
    Timber.d("Is selected is $isSelected")

        setTextColor(
            if (isTextTitle) {
                if (isSelected) {
                    android.R.color.white
                } else {
                    android.R.color.black
                }
            } else {
                if (isSelected) {
                    R.color.light_grey
                } else {
                    R.color.color_black
                }
            }
        )
        Timber.d("color is $currentTextColor")
}