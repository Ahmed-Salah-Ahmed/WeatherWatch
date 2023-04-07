package com.iti.weatherwatch.dialogs.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.iti.weatherwatch.datasource.MyLocationProvider

class DialogViewModel(private val myLocationProvider: MyLocationProvider) : ViewModel() {

    fun getFreshLocation() {
        myLocationProvider.getFreshLocation()
    }

    fun observeLocation(): LiveData<ArrayList<Double>> {
        return myLocationProvider.locationList
    }

}
