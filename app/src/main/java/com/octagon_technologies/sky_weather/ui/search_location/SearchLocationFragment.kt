package com.octagon_technologies.sky_weather.ui.search_location

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.octagon_technologies.sky_weather.main_activity.MainActivity
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.databinding.SearchLocationFragmentBinding
import com.octagon_technologies.sky_weather.ui.search_location.each_search_result_item.EachSearchResultItem
import com.octagon_technologies.sky_weather.utils.CustomTextWatcher
import com.octagon_technologies.sky_weather.utils.Theme
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchLocationFragment : Fragment(R.layout.search_location_fragment) {

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()
    private val viewModel: SearchLocationViewModel by viewModels()
    private lateinit var binding: SearchLocationFragmentBinding

    private val imm by lazy { requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SearchLocationFragmentBinding.bind(view)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        setUpLocationSuggestionsRecyclerView()

        binding.cancelBtn.setOnClickListener { findNavController().popBackStack() }
        binding.searchQuery.addTextChangedListener(object : CustomTextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                handler.cancel()
                handler.start()
            }
        })


        groupAdapter.setOnItemClickListener { item, _ ->
            val location = (item as EachSearchResultItem).location

            // Change the current database location if it was set in the MainActivity; this prevents widget creation
            // from being mistaken as user location setting
            val mainActivity = (activity as? MainActivity)

            if (mainActivity != null) {
                viewModel.selectLocation(location)
//                mainActivity.hasNotificationChanged = false

                findNavController().popBackStack(R.id.currentForecastFragment, true)
            }

//            val widgetConfigureActivity = (activity as? WidgetConfigureActivity)
//            else if (widgetConfigureActivity != null) {
//                        val widgetConfigureViewModel = widgetConfigureActivity.viewModel.also {
//                                it.reverseGeoCodingLocation.value = location
//                            }
//
//                        findNavController().popBackStack()
//                        widgetConfigureViewModel?.navigateToLocationFragment?.value = false
//                    }
//                }

        }
    }

    private fun setUpLocationSuggestionsRecyclerView() {
        binding.searchLocationRecyclerview.adapter = groupAdapter

        viewModel.searchLocationSuggestions.observe(viewLifecycleOwner) { suggestions ->
            groupAdapter.update(
                suggestions.map { location ->
                    EachSearchResultItem(
                        theme = viewModel.theme,
                        isLikedByUser = viewModel.listOfFavouriteLocation.value?.contains(location) == true,
                        location = location,
                        onChangeFavoriteStatus = { isLikedByUser -> viewModel.changeFavoriteStatus(isLikedByUser, location) }
                    )
                }
            )
        }
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