package com.iti.weatherwatch.datasource.local

import com.iti.weatherwatch.datasource.model.OpenWeatherApi
import com.iti.weatherwatch.datasource.model.WeatherAlert
import kotlinx.coroutines.flow.Flow

/*
This is an interface called LocalSource that defines the contract for accessing local data in the app.

getCurrentWeather(): retrieves the current weather from local storage.
insertCurrentWeather(weather: OpenWeatherApi): Long: inserts the current weather into local storage and returns the ID of the inserted data.
updateWeather(weather: OpenWeatherApi): updates the current weather in local storage.
deleteWeathers(): deletes all weather data from local storage.
getFavoritesWeather(): retrieves a list of favorite weather locations from local storage as a Flow of List<OpenWeatherApi>.
deleteFavoriteWeather(id: Int): deletes a favorite weather location with the specified ID from local storage.
getFavoriteWeather(id: Int): retrieves a favorite weather location with the specified ID from local storage.
insertAlert(alert: WeatherAlert): Long: inserts a weather alert into local storage and returns the ID of the inserted data.
getAlertsList(): retrieves a list of weather alerts from local storage as a Flow of List<WeatherAlert>.
deleteAlert(id: Int): deletes a weather alert with the specified ID from local storage.
getAlert(id: Int): retrieves a weather alert with the specified ID from local storage.
 */
interface LocalSource {
    fun getCurrentWeather(): OpenWeatherApi

    suspend fun insertCurrentWeather(weather: OpenWeatherApi): Long

    suspend fun updateWeather(weather: OpenWeatherApi)

    suspend fun deleteWeathers()

    fun getFavoritesWeather(
    ): Flow<List<OpenWeatherApi>>

    suspend fun deleteFavoriteWeather(id: Int)

    fun getFavoriteWeather(id: Int): OpenWeatherApi

    suspend fun insertAlert(alert: WeatherAlert): Long

    fun getAlertsList(): Flow<List<WeatherAlert>>

    suspend fun deleteAlert(id: Int)

    fun getAlert(id: Int): WeatherAlert

}
