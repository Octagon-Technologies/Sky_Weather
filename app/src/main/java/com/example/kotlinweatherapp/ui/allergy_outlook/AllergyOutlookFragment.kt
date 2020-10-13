package com.example.kotlinweatherapp.ui.allergy_outlook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.databinding.AllergyOutlookFragmentBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class AllergyOutlookFragment : Fragment() {

    private lateinit var viewModel: AllergyOutlookViewModel
    private lateinit var binding: AllergyOutlookFragmentBinding
    private lateinit var barChart: BarChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AllergyOutlookFragmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this, AllergyOutlookViewModelFactory(requireContext())).get(AllergyOutlookViewModel::class.java)

        barChart = binding.barChart



        return binding.root
    }

    private fun getBarChartEntries() {
        val arrayOfBarEntry = ArrayList<BarEntry>()

    }

}