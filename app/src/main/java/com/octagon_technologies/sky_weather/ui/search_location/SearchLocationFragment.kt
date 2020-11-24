package com.octagon_technologies.sky_weather.ui.search_location

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.octagon_technologies.sky_weather.CustomTextWatcher
import com.octagon_technologies.sky_weather.MainActivity
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.Theme
import com.octagon_technologies.sky_weather.databinding.SearchLocationFragmentBinding
import com.octagon_technologies.sky_weather.ui.find_location.FindLocationViewModel
import com.octagon_technologies.sky_weather.ui.find_location.FindLocationViewModelFactory
import com.octagon_technologies.sky_weather.ui.search_location.each_search_result_item.EachSearchResultItem
import com.octagon_technologies.sky_weather.ui.shared_code.MainFavouriteLocationsObject
import com.octagon_technologies.sky_weather.ui.shared_code.MainLocationObject
import com.octagon_technologies.sky_weather.ui.shared_code.MainRecentLocationsObject
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchLocationFragment : Fragment() {

    private val groupAdapter by lazy { GroupAdapter<GroupieViewHolder>() }
    private val theme by lazy { (activity as MainActivity).liveTheme.value }
    private val viewModel: SearchLocationViewModel by viewModels { SearchLocationViewModelFactory(requireContext()) }
    private val findLocationViewModel: FindLocationViewModel by viewModels { FindLocationViewModelFactory(requireContext()) }
    private val binding by lazy {
        SearchLocationFragmentBinding.inflate(layoutInflater).also {
            it.theme = theme
        }
    }
    private val imm by lazy { requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        imm.showSoftInput(binding.searchQuery, InputMethodManager.SHOW_IMPLICIT)
        binding.cancelBtn.setOnClickListener { findNavController().popBackStack() }
        binding.searchQuery.addTextChangedListener(object : CustomTextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                handler.cancel()
                handler.start()
            }
        })

        viewModel.locationSuggestions.observe(viewLifecycleOwner, {
            binding.searchLocationRecyclerview.addLocationSuggestionsToRecyclerView(theme, it, groupAdapter, viewModel.favouriteItemsMap.value!!, viewModel.addToFavourite)
        })

        groupAdapter.setOnItemClickListener { item, _ ->
            (item as EachSearchResultItem).locationItem.apply {

                val reverseGeoCodingLocation = toReverseGeoCodingLocation()
                CoroutineScope(Dispatchers.Main).launch {
                    // Set current location to general ReverseGeoCodingLocation
                    MainLocationObject.insertLocationToLocalStorage(
                        viewModel.mainDataBase, reverseGeoCodingLocation
                    )

                    // Add location to recent location
                    MainRecentLocationsObject.insertRecentLocationToLocalStorage(
                        viewModel.mainDataBase, this@apply
                    )
                }

                (activity as MainActivity).liveLocation.value = reverseGeoCodingLocation
                findNavController().popBackStack(R.id.currentForecastFragment, false)
            }
        }

        findLocationViewModel.reversedGeoCodingLocation.observe(viewLifecycleOwner, {
            findLocationViewModel.addCurrentLocationToDatabase(activity)
            (activity as MainActivity).liveLocation.value = it
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

    override fun onStop() {
        super.onStop()
        imm.hideSoftInputFromWindow(binding.searchQuery.windowToken, 0)
    }
}