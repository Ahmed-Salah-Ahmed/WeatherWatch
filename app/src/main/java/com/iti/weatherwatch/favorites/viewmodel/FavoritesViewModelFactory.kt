package com.iti.weatherwatch.favorites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.weatherwatch.datasource.WeatherRepository
/*
This is a FavoritesViewModelFactory class that implements the ViewModelProvider.Factory interface. It is responsible for creating instances of the FavoritesViewModel class with the appropriate dependencies.

The FavoritesViewModelFactory constructor takes a WeatherRepository object as a parameter. The create method of this factory class receives the class type of the ViewModel to be created as a parameter, and then creates a new instance of the FavoritesViewModel with the provided WeatherRepository.

This class is used in conjunction with the ViewModelProvider class to instantiate the FavoritesViewModel class with its required dependencies.
 */
class FavoritesViewModelFactory(private val repository: WeatherRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoritesViewModel(repository) as T
    }
}
