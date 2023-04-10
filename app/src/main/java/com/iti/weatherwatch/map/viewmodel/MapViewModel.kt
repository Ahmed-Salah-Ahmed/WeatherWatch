package com.iti.weatherwatch.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.weatherwatch.datasource.WeatherRepository
import kotlinx.coroutines.*

/*
This is a ViewModel class named MapViewModel that takes an instance of WeatherRepository as a constructor parameter. It has a function named setFavorite that takes four string parameters lat, lon, lang, and units.

 Inside the function, it launches a coroutine on the IO dispatcher using the viewModelScope property to insert favorite weather data into the local database by calling the insertFavoriteWeatherFromRemoteToLocal function of the WeatherRepository instance.

If an exception occurs during the execution of the coroutine, it will be thrown. Finally, the coroutine will be canceled.
 */
class MapViewModel(private val repository: WeatherRepository) : ViewModel() {

    fun setFavorite(lat: String, lon: String, lang: String, units: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insertFavoriteWeatherFromRemoteToLocal(lat, lon, lang, units)
            } catch (e: Exception) {
                throw e
            }
            this.cancel()
        }
    }

}
