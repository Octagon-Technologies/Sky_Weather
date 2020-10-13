package com.example.kotlinweatherapp.ui.future_forecast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.databinding.FragmentFutureForecastBinding


class FutureForecastFragment : Fragment() {

    lateinit var binding: FragmentFutureForecastBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFutureForecastBinding.inflate(inflater)



        return binding.root
    }

}