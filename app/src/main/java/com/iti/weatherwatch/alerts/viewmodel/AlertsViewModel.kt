package com.iti.weatherwatch.alerts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.weatherwatch.datasource.WeatherRepository
import com.iti.weatherwatch.datasource.model.WeatherAlert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/*
This is a Kotlin class AlertsViewModel that extends the ViewModel class provided by Android's Jetpack architecture components. The class takes a WeatherRepository object in its constructor, which is used to interact with the data source.

The class has a private mutable StateFlow object _alerts that holds a list of WeatherAlert objects. It also has a public alerts property that exposes a read-only version of _alerts.

The class has two functions. The getFavorites() function is used to fetch the list of favorite weather alerts from the data source using a coroutine launched on the I/O thread. The function collects the list of alerts and emits it to _alerts. The deleteFavoriteWeather() function is used to delete a specific favorite weather alert from the data source using a coroutine launched on the main thread.

Overall, AlertsViewModel is responsible for handling interactions with the data source related to weather alerts in the Android application. It uses coroutines and Jetpack's ViewModel class to ensure efficient and safe handling of data operations.
 */
class AlertsViewModel(private val repository: WeatherRepository) : ViewModel() {

    private var _alerts = MutableStateFlow<List<WeatherAlert>>(emptyList())
    val alerts = _alerts

    fun getFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAlertsList().collect {
                _alerts.emit(it)
            }
        }
    }

    fun deleteFavoriteWeather(id: Int) {
        viewModelScope.launch {
            repository.deleteAlert(id)
        }
    }
}
