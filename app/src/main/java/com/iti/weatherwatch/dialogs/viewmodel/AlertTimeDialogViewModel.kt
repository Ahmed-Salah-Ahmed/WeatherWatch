package com.iti.weatherwatch.dialogs.viewmodel

import androidx.lifecycle.*
import com.iti.weatherwatch.datasource.WeatherRepository
import com.iti.weatherwatch.datasource.model.WeatherAlert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
