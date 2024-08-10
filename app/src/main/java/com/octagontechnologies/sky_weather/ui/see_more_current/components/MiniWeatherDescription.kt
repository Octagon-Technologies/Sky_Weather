package com.octagontechnologies.sky_weather.ui.see_more_current.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.octagontechnologies.sky_weather.ui.compose.theme.QuickSand

@Composable
fun MiniWeatherDescription(title: String, value: String, cardColor: Color, onCardColor: Color, modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(cardColor)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Medium,
            fontFamily = QuickSand,
            color = onCardColor,
            fontSize = 16.sp
        )

        Text(
            text = value,
            fontWeight = FontWeight.SemiBold,
            fontFamily = QuickSand,
            fontSize = 16.sp,
            color = onCardColor
        )
    }
}