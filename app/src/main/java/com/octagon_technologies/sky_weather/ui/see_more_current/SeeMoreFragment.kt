package com.octagon_technologies.sky_weather.ui.see_more_current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.octagon_technologies.sky_weather.MainActivity
import com.octagon_technologies.sky_weather.R
import com.octagon_technologies.sky_weather.databinding.SeeMoreFragmentBinding
import com.octagon_technologies.sky_weather.repository.network.single_forecast.SingleForecast
import com.octagon_technologies.sky_weather.utils.Theme
import com.octagon_technologies.sky_weather.utils.removeToolbarAndBottomNav
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class SeeMoreFragment : Fragment() {

    private lateinit var binding: SeeMoreFragmentBinding
    private lateinit var singleForecast: SingleForecast
    val groupAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SeeMoreFragmentBinding.inflate(inflater)
        (activity as MainActivity).singleForecastJsonAdapter
            .fromJson(SeeMoreFragmentArgs.fromBundle(requireArguments()).singleForecastJson)?.let {
                singleForecast = it
            } ?: findNavController().popBackStack()

        binding.singleForecast = singleForecast
        binding.groupAdapter = groupAdapter
        binding.units = (activity as MainActivity).liveUnits.value

        binding.backBtn.setOnClickListener { findNavController().popBackStack() }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val theme = (activity as MainActivity).liveTheme.value
        removeToolbarAndBottomNav(
            if (theme == Theme.LIGHT) R.color.light_theme_blue else R.color.dark_theme_blue,
            true
        )
    }

}