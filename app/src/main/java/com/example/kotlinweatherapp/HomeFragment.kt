package com.example.kotlinweatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinweatherapp.databinding.FragmentHomeBinding
import com.example.kotlinweatherapp.database.DataBase

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = DataBase.getInstance(application)!!.databaseDao

        val viewModel = ViewModelProvider(this, HomeViewModelFactory(dataSource)).get(HomeViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        fun ifErrorExistsIsTrue(){
            binding.listRecyclerView.visibility = View.GONE
        }

        fun ifErrorExistsIsFalse(){
            binding.listRecyclerView.visibility = View.VISIBLE
        }

        binding.listRecyclerView.adapter = RecyclerAdapter()

        viewModel.doesErrorExist.observe(viewLifecycleOwner, Observer {
            if (it == true) {ifErrorExistsIsTrue()}
            else {ifErrorExistsIsFalse()}
        })



        return binding.root
    }
}