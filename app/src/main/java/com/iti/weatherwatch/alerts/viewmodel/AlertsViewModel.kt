package com.iti.weatherwatch.alerts.viewmodel

import androidx.lifecycle.*

class AlertsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is alerts Fragment"
    }
    val text: LiveData<String> = _text
}
