package com.iti.weatherwatch.dialogs.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.weatherwatch.datasource.WeatherRepository

/*
This class is a ViewModelFactory used to create instances of AlertTimeDialogViewModel class. The factory implements the ViewModelProvider.Factory interface and overrides the create method. The create method takes a modelClass parameter which is the class of the ViewModel to be created, and returns an instance of the ViewModel.

The AlertTimeViewModelFactory class has a single constructor that takes a WeatherRepository instance as a parameter. This WeatherRepository instance is used to create an instance of the AlertTimeDialogViewModel by calling the constructor of the AlertTimeDialogViewModel class and passing the WeatherRepository instance to it. The create method returns this instance after casting it to the type of ViewModel requested by the caller.
 */
class AlertTimeViewModelFactory(private val repository: WeatherRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlertTimeDialogViewModel(repository) as T
    }
}
