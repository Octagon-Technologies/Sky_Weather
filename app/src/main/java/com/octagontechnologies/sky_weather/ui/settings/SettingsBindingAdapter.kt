package com.octagontechnologies.sky_weather.ui.settings

import android.content.res.ColorStateList
import android.util.TypedValue
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.octagontechnologies.sky_weather.R
import timber.log.Timber


@BindingAdapter("toggleLeftSettingsBackground")
fun ConstraintLayout.toggleLeftSettingsBackground(isSelected: Boolean) {
    setBackgroundResource(
        if (isSelected) R.drawable.left_selected_each_settings_item_background
        else R.drawable.left_each_settings_item_background
    )
}

@BindingAdapter("toggleRightSettingsBackground")
fun ConstraintLayout.toggleRightSettingsBackground(isSelected: Boolean) {
    setBackgroundResource(
        if (isSelected) R.drawable.right_selected_each_settings_item_background
        else R.drawable.right_each_settings_item_background
    )
}

@BindingAdapter("toggleSettingsTextColor")
fun TextView.toggleSettingsTextColor(isSelected: Boolean) {
    val value = TypedValue()
    context.theme.resolveAttribute(
        if (isSelected) R.attr.colorOnSurface
        else R.attr.colorOnTertiaryFixed,
        value,
        true
    )

    setTextColor(ColorStateList.valueOf(value.data))
}

@BindingAdapter("toggleSettingsMiniTextColor")
fun TextView.toggleSettingsMiniTextColor(isSelected: Boolean) {
    val value = TypedValue()
    context.theme.resolveAttribute(
        if (isSelected) R.attr.colorOnSurfaceVariant
        else R.attr.colorOnTertiaryFixedVariant,
        value,
        true
    )
    setTextColor(
        ColorStateList.valueOf(value.data)
    )
}