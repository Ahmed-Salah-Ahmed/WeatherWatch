package com.iti.weatherwatch.ui.favorites

import androidx.lifecycle.*

class FavoritesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is favorites Fragment"
    }
    val text: LiveData<String> = _text
}
