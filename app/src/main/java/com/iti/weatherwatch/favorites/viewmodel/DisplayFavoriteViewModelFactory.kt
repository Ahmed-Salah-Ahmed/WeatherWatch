package com.iti.weatherwatch.favorites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.weatherwatch.datasource.WeatherRepository

/*
The DisplayFavoriteViewModelFactory class is a factory class for creating instances of the DisplayFavoriteWeatherViewModel class. It implements the ViewModelProvider.Factory interface and overrides its create() method, which creates and returns the view model instance.

The DisplayFavoriteViewModelFactory takes a WeatherRepository object as a constructor parameter, which is used to create the DisplayFavoriteWeatherViewModel instance. The create() method casts the created view model instance to the generic type T and returns it.
 */
class DisplayFavoriteViewModelFactory(private val repository: WeatherRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DisplayFavoriteWeatherViewModel(repository) as T
    }
}
