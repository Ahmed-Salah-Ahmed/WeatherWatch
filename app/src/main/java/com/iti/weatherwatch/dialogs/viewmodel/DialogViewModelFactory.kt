package com.iti.weatherwatch.dialogs.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.weatherwatch.datasource.MyLocationProvider

/*
This is a Kotlin class called DialogViewModelFactory which is implementing the ViewModelProvider.Factory interface. It is responsible for creating an instance of the DialogViewModel class.

The constructor of this class takes an instance of MyLocationProvider as a parameter. It overrides the create method from the ViewModelProvider.Factory interface which is used to create a new instance of the ViewModel.

Inside the create method, it checks if the modelClass parameter is of type DialogViewModel, and if so, it returns a new instance of DialogViewModel by passing the myLocationProvider parameter to its constructor.

This class provides a way to create instances of DialogViewModel using ViewModelProvider which is a part of the Android Architecture Components library. By using a ViewModelProvider.Factory, the ViewModel can be properly initialized with its dependencies.
 */
class DialogViewModelFactory(private val myLocationProvider: MyLocationProvider) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DialogViewModel(myLocationProvider) as T
    }
}
