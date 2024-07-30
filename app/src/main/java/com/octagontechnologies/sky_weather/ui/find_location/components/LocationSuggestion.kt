package com.octagontechnologies.sky_weather.ui.find_location.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.octagontechnologies.sky_weather.domain.Location
import com.octagontechnologies.sky_weather.ui.compose.theme.LocalAppColors

@Composable
fun LocationSuggestion(
    onSelectLocation: (Location) -> Unit,
    location: Location,
    currentIndex: Int,
    lastIndex: Int,
    actionIcon: ImageVector,
    actionIconContentDescription: String? = null,
    onActionIcon: (Location) -> Unit,
) {
    Column(Modifier.padding(top = 10.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onSelectLocation(location) }) {
            Text(
                text = location.displayNameWithoutCountryCode,
                lineHeight = 19.sp,
                modifier = Modifier
                    .padding(start = 8.dp, end = 12.dp)
                    .weight(1f)
            )

            IconButton(modifier = Modifier
                .padding(end = 12.dp)
                .size(32.dp), onClick = {}) {
                Icon(
                    modifier = Modifier
                        .clickable { onActionIcon(location) }
                        .padding(2.dp),
                    imageVector = actionIcon,
                    contentDescription = actionIconContentDescription
                )
            }
        }

        if (currentIndex != lastIndex)
            HorizontalDivider(
                modifier = Modifier.padding(top = 8.dp),
                thickness = (0.125).dp,
                color = LocalAppColors.current.onSurfaceLighter
            )
    }
}