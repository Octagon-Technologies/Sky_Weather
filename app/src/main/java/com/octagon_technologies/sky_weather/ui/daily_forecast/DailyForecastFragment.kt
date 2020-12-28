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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import com.octagon_technologies.sky_weather.*
import com.octagon_technologies.sky_weather.databinding.DailyForecastFragmentBinding
import com.octagon_technologies.sky_weather.ui.daily_forecast.daily_tab.DailyTabFragment
import com.octagon_technologies.sky_weather.ui.daily_forecast.each_daily_forecast_item.EachDailyForecastItem
import com.octagon_technologies.sky_weather.utils.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DailyForecastFragment : Fragment() {

    private val viewModel by viewModels<DailyForecastViewModel>()
    private lateinit var binding: DailyForecastFragmentBinding

    private lateinit var bottomSheet: BottomSheetBehavior<View>
    private val mainActivity by lazy { activity as MainActivity }
    private val theme by lazy { mainActivity.liveTheme }
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DailyForecastFragmentBinding.inflate(layoutInflater).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.groupAdapter = groupAdapter
            it.dailyForecastViewModel = viewModel
            it.units = mainActivity.liveUnits.value
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
        bottomSheet = BottomSheetBehavior.from(binding.selectedDailyForecastLayout.root).also {
            it.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        viewModel.statusCode.observe(viewLifecycleOwner) {
            val message = when(it ?: return@observe) {
                StatusCode.Success -> return@observe
                StatusCode.NoNetwork -> getStringResource(R.string.no_network_availble_plain_text)
                StatusCode.ApiLimitExceeded -> getStringResource(R.string.api_limit_exceeded_plain_text)
            }

            showLongToast(message)
        }

        bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                Timber.d("newState is $newState in onStateChanged()")

                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    Timber.d("State is expanded.")
                    mainActivity.binding.navView.visibility = View.GONE
                    changeSystemNavigationBarColor(
                        if (theme.value == Theme.LIGHT) android.R.color.white
                        else R.color.dark_black
                    )
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    Timber.d("State is collapsed.")
                    mainActivity.binding.navView.visibility = View.VISIBLE
                    changeSystemNavigationBarColor(
                        if (theme.value == Theme.LIGHT) R.color.light_theme_blue
                        else R.color.dark_theme_blue
                    )
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

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

        groupAdapter.setOnItemClickListener { item, _ ->
            val coordinates = mainActivity.liveLocation.value?.getCoordinates() ?: return@setOnItemClickListener

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

        mainActivity.liveTheme.observe(viewLifecycleOwner) {
            addToolbarAndBottomNav(it, true)
            changeSystemNavigationBarColor(
                if (it == Theme.LIGHT) R.color.light_theme_blue
                else R.color.dark_theme_blue
            )
        }
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
    }
}