package com.example.kotlinweatherapp

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.kotlinweatherapp.database.WeatherDataBase

const val SET_LOCATION = "setLocation"
const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"

class FragmentSettings: PreferenceFragmentCompat() {

    lateinit var viewModel: HomeViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.root_preferences)

        val application = requireNotNull(this.activity).application
        val dataBase = WeatherDataBase.getInstance(application)

        viewModel = ViewModelProvider(this, HomeViewModelFactory(dataBase!!)).get(HomeViewModel::class.java)

        findPreference<Preference>(SET_LOCATION)!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener{ preference, newValue ->
            val stringValue = newValue.toString()
            viewModel.mainText.value = stringValue
            viewModel.insertCityName()
            Log.i("ViewModelSettings", "Value of string value is $stringValue")
            Log.i("ViewModelSettings", "Value of viewModel.mainText.value is ${viewModel.mainText.value}")

            true
        }


        findPreference<Preference>(USE_DEVICE_LOCATION)!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener{ preference: Preference?, newValue: Any? ->
            viewModel.useDeviceLocation.value = newValue as Boolean
            viewModel.insertCityName()
            Log.i("SettingsFrag", "Value of viewModel.useDeviceLocation.value is ${viewModel.useDeviceLocation.value} and Value of newValue is $newValue")
            true
        }

    }
}