package com.iti.weatherwatch.datasource.local

import com.iti.weatherwatch.model.OpenWeatherApi
import com.iti.weatherwatch.model.WeatherAlert
import kotlinx.coroutines.flow.Flow

interface LocalSource {
    fun getCurrentWeather(): OpenWeatherApi

    suspend fun insertCurrentWeather(weather: OpenWeatherApi)

    suspend fun updateWeather(weather: OpenWeatherApi)

    suspend fun deleteWeathers()

    fun getFavoritesWeather(
    ): Flow<List<OpenWeatherApi>>

    suspend fun deleteFavoriteWeather(id: Int)

    fun getFavoriteWeather(id: Int): OpenWeatherApi

    suspend fun insertAlert(alert: WeatherAlert)

    fun getAlertsList(): Flow<List<WeatherAlert>>

    suspend fun deleteAlert(id: Int)

}
