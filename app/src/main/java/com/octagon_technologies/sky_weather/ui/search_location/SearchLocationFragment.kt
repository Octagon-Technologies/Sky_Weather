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
import com.octagon_technologies.sky_weather.ui.find_location.FindLocationViewModel
import com.octagon_technologies.sky_weather.ui.find_location.FindLocationViewModelFactory
import com.octagon_technologies.sky_weather.ui.search_location.each_search_result_item.EachSearchResultItem
import com.octagon_technologies.sky_weather.utils.CustomTextWatcher
import com.octagon_technologies.sky_weather.utils.Theme
import com.octagon_technologies.sky_weather.utils.changeSystemNavigationBarColor
import com.octagon_technologies.sky_weather.widgets.WidgetConfigureActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.launch

class SearchLocationFragment : Fragment() {

    private val groupAdapter by lazy { GroupAdapter<GroupieViewHolder>() }
    private val theme by lazy { (activity as? MainActivity)?.liveTheme?.value ?: Theme.DARK }
    private val viewModel: SearchLocationViewModel by viewModels {
        SearchLocationViewModelFactory(requireContext())
    }
    private val findLocationViewModel: FindLocationViewModel by viewModels {
        FindLocationViewModelFactory(requireContext())
    }
    private val binding by lazy {
        SearchLocationFragmentBinding.inflate(layoutInflater).also {
            it.theme = theme
        }
    }
    private val liveLocation by lazy {
        (activity as? MainActivity)?.liveLocation
            ?: (activity as? WidgetConfigureActivity)?.viewModel?.reverseGeoCodingLocation
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
                            viewModel.mainDataBase, reverseGeoCodingLocation
                        )

                        // Add location to recent location
                        RecentLocationsRepo.insertRecentLocationToLocalStorage(
                            viewModel.mainDataBase, this@apply
                        )
                    }

                    (activity as? MainActivity)?.apply {
                        hasNotificationChanged = false
                        liveLocation.value = reverseGeoCodingLocation
                    } ?: run {
                        (activity as? WidgetConfigureActivity)?.viewModel?.reverseGeoCodingLocation?.value =
                            reverseGeoCodingLocation
                    }

                    (activity as? WidgetConfigureActivity)?.let {
                        findNavController().popBackStack()
                        it.viewModel.navigateToLocationFragment.value = false
                    } ?: run {
                        findNavController().popBackStack(R.id.currentForecastFragment, false)
                    }
                }
            }
        }

        findLocationViewModel.reversedGeoCodingLocation.observe(viewLifecycleOwner, {
            findLocationViewModel.addCurrentLocationToDatabase(activity)
            liveLocation?.value = it
            findNavController().popBackStack(R.id.currentForecastFragment, false)
        })

        return binding.root
    }


    val handler = object : CountDownTimer(1000, 500) {
        override fun onTick(millisUntilFinished: Long) {}

        override fun onFinish() {
            viewModel.getLocationSuggestions(binding.searchQuery.text.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        changeSystemNavigationBarColor(
            if (theme == Theme.LIGHT) android.R.color.white
            else R.color.dark_black
        )
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