package com.example.kotlinweatherapp.ui.hourly_forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweatherapp.databinding.HourlyForecastFragmentBinding
import com.example.kotlinweatherapp.ui.hourly_forecast.each_hourly_forecast_item.EachDayTextItem
import com.example.kotlinweatherapp.ui.hourly_forecast.each_hourly_forecast_item.EachHourlyForecastItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import timber.log.Timber

class HourlyForecastFragment : Fragment() {

    private lateinit var viewModel: HourlyForecastViewModel
    private lateinit var binding: HourlyForecastFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView called")
        binding = HourlyForecastFragmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(viewModelStore, HourlyForecastViewModelFactory(requireContext())).get(HourlyForecastViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.hourlyRecyclerView.addCustomScrollListener(binding)

        viewModel.loadData()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate called")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated called")
    }
}