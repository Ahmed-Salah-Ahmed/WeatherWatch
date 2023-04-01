package com.iti.weatherwatch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.weatherwatch.datasource.IWeatherRepository
import com.iti.weatherwatch.home.viewmodel.HomeViewModel

class HomeViewModelFactory(private val repository: IWeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}
