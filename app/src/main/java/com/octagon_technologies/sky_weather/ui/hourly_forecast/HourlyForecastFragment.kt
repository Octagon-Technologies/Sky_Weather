package com.octagon_technologies.sky_weather.ui.hourly_forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.octagon_technologies.sky_weather.*
import com.octagon_technologies.sky_weather.databinding.HourlyForecastFragmentBinding
import com.octagon_technologies.sky_weather.databinding.SelectedHourlyForecastLayoutBinding
import com.octagon_technologies.sky_weather.lazy.adHelpers
import com.octagon_technologies.sky_weather.ui.hourly_forecast.each_hourly_forecast_item.EachDayTextItem
import com.octagon_technologies.sky_weather.ui.hourly_forecast.each_hourly_forecast_item.EachHourlyForecastItem
import com.octagon_technologies.sky_weather.utils.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class HourlyForecastFragment : Fragment() {

    private val viewModel: HourlyForecastViewModel by viewModels()
    private lateinit var binding: HourlyForecastFragmentBinding
    private lateinit var selectedHourlyForecastLayout: SelectedHourlyForecastLayoutBinding
    private lateinit var bottomSheet: BottomSheetBehavior<View>

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()
    private val mainActivity by lazy { activity as MainActivity }
    private val adHelper by adHelpers()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HourlyForecastFragmentBinding.inflate(layoutInflater).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.groupAdapter = groupAdapter
            it.timeFormat = mainActivity.liveTimeFormat
            it.units = mainActivity.liveUnits

            it.selectedHourlyForecastLayout.selectedHourAdView.advertisementPlainText
                .setTextColor(mainActivity.liveTheme.getWhiteOrBlackTextColor(context))
        }

        selectedHourlyForecastLayout = binding.selectedHourlyForecastLayout.also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.theme = mainActivity.liveTheme
            it.units = mainActivity.liveUnits
            it.timeFormat = mainActivity.liveTimeFormat
            it.windDirectionUnits = mainActivity.liveWindDirectionUnits
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheet = BottomSheetBehavior.from<View>(selectedHourlyForecastLayout.root).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        viewModel.statusCode.observe(viewLifecycleOwner) {
            val message = when (it ?: return@observe) {
                StatusCode.Success -> return@observe
                StatusCode.NoNetwork -> getStringResource(R.string.no_network_availble_plain_text)
                StatusCode.ApiLimitExceeded -> getStringResource(R.string.api_limit_exceeded_plain_text)
            }

            showLongToast(message)
        }

        (activity as MainActivity).liveLocation.observe(viewLifecycleOwner, {
            it?.also {
                viewModel.getHourlyForecastAsync(it, mainActivity.liveUnits.value)
            }
            Timber.d("Observed liveLocation is $it")
        })

        binding.hourlyRecyclerView.addCustomScrollListener(groupAdapter, binding)

        bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    mainActivity.binding.navView.visibility = View.GONE
                    Timber.d("State changed to $newState")
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    mainActivity.binding.navView.visibility = View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        groupAdapter.setOnItemClickListener { item, _ ->
            if (item is EachDayTextItem) return@setOnItemClickListener
            val eachHourlyForecastItem = item as EachHourlyForecastItem

            val coordinates =
                mainActivity.liveLocation.value?.getCoordinates() ?: return@setOnItemClickListener
            viewModel.getSelectedSingleForecast(
                eachHourlyForecastItem.eachHourlyForecast.observationTime,
                mainActivity.liveUnits.value,
                coordinates
            )
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }

        adHelper.loadAd(binding.selectedHourlyForecastLayout.selectedHourAdView) {
            Timber.d("Load ad listener return $it")
            // If it failed in onAdFailedToLoad(), hide the ad view
            if (!it) {
                binding.selectedHourlyForecastLayout.selectedHourAdView.root.visibility = View.GONE
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mainActivity.liveTheme.observe(viewLifecycleOwner, {
            addToolbarAndBottomNav(it, bottomSheet.state != BottomSheetBehavior.STATE_EXPANDED)
            changeSystemNavigationBarColor(
                if (it == Theme.LIGHT) R.color.light_theme_blue
                else R.color.dark_theme_blue
            )
        })
    }
}
