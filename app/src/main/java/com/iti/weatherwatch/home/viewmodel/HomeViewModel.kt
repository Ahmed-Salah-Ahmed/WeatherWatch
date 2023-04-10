package com.iti.weatherwatch.home.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.iti.weatherwatch.datasource.IWeatherRepository
import com.iti.weatherwatch.datasource.MyLocationProvider
import com.iti.weatherwatch.datasource.model.OpenWeatherApi
import kotlinx.coroutines.*

/*
This is a ViewModel class for the Home screen of the weather application. It has functions to retrieve data from the database and from the remote data source, and a function to get the user's current location. It also has a LiveData object to observe the current weather data.

The getDataFromDatabase() function retrieves the current weather data from the local database using the repository's getCurrentWeatherFromLocalDataSource() function. It does so in a background coroutine to avoid blocking the main thread.

The getDataFromRemoteToLocal() function retrieves the current weather data from the remote data source using the repository's insertCurrentWeatherFromRemoteToLocal() function. It takes in the latitude and longitude of the user's location, the language setting, and the temperature units setting as parameters. It also does this in a background coroutine to avoid blocking the main thread. If the remote call succeeds, it updates the local database with the new data and retrieves the updated data from the database by calling getDataFromDatabase().

The getFreshLocation() function calls the getFreshLocation() function of the MyLocationProvider object, which retrieves the user's current location using the device's location services and updates the locationList LiveData object with the latitude and longitude of the user's location.

The observeLocation() function returns the locationList LiveData object so that the Home screen can observe changes to the user's location.

The _openWeatherAPI LiveData object is used to observe changes to the current weather data. It is updated with the latest data retrieved from the database or remote data source. The openWeatherAPI LiveData object is exposed to the Home screen to observe changes to the current weather data.
 */
class HomeViewModel(
    private val repository: IWeatherRepository,
    private val myLocationProvider: MyLocationProvider
) : ViewModel() {

    fun getDataFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            _openWeatherAPI.postValue(
                repository.getCurrentWeatherFromLocalDataSource()
            )
        }
    }

    fun getDataFromRemoteToLocal(lat: String, long: String, language: String, units: String) {
        var result: OpenWeatherApi? = null
        viewModelScope.launch(Dispatchers.Main) {
            val job =
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        result =
                            repository.insertCurrentWeatherFromRemoteToLocal(
                                lat,
                                long,
                                language,
                                units
                            )
                    } catch (e: Exception) {
                        Log.d("Error in HomeViewModel", e.toString())
                    }

                }
            job.join()
            result?.let { getDataFromDatabase() }
            this.cancel()
        }
    }

    fun getFreshLocation() {
        myLocationProvider.getFreshLocation()
    }

    fun observeLocation(): LiveData<ArrayList<Double>> {
        return myLocationProvider.locationList
    }

    private val _openWeatherAPI = MutableLiveData<OpenWeatherApi>()
    val openWeatherAPI: LiveData<OpenWeatherApi> = _openWeatherAPI
}
