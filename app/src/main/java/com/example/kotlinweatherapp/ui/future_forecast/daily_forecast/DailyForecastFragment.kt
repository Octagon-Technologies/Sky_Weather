package com.example.kotlinweatherapp.ui.future_forecast.daily_forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinweatherapp.databinding.DailyForecastFragmentBinding
import timber.log.Timber

class DailyForecastFragment : Fragment() {

    companion object {
        fun newInstance() = DailyForecastFragment()
    }

    private lateinit var viewModel: DailyForecastViewModel
    private lateinit var binding: DailyForecastFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView called")
        binding = DailyForecastFragmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(DailyForecastViewModel::class.java)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("OnViewCreated called")
    }

}