package com.iti.weatherwatch.favorites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.weatherwatch.datasource.WeatherRepository
import com.iti.weatherwatch.datasource.model.OpenWeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
/*
This code defines a FavoritesViewModel that is responsible for retrieving the list of favorite weather locations and deleting a favorite weather location. The FavoritesViewModel class has a constructor that takes an instance of the WeatherRepository as a parameter.

The getFavorites() function retrieves the list of favorite weather locations by calling the getFavoritesWeatherFromLocalDataSource() function of the WeatherRepository. The list of favorite weather locations is emitted using a MutableStateFlow that can be observed by the UI.

The deleteFavoriteWeather(id: Int) function deletes the favorite weather location with the given id by calling the deleteFavoriteWeather(id: Int) function of the WeatherRepository.

The favorites property is a MutableStateFlow that holds the list of favorite weather locations. It can be observed by the UI to get updates whenever the list of favorite weather locations changes.
 */
class FavoritesViewModel(private val repository: WeatherRepository) : ViewModel() {

    fun getFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFavoritesWeatherFromLocalDataSource().collect {
                _favorites.emit(it)
            }
        }
    }

    fun deleteFavoriteWeather(id: Int) {
        viewModelScope.launch {
            repository.deleteFavoriteWeather(id)
        }
    }

    private var _favorites = MutableStateFlow<List<OpenWeatherApi>>(emptyList())
    val favorites = _favorites

}
