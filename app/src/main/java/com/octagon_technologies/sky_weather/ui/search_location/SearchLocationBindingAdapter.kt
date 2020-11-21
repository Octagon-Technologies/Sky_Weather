package com.octagon_technologies.sky_weather.ui.search_location

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.octagon_technologies.sky_weather.Theme
import com.octagon_technologies.sky_weather.database.MainDataBase
import com.octagon_technologies.sky_weather.network.location.LocationItem
import com.octagon_technologies.sky_weather.ui.search_location.each_search_result_item.EachSearchResultItem
import com.octagon_technologies.sky_weather.ui.shared_code.MainFavouriteLocationsObject
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

fun RecyclerView.addLocationSuggestionsToRecyclerView(
    theme: Theme?,
    mapOfLocationItems: Map<String?, LocationItem>,
    groupAdapter: GroupAdapter<GroupieViewHolder>,
    favouriteItemsMap: Map<String?, LocationItem>,
    addToFavourite: (EachSearchResultItem) -> Job
) {
    adapter = groupAdapter
    layoutManager = LinearLayoutManager(context)

    groupAdapter.clear()

        mapOfLocationItems.values.sortedBy { it.displayName }.forEach {
            val eachSearchResultItem = EachSearchResultItem(
                theme = theme,
                isLikedByUser = it in favouriteItemsMap.values,
                locationItem = it,
                lambda = addToFavourite
            )
            groupAdapter.add(eachSearchResultItem)
        }
}
