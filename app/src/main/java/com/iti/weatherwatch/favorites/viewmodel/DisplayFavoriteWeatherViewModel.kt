package com.iti.weatherwatch.favorites.viewmodel

import androidx.lifecycle.*
import com.iti.weatherwatch.datasource.WeatherRepository
import com.iti.weatherwatch.datasource.model.OpenWeatherApi
import kotlinx.coroutines.*

/*
his is a ViewModel class named DisplayFavoriteWeatherViewModel. It receives a WeatherRepository instance in its constructor and provides functions to retrieve and update weather data for a favorite location.

The getWeather function takes an id parameter and retrieves the weather data for the favorite location with that ID using the WeatherRepository instance. It launches a coroutine in the IO context to perform the repository operation and sets the result to a private _weather mutable live data object using the postValue function. The coroutine job is launched in the ViewModel's viewModelScope. Then, it launches another coroutine in the Main context to wait for the first job to finish, set the result to the public weather live data object, and then cancel the job and itself.

The updateWeather function takes the same parameters as getWeather plus some additional parameters to update the location's weather data. It performs a similar operation as getWeather but uses the updateFavoriteWeather function of the WeatherRepository instance to update the location's weather data.

The weather property is a public live data object that holds the weather data for the favorite location.
 */
class DisplayFavoriteWeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    fun getWeather(id: Int) {
        var result: OpenWeatherApi? = null
        val job = viewModelScope.launch(Dispatchers.IO) {
            result = repository.getFavoriteWeather(id)
        }
        viewModelScope.launch(Dispatchers.Main) {
            job.join()
            job.cancel()
            _weather.postValue(result)
            this.cancel()
        }
    }

    fun updateWeather(
        latitude: Double,
        longitude: Double,
        units: String,
        language: String,
        id: Int
    ) {
        var result: OpenWeatherApi? = null
        val job = viewModelScope.launch(Dispatchers.IO) {
            result =
                repository.updateFavoriteWeather("$latitude", "$longitude", units, language, id)
        }
        viewModelScope.launch(Dispatchers.Main) {
            job.join()
            job.cancel()
            _weather.postValue(result)
            this.cancel()
        }
    }

    private var _weather = MutableLiveData<OpenWeatherApi?>()
    val weather = _weather

}
