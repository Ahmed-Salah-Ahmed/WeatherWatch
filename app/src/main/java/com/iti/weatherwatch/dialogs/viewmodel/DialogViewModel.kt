package com.iti.weatherwatch.dialogs.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.iti.weatherwatch.datasource.MyLocationProvider

/*
This is a class called DialogViewModel that extends the ViewModel class from the Android Architecture Components library. It takes an instance of MyLocationProvider in its constructor, which is a custom class that provides the user's location.

The DialogViewModel class has three methods: getFreshLocation(), observeLocation(), and observePermission().

getFreshLocation() is a function that requests the user's current location by calling the getFreshLocation() method on the MyLocationProvider instance.

observeLocation() returns a LiveData object that observes changes to the user's location. The MyLocationProvider class updates this LiveData object whenever the user's location changes.

observePermission() returns a LiveData object that observes changes to the user's location permission status. The MyLocationProvider class updates this LiveData object whenever the user grants or denies location permission.
 */
class DialogViewModel(private val myLocationProvider: MyLocationProvider) : ViewModel() {
    fun getFreshLocation() {
        myLocationProvider.getFreshLocation()
    }

    fun observeLocation(): LiveData<ArrayList<Double>> {
        return myLocationProvider.locationList
    }

    fun observePermission(): LiveData<String> {
        return myLocationProvider.denyPermission
    }
}
