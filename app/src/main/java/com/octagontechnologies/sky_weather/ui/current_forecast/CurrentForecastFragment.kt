package com.octagontechnologies.sky_weather.ui.current_forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.octagontechnologies.sky_weather.databinding.CurrentForecastFragmentBinding
import com.octagontechnologies.sky_weather.domain.getFormattedFeelsLike
import com.octagontechnologies.sky_weather.domain.getFormattedTemp
import com.octagontechnologies.sky_weather.main_activity.MainActivity
import com.octagontechnologies.sky_weather.utils.addToolbarAndBottomNav
import com.octagontechnologies.sky_weather.utils.loadWeatherIcon
import com.octagontechnologies.sky_weather.utils.setUpToastMessage
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CurrentForecastFragment : Fragment() {

    private lateinit var binding: CurrentForecastFragmentBinding
    private val viewModel by viewModels<CurrentForecastViewModel>()

    private val mainActivity by lazy { requireActivity() as MainActivity }
//    private val adHelper by adHelpers()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CurrentForecastFragmentBinding.inflate(layoutInflater).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpStatusCode()
        setUpSwipeToRefresh()
        setUpHourlyForecastPreview()
        setUpSeeMoreButton()
        setUpLiveData()
        setUpAd()
    }

    private fun setUpAd() {
        viewModel.adRepo.getNativeAd(
            fragment = this,
            nativeAdBinding = binding.currentNativeAdLayout,
            onAdCompleted = { isSuccess ->
                Timber.d("isSuccess is $isSuccess")

                binding.currentNativeAdLayout.root.visibility =
                    if (isSuccess) View.VISIBLE else View.GONE
            })
    }

    private fun setUpLiveData() {
        viewModel.currentForecast.observe(viewLifecycleOwner) { currentForecast ->
            if (currentForecast != null) {
                binding.mainTemp.text = currentForecast.getFormattedTemp(viewModel.isImperial())
                binding.mainRealfeelDisplayText.text = currentForecast.getFormattedFeelsLike(viewModel.isImperial())
                binding.mainWeatherImage.loadWeatherIcon(currentForecast.timeInEpochMillis, currentForecast.weatherCode)
            }
        }
    }

    private fun setUpSeeMoreButton() {
        binding.seeMoreBtn.setOnClickListener {
            val currentForecast = viewModel.currentForecast.value
            if (currentForecast == null) {
                Toast.makeText(requireContext(), "No weather data available", Toast.LENGTH_SHORT)
                    .show()
            } else {
                findNavController()
                    .navigate(
                        CurrentForecastFragmentDirections
                            .actionCurrentForecastFragmentToSeeMoreFragment(currentForecast)
                    )
            }
        }
    }

    private fun setUpSwipeToRefresh() {
        binding.swipeToRefreshLayout.setOnRefreshListener { viewModel.refreshWeatherForecast() }

        viewModel.isRefreshing.observe(viewLifecycleOwner) { isRefreshing ->
            binding.swipeToRefreshLayout.isRefreshing = isRefreshing == true
        }
    }

    private fun setUpHourlyForecastPreview() {
        viewModel.oneHourForecast.observe(viewLifecycleOwner) { oneHourForecast ->
            binding.oneHourTempText.text = oneHourForecast.getFormattedTemp(viewModel.isImperial())
            binding.oneHourFeelslikeDisplayText.text = oneHourForecast.getFormattedFeelsLike(viewModel.isImperial())
            binding.oneHourWeatherImage.loadWeatherIcon(oneHourForecast?.timeInEpochMillis, oneHourForecast?.weatherCode)
        }
        viewModel.sixHourForecast.observe(viewLifecycleOwner) { sixHourForecast ->
            binding.sixHourTempText.text = sixHourForecast.getFormattedTemp(viewModel.isImperial())
            binding.sixHourFeelslikeDisplayText.text = sixHourForecast.getFormattedFeelsLike(viewModel.isImperial())
            binding.sixHourWeatherImage.loadWeatherIcon(sixHourForecast?.timeInEpochMillis, sixHourForecast?.weatherCode)
        }
        viewModel.twentyFourHourForecast.observe(viewLifecycleOwner) { twentyFourHourForecast ->
            binding.twentyFourTempText.text = twentyFourHourForecast.getFormattedTemp(viewModel.isImperial())
            binding.twentyFourFeelslikeDisplayText.text =
                twentyFourHourForecast.getFormattedFeelsLike(viewModel.isImperial())
            binding.twentyFourWeatherImage.loadWeatherIcon(twentyFourHourForecast?.timeInEpochMillis, twentyFourHourForecast?.weatherCode)
        }
    }

    private fun setUpStatusCode() {
        viewModel.statusCode.setUpToastMessage(this) {
            viewModel.onStatusCodeDisplayed()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.theme.observe(viewLifecycleOwner) {
            addToolbarAndBottomNav(it)
//
            // In Selected Hourly and Daily forecast, the navView is usually removed
            mainActivity.binding.navView.visibility = View.VISIBLE
        }
//            changeSystemNavigationBarColor(
//                if (it == Theme.LIGHT) R.color.light_theme_blue
//                else R.color.dark_theme_blue
//            )
//        }
    }
}