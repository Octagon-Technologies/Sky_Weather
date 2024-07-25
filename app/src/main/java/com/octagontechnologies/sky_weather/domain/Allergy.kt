package com.octagontechnologies.sky_weather.domain

import androidx.annotation.DrawableRes

data class Allergy(
    val grassPollen: MiniAllergy?,
    val treePollen: MiniAllergy?,
    val weedPollen: MiniAllergy?
)

data class MiniAllergy (
    val type: String,
    val level: String,
    @DrawableRes val iconRes: Int
)