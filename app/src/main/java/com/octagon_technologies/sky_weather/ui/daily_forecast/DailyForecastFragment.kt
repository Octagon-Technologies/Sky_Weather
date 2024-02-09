package com.octagon_technologies.sky_weather.ui.daily_forecast

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.databinding.DailyForecastFragmentBinding
import com.octagon_technologies.sky_weather.main_activity.MainActivity
import com.octagon_technologies.sky_weather.ui.daily_forecast.daily_tab.DailyTabFragment
import com.octagon_technologies.sky_weather.ui.daily_forecast.each_daily_forecast_item.EachDailyForecastItem
import com.octagon_technologies.sky_weather.utils.StatusCode
import com.octagon_technologies.sky_weather.utils.Theme
import com.octagon_technologies.sky_weather.utils.Units
import com.octagon_technologies.sky_weather.utils.addToolbarAndBottomNav
import com.octagon_technologies.sky_weather.utils.changeSystemNavigationBarColor
import com.octagon_technologies.sky_weather.utils.getDayWithMonth
import com.octagon_technologies.sky_weather.utils.getFullMonth
import com.octagon_technologies.sky_weather.utils.getStringResource
import com.octagon_technologies.sky_weather.utils.showLongToast
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DailyForecastFragment : Fragment() {

    private val viewModel by viewModels<DailyForecastViewModel>()
    private lateinit var binding: DailyForecastFragmentBinding
    private val mainActivity by lazy { requireActivity() as MainActivity }

    private lateinit var bottomSheet: BottomSheetBehavior<View>
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DailyForecastFragmentBinding.inflate(layoutInflater)
        binding.selectedDailyForecastLayout.let {
            it.lifecycleOwner = viewLifecycleOwner
            it.theme = viewModel.theme
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dailyTempUnitText.text = if (viewModel.units.value == Units.METRIC) "°C" else "°F"

        setUpStatusCode()
        setUpDailyRecyclerView()
        setUpSelectedDailyForecast()
        setUpLiveData()
        setUpTabLayout()
        setUpBottomSheet()
    }

    private fun setUpSelectedDailyForecast() {
        viewModel.selectedDailyForecast.observe(viewLifecycleOwner) { selectedDailyForecast ->
            binding.selectedDailyForecastLayout.dailyDateText.text =
                selectedDailyForecast.timeInMillis.getDayWithMonth()
            binding.selectedDailyForecastLayout.tempUnitText.text =
                if (viewModel.units.value == Units.METRIC) "°C" else "°F"
        }
    }

    private fun setUpLiveData() {
        viewModel.listOfDailyForecast.observe(viewLifecycleOwner) { listOfDailyForecast ->
            val timeInMillis = listOfDailyForecast?.firstOrNull()?.timeInMillis
            binding.monthText.text = getFullMonth(timeInMillis)
        }
    }

    private fun setUpTabLayout() {
        val tabLayout = binding.selectedDailyForecastLayout.tabLayout.apply {
            tabTextColors = ColorStateList.valueOf(
                ResourcesCompat.getColor(
                    resources,
                    if (viewModel.theme.value == Theme.LIGHT) R.color.dark_black
                    else android.R.color.white,
                    null
                )
            )
        }

        val viewPager2 = binding.selectedDailyForecastLayout.layoutViewPager
        viewPager2.isSaveEnabled = false
        viewPager2.adapter = MyFragmentStateAdapter()


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

    private fun setUpBottomSheet() {
        bottomSheet = BottomSheetBehavior.from(binding.selectedDailyForecastLayout.root).also {
            it.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                Timber.d("newState is $newState in onStateChanged()")
                adjustNavViewBasedOnBottomSheetState(newState)
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun adjustNavViewBasedOnBottomSheetState(newState: Int) {
        if (newState == BottomSheetBehavior.STATE_EXPANDED ||
            newState == BottomSheetBehavior.STATE_HALF_EXPANDED ||
            newState == BottomSheetBehavior.STATE_DRAGGING
        ) {
            Timber.d("State is expanded.")
            mainActivity.binding.navView.visibility = View.GONE
            changeSystemNavigationBarColor(
                if (viewModel.theme.value == Theme.LIGHT) android.R.color.white
                else R.color.dark_black
            )
        } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
            Timber.d("State is collapsed.")
            mainActivity.binding.navView.visibility = View.VISIBLE
            changeSystemNavigationBarColor(
                if (viewModel.theme.value == Theme.LIGHT) R.color.light_theme_blue
                else R.color.dark_theme_blue
            )
        }
    }

    private fun setUpStatusCode() {
        viewModel.statusCode.observe(viewLifecycleOwner) {
            val message = when (it ?: return@observe) {
                StatusCode.Success -> return@observe
                StatusCode.NoNetwork -> getStringResource(R.string.no_network_availble_plain_text)
                StatusCode.ApiLimitExceeded -> getStringResource(R.string.api_limit_exceeded_plain_text)
            }

            showLongToast(message)
        }
    }

    private fun setUpDailyRecyclerView() {
        binding.dayRecyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.dayRecyclerview.adapter = groupAdapter

        viewModel.listOfDailyForecast.observe(viewLifecycleOwner) { listOfDailyForecast ->
            groupAdapter.update(
                listOfDailyForecast?.map { dailyForecast ->
                    EachDailyForecastItem(dailyForecast) {
                        viewModel.selectDailyForecast(it)
                        bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                } ?: listOf()
            )
        }
    }

    override fun onStart() {
        super.onStart()
        adjustNavViewBasedOnBottomSheetState(bottomSheet.state)

        viewModel.theme.observe(viewLifecycleOwner) {
            addToolbarAndBottomNav(it, true)
            changeSystemNavigationBarColor(
                if (it == Theme.LIGHT) R.color.light_theme_blue
                else R.color.dark_theme_blue
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity.binding.navView.visibility = View.VISIBLE
    }

    inner class MyFragmentStateAdapter : FragmentStateAdapter(this) {
        override fun getItemCount() = 2

        override fun createFragment(position: Int): Fragment {
            Timber.d("MyFragmentStateAdapter.position is $position")
            val dailyTabFragment = DailyTabFragment.getInstance(
                selectedTimePeriod = viewModel.selectedDailyForecast.map { if (position == 0) it.dayTime else it.nightTime },
            )
            Timber.d("dailyTabFragment.timePeriod in createFragment() is ${dailyTabFragment.timePeriod.value}")

            return dailyTabFragment
        }

        override fun getItemId(position: Int): Long {
            Timber.d("getItemId called")
            return super.getItemId(position)
        }


    }
}