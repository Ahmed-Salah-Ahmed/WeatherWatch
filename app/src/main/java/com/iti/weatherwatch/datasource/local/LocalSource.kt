package com.iti.weatherwatch.datasource.local

import androidx.lifecycle.LiveData
import com.iti.weatherwatch.model.OpenWeatherApi

interface LocalSource {
    fun getCurrentWeather(timeZone: String): LiveData<OpenWeatherApi>

    suspend fun insertCurrentWeather(weather: OpenWeatherApi)

    suspend fun updateCurrentWeather(weather: OpenWeatherApi)

    suspend fun deleteWeather(timeZone: String)
}
