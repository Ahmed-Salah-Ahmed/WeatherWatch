package com.iti.weatherwatch.home.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.iti.weatherwatch.datasource.IWeatherRepository
import com.iti.weatherwatch.datasource.MyLocationProvider
import com.iti.weatherwatch.model.OpenWeatherApi
import kotlinx.coroutines.*

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
                        Log.i("zoz", "getDataFromRemoteToLocal: ${e.message}")
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
