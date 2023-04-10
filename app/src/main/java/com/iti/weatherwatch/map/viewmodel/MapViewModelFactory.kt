package com.iti.weatherwatch.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.weatherwatch.datasource.WeatherRepository

/*
This is a factory class that implements the ViewModelProvider.Factory interface. It's responsible for creating instances of MapViewModel class with a specified constructor that takes a WeatherRepository parameter.

The create method receives a Class object that represents the MapViewModel class, and it returns an instance of MapViewModel using the constructor that takes a WeatherRepository parameter. This is achieved by simply instantiating the MapViewModel class and passing the WeatherRepository instance to it.
 */
class MapViewModelFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(repository) as T
    }
}
