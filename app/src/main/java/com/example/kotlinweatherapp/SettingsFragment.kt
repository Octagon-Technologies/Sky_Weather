package com.example.kotlinweatherapp

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.kotlinweatherapp.database.DataBase

const val SIGNATURE = "signature"

class FragmentSettings: PreferenceFragmentCompat() {

    lateinit var viewModel: HomeViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.root_preferences)

        val application = requireNotNull(this.activity).application
        val dataSource = DataBase.getInstance(application)!!.databaseDao

        viewModel = ViewModelProvider(this, HomeViewModelFactory(dataSource)).get(HomeViewModel::class.java)

        findPreference<Preference>(SIGNATURE)!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener{ preference, newValue ->
            val stringValue = newValue.toString()
            viewModel.mainText.value = stringValue
            viewModel.insert()
            Log.i("ViewModelSettings", "Value of string value is $stringValue")
            Log.i("ViewModelSettings", "Value of viewModel.mainText.value is ${viewModel.mainText.value}")

            true
        }
    }


}