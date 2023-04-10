package com.iti.weatherwatch.dialogs.viewmodel

import androidx.lifecycle.*
import com.iti.weatherwatch.datasource.WeatherRepository
import com.iti.weatherwatch.datasource.model.WeatherAlert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
This is a ViewModel class named AlertTimeDialogViewModel. It is used in the context of a dialog to add a new alert time for weather conditions.

The ViewModel class takes in an instance of the WeatherRepository class through its constructor. It provides a single method insertAlert which takes in a WeatherAlert object and adds it to the repository.

The _id variable is a MutableLiveData that holds the ID of the newly inserted alert. This ID can be observed by external classes through the id LiveData object.

The insertAlert function launches a coroutine to insert the alert into the repository using the IO dispatcher. Once the insertion is complete, the ID of the inserted alert is set in the _id MutableLiveData object, which can be observed by external classes.
 */
class AlertTimeDialogViewModel(private val repository: WeatherRepository) : ViewModel() {
    fun insertAlert(alert: WeatherAlert) {
        var response: Long? = null
        viewModelScope.launch(Dispatchers.IO) {
            val job = viewModelScope.launch(Dispatchers.IO) {
                response = repository.insertAlert(alert)
            }
            job.join()
            if (response != null) {
                _id.postValue(response!!)
            }
        }
    }

    private var _id = MutableLiveData<Long>()
    val id = _id
}
