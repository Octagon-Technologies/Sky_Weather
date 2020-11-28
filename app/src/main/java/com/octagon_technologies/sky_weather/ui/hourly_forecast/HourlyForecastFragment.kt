package com.octagon_technologies.sky_weather.ui.hourly_forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.octagon_technologies.sky_weather.MainActivity
import com.octagon_technologies.sky_weather.addToolbarAndBottomNav
import com.octagon_technologies.sky_weather.databinding.HourlyForecastFragmentBinding
import com.octagon_technologies.sky_weather.databinding.SelectedHourlyForecastLayoutBinding
import com.octagon_technologies.sky_weather.getCoordinates
import com.octagon_technologies.sky_weather.ui.hourly_forecast.each_hourly_forecast_item.EachDayTextItem
import com.octagon_technologies.sky_weather.ui.hourly_forecast.each_hourly_forecast_item.EachHourlyForecastItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.joda.time.DateTime
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class HourlyForecastFragment : Fragment() {

    private val viewModel: HourlyForecastViewModel by viewModels {
        HourlyForecastViewModelFactory(requireContext())
    }
    private lateinit var binding: HourlyForecastFragmentBinding
    private lateinit var selectedHourlyForecastLayout: SelectedHourlyForecastLayoutBinding
    private lateinit var bottomSheet: BottomSheetBehavior<View>

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()
    private val mainActivity by lazy { activity as MainActivity }

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

        (activity as MainActivity).liveLocation.observe(viewLifecycleOwner, {
            it?.also {
                viewModel.getHourlyForecastAsync(it, mainActivity.liveUnits.value)
            }
            Timber.d("Observed liveLocation is $it")
        })

        binding.hourlyRecyclerView.addCustomScrollListener(groupAdapter, binding)

        viewModel.selectedSingleForecast.observe(viewLifecycleOwner) {
            val hours = SimpleDateFormat("HH", Locale.ENGLISH)
                .format(DateTime(it?.observationTime?.value).toDate().time)?.toInt()

            viewModel.isDay.value = hours in 8..19
        }

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

            val coordinates = mainActivity.liveLocation.value?.getCoordinates()!!
            viewModel.getSelectedSingleForecast(
                eachHourlyForecastItem.eachHourlyForecast.observationTime,
                mainActivity.liveUnits.value,
                coordinates
            )
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }

    }

    override fun onStart() {
        super.onStart()
        mainActivity.liveTheme.observe(viewLifecycleOwner, {
            addToolbarAndBottomNav(it, bottomSheet.state != BottomSheetBehavior.STATE_EXPANDED)
        })
    }
}
