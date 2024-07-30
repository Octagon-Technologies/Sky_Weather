package com.octagontechnologies.sky_weather.ui.find_location.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.octagontechnologies.sky_weather.R
import com.octagontechnologies.sky_weather.domain.Location
import com.octagontechnologies.sky_weather.ui.compose.theme.LocalAppColors
import com.octagontechnologies.sky_weather.ui.compose.theme.QuickSand
import com.octagontechnologies.sky_weather.utils.Resource

@Composable
fun SearchTab(
    query: String,
    onSearchQueryChanged: (String) -> Unit,
    suggestions: Resource<List<Location>>,
    favouriteLocations: List<Location>,
    modifier: Modifier = Modifier,

    selectLocation: (Location) -> Unit,
    addOrRemoveFromFavourite: (Location) -> Unit,
    hideSearchTab: () -> Unit
) {
    BackHandler {
        hideSearchTab()
    }


    Box(
        modifier
            .fillMaxSize()
            .clickable { hideSearchTab() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.85f)
                .align(Alignment.Center)
                .clickable {  },
            colors = CardDefaults.cardColors(
                containerColor = LocalAppColors.current.surface,
                contentColor = LocalAppColors.current.onSurface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
                TextField(
                    value = query,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = LocalAppColors.current.surfaceVariant,
                        focusedContainerColor = LocalAppColors.current.surfaceVariant,

                        unfocusedTextColor = LocalAppColors.current.onSurface,
                        focusedTextColor = LocalAppColors.current.onSurface,

//                        unfocusedPlaceholderColor = LocalAppColors.current.onSurface.copy(alpha = 0.75f),

                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = LocalAppColors.current.onSurface,
//                        focusedIndicatorColor = LocalAppColors.current.onSurface,
//                        unfocusedIndicatorColor = LocalAppColors.current.onSurface
                    ),
                    onValueChange = onSearchQueryChanged,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.search_your_location),
                            style = MaterialTheme.typography.bodyMedium,
                            color = LocalAppColors.current.onSurface.copy(alpha = 0.9f)
                        )
                    }
                )

                when (suggestions) {
                    is Resource.Success -> {
                        LazyColumn(
                            Modifier
                                .padding(top = 8.dp, bottom = 10.dp)
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            itemsIndexed(suggestions.data!!) { currentIndex, location ->
                                val isFavourite = location in favouriteLocations

                                LocationSuggestion(
                                    location = location,
                                    onSelectLocation = selectLocation,
                                    actionIcon = if (isFavourite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                                    actionIconContentDescription = stringResource(R.string.remove_from_favourites),
                                    onActionIcon = { addOrRemoveFromFavourite(location) },
                                    currentIndex = currentIndex,
                                    lastIndex = suggestions.data.lastIndex
                                )
                            }
                        }
                    }

                    is Resource.Loading -> {
                        Box(Modifier.fillMaxSize()) {
                            CircularProgressIndicator(
                                Modifier
                                    .size(32.dp)
                                    .align(Alignment.Center),
                                color = LocalAppColors.current.onSurface
                            )
                        }
                    }

                    is Resource.Error -> {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .padding(horizontal = 24.dp), contentAlignment = Alignment.Center
                        ) {
                            val errorMessage = suggestions.resMessage

                            Text(
                                text = stringResource(id = errorMessage),
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 18.sp,
                                color = LocalAppColors.current.onSurface,
                                fontFamily = QuickSand,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}