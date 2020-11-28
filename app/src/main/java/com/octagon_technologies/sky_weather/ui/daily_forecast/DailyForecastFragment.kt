package com.octagon_technologies.sky_weather.ui.daily_forecast

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import com.octagon_technologies.sky_weather.*
import com.octagon_technologies.sky_weather.databinding.DailyForecastFragmentBinding
import com.octagon_technologies.sky_weather.ui.daily_forecast.daily_layout.DailyTabFragment
import com.octagon_technologies.sky_weather.ui.daily_forecast.each_daily_forecast_item.EachDailyForecastItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import timber.log.Timber

class DailyForecastFragment : Fragment() {

    private val viewModel by viewModels<DailyForecastViewModel> {
        DailyForecastViewModelFactory(requireContext())
    }

    private lateinit var binding: DailyForecastFragmentBinding
    private val bottomSheet by lazy { BottomSheetBehavior.from<View>(binding.selectedDailyForecastLayout.root) }
    private val mainActivity by lazy { activity as MainActivity }
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DailyForecastFragmentBinding.inflate(layoutInflater).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.groupAdapter = groupAdapter
            it.dailyForecastViewModel = viewModel
        }

        binding.selectedDailyForecastLayout.let {
            it.lifecycleOwner = viewLifecycleOwner
            it.dailyForecastViewModel = viewModel
            it.theme = mainActivity.liveTheme
            it.timeFormat = mainActivity.liveTimeFormat
            it.units = mainActivity.liveUnits
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED

        val tabLayout = binding.selectedDailyForecastLayout.tabLayout.apply {
            tabTextColors = ColorStateList.valueOf(
                ResourcesCompat.getColor(
                    resources,
                    if (mainActivity.liveTheme.value == Theme.LIGHT) R.color.dark_black
                    else android.R.color.white,
                    null
                )
            )
        }

        val viewPager2 = binding.selectedDailyForecastLayout.layoutViewPager
        viewPager2.adapter = MyFragmentStateAdapter()
        viewPager2.isSaveEnabled = false

        viewModel.getDailyForecastAsync(
            mainActivity.liveLocation.value,
            mainActivity.liveUnits.value
        )

        bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    Timber.d("State is expanded.")
                    mainActivity.binding.navView.visibility = View.GONE
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    Timber.d("State is collapsed.")
                    mainActivity.binding.navView.visibility = View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        groupAdapter.setOnItemClickListener { item, _ ->
            val coordinates = mainActivity.liveLocation.value?.getCoordinates()!!

            viewModel.getSelectedDailyForecast(
                coordinates,
                (item as EachDailyForecastItem).eachDailyForecast,
                mainActivity.liveUnits.value
            )
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }

        val tabConfigurationStrategy =
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = if (position == 0) "Day" else "Night"
            }

        TabLayoutMediator(
            tabLayout,
            viewPager2,
            tabConfigurationStrategy
        ).attach()
    }

    override fun onStart() {
        super.onStart()
        mainActivity.liveTheme.observe(viewLifecycleOwner, {
            addToolbarAndBottomNav(it, bottomSheet.state != BottomSheetBehavior.STATE_EXPANDED)
        })
    }

    inner class MyFragmentStateAdapter : FragmentStateAdapter(this) {
        override fun getItemCount() = 2

        override fun createFragment(position: Int): Fragment {
            Timber.d("MyFragmentStateAdapter.position is $position")
            return DailyTabFragment.getInstance(
                constructorIsDay = position == 0,
                mainActivity = mainActivity,
                constructorDailyForecastViewModel = viewModel
            )
        }

        override fun onBindViewHolder(
            holder: FragmentViewHolder,
            position: Int,
            payloads: MutableList<Any>
        ) {
            super.onBindViewHolder(holder, position, payloads)
        }
    }
}

/*
[{"temp":[{"observation_time":"2020-11-18T15:00:00Z","min":{"value":11.64,"units":"C"}},
{"observation_time":"2020-11-17T23:00:00Z","max":{"value":24.8,"units":"C"}}],
"humidity":[{"observation_time":"2020-11-17T23:00:00Z","min":{"value":27.85,"units":"%"}},
{"observation_time":"2020-11-18T08:00:00Z","max":{"value":84.84,"units":"%"}}],"weather_code":{"value":"fog"},
"observation_time":{"value":"2020-11-17"},"lat":34.08687645,"lon":-118.29337729},
 */