package com.iti.weatherwatch.dialogs.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.weatherwatch.datasource.WeatherRepository
import com.iti.weatherwatch.model.WeatherAlert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertTimeDialogViewModel(private val repository: WeatherRepository) : ViewModel() {
    fun insertAlert(alert: WeatherAlert) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAlert(alert)
        }
    }
}
