package com.octagon_technologies.sky_weather.ui.search_location

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.octagon_technologies.sky_weather.MainActivity
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.databinding.SearchLocationFragmentBinding
import com.octagon_technologies.sky_weather.repository.LocationRepo
import com.octagon_technologies.sky_weather.repository.RecentLocationsRepo
import com.octagon_technologies.sky_weather.ui.search_location.each_search_result_item.EachSearchResultItem
import com.octagon_technologies.sky_weather.utils.*
import com.octagon_technologies.sky_weather.widgets.WidgetConfigureActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchLocationFragment : Fragment() {

    private val groupAdapter by lazy { GroupAdapter<GroupieViewHolder>() }
    private val theme by lazy { (activity as? MainActivity)?.liveTheme?.value ?: Theme.DARK }
    private val viewModel: SearchLocationViewModel by viewModels()
    private val binding by lazy {
        SearchLocationFragmentBinding.inflate(layoutInflater).also {
            it.theme = theme
        }
    }
    private val imm by lazy { requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.cancelBtn.setOnClickListener { findNavController().popBackStack() }
        binding.searchQuery.addTextChangedListener(object : CustomTextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                handler.cancel()
                handler.start()
            }
        })

        viewModel.locationSuggestions.observe(viewLifecycleOwner, {
            binding.searchLocationRecyclerview.addLocationSuggestionsToRecyclerView(
                theme,
                it,
                groupAdapter,
                viewModel.favouriteItemsMap.value!!,
                viewModel.addToFavourite
            )
        })

        groupAdapter.setOnItemClickListener { item, _ ->
            (item as EachSearchResultItem).location.apply {
                lifecycleScope.launch {
                    val reverseGeoCodingLocation = toReverseGeoCodingLocation()

                    // Set current location to general ReverseGeoCodingLocation if it was set in the MainActivity
                    (activity as? MainActivity)?.let {
                        LocationRepo.insertLocationToLocalStorage(
                            viewModel.weatherDataBase, reverseGeoCodingLocation
                        )

                        // Add location to recent location
                        RecentLocationsRepo.insertRecentLocationToLocalStorage(
                            viewModel.weatherDataBase, this@apply
                        )

                        it.hasNotificationChanged = false
                        it.liveLocation.value = reverseGeoCodingLocation

                        findNavController().popBackStack(R.id.currentForecastFragment, false)
                    } ?: run {
                        val widgetConfigureViewModel =
                            (activity as? WidgetConfigureActivity)?.viewModel?.also {
                                it.reverseGeoCodingLocation.value = reverseGeoCodingLocation
                            }

                        findNavController().popBackStack()
                        widgetConfigureViewModel?.navigateToLocationFragment?.value = false
                    }
                }
            }
        }

        return binding.root
    }


    val handler = object : CountDownTimer(1000, 500) {
        override fun onTick(millisUntilFinished: Long) {}

        override fun onFinish() {
            viewModel.getLocationSuggestions(binding.searchQuery.text.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        imm.showSoftInput(binding.searchQuery, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onPause() {
        super.onPause()
        imm.hideSoftInputFromWindow(binding.searchQuery.windowToken, 0)
    }
}