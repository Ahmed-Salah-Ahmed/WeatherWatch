package com.iti.weatherwatch.alerts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.weatherwatch.datasource.WeatherRepository

/*
This is a Kotlin class AlertsViewModelFactory that implements the ViewModelProvider.Factory interface provided by Android's Jetpack architecture components. The purpose of this class is to provide an instance of the AlertsViewModel class with a WeatherRepository object dependency.

The class takes a WeatherRepository object in its constructor, which is used to construct an instance of AlertsViewModel. It has a single function create() that overrides the create() function in ViewModelProvider.Factory. The function takes a generic type T that extends ViewModel and returns an instance of AlertsViewModel.

Inside the function, an instance of AlertsViewModel is created with the WeatherRepository object provided in the constructor. The instance is then cast to type T and returned. This way, whenever an instance of AlertsViewModel is required, the AlertsViewModelFactory can be used to provide an instance of it with a WeatherRepository object dependency.

Overall, AlertsViewModelFactory is responsible for providing instances of the AlertsViewModel class with dependencies whenever requested. It is a standard implementation of the ViewModelProvider.Factory interface that is used in Jetpack ViewModel architecture components.
 */
class AlertsViewModelFactory(private val repository: WeatherRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlertsViewModel(repository) as T
    }
}
