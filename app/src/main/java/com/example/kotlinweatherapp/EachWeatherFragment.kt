package com.example.kotlinweatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinweatherapp.database.DataBase
import com.example.kotlinweatherapp.databinding.EachWeatherFragmentBinding

class EachWeatherFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: EachWeatherFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.each_weather_fragment, container, false)

        val application = requireNotNull(this.activity).application
        val cityDataSource = DataBase.getInstance(application)!!.databaseDao

        viewModel = ViewModelProvider(this, HomeViewModelFactory(cityDataSource = cityDataSource)).get(HomeViewModel::class.java)


        val all = EachWeatherFragmentArgs.fromBundle(requireArguments()).all

        binding.lifecycleOwner = this
        binding.all = all
        binding.weather = all.weather[0]

        return binding.root
    }
}