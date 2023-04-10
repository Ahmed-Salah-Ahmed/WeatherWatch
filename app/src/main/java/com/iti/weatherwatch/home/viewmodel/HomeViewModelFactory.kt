package com.iti.weatherwatch.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.weatherwatch.datasource.IWeatherRepository
import com.iti.weatherwatch.datasource.MyLocationProvider

/*
This is a factory class that implements the ViewModelProvider.Factory interface. It is responsible for creating instances of the HomeViewModel class.

The constructor takes two parameters, an instance of IWeatherRepository and an instance of MyLocationProvider.

The create() method takes a class argument, and if it is HomeViewModel, it creates a new instance of HomeViewModel and returns it. Otherwise, it throws an IllegalArgumentException. This factory is used to create HomeViewModel instances in the HomeFragment.
 */
class HomeViewModelFactory(
    private val repository: IWeatherRepository,
    private val myLocationProvider: MyLocationProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repository, myLocationProvider) as T
    }
}
