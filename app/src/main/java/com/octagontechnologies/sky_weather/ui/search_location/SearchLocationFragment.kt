package com.octagontechnologies.sky_weather.ui.search_location

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.octagontechnologies.sky_weather.main_activity.MainActivity
import com.octagontechnologies.sky_weather.R
import com.octagontechnologies.sky_weather.databinding.SearchLocationFragmentBinding
import com.octagontechnologies.sky_weather.domain.Location
import com.octagontechnologies.sky_weather.ui.search_location.each_search_result_item.EachSearchResultItem
import com.octagontechnologies.sky_weather.utils.CustomTextWatcher
import com.octagontechnologies.sky_weather.utils.setUpToastMessage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SearchLocationFragment : Fragment(R.layout.search_location_fragment) {

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()
    private val viewModel: SearchLocationViewModel by viewModels()
    private lateinit var binding: SearchLocationFragmentBinding

    private val imm by lazy { requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SearchLocationFragmentBinding.bind(view)

        setUpStatusCode()
        setUpLoadingSpinner()
        setUpLocationSuggestionsRecyclerView()

        viewModel.navigateHome.observe(viewLifecycleOwner) { navigateHome ->
            if (navigateHome == true) {
                findNavController().navigate(
                    SearchLocationFragmentDirections.actionSearchLocationFragmentToCurrentForecastFragment()
                )
                viewModel.onNavigateHomeDone()
            }
        }

        binding.cancelBtn.setOnClickListener { findNavController().popBackStack() }
        binding.searchQuery.addTextChangedListener(object : CustomTextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                handler.cancel()
                handler.start()
            }
        })

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

    private fun setUpLoadingSpinner() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.searchLocationRecyclerview.visibility =
                if (isLoading) View.GONE else View.VISIBLE
            binding.loadingProgressBar.visibility =
                if (isLoading) View.VISIBLE else View.GONE
        }
    }

    // TODO: Removed widget code, but we'll need this in future if we aere to implement it
    // Change the current database location if it was set in the MainActivity; this prevents widget creation
    // from being mistaken as user location setting
    private fun Location.onLocationSelected() {
        val mainActivity = (activity as? MainActivity)

        if (mainActivity != null) {
            viewModel.selectLocation(this)
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
                        onLocationSelected = { location.onLocationSelected() },
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

    private fun setUpStatusCode() {
        viewModel.statusCode.setUpToastMessage(this) {
            viewModel.onStatusCodeDisplayed()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.searchQuery.requestFocus()

        val isShowInput = imm.showSoftInput(binding.searchQuery, InputMethodManager.SHOW_IMPLICIT)
        Timber.d("isShowInput is $isShowInput")
    }

    override fun onStop() {
        super.onStop()
        imm.hideSoftInputFromWindow(binding.searchQuery.windowToken, 0)
    }
}