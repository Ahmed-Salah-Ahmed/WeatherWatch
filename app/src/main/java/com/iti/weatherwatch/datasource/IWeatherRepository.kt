package com.iti.weatherwatch.datasource

import com.iti.weatherwatch.datasource.model.OpenWeatherApi
import com.iti.weatherwatch.datasource.model.WeatherAlert
import kotlinx.coroutines.flow.Flow

/*
This is an interface named IWeatherRepository. It outlines the methods that need to be implemented by a class that wants to act as a Weather Repository.

    insertFavoriteWeatherFromRemoteToLocal - a suspend function that takes in the latitude and longitude of a location and inserts its weather data into the local database.
    insertCurrentWeatherFromRemoteToLocal - a suspend function that takes in the latitude and longitude of a location and inserts its current weather data into the local database. It returns an OpenWeatherApi object.
    getCurrentWeatherFromLocalDataSource - a function that returns the current weather data from the local database as an OpenWeatherApi object.
    deleteWeathersFromLocalDataSource - a suspend function that deletes all weather data from the local database.
    getFavoritesWeatherFromLocalDataSource - a function that returns a flow of a list of favorite weather data saved in the local database.
    deleteFavoriteWeather - a suspend function that deletes a favorite weather data with the given ID from the local database.
    getFavoriteWeather - a function that returns a favorite weather data with the given ID from the local database as an OpenWeatherApi object.
    updateWeather - a suspend function that updates the weather data of a given location in the local database.
    updateFavoriteWeather - a suspend function that updates the weather data of a favorite location with the given ID in the local database.
    insertAlert - a suspend function that inserts a weather alert into the local database and returns its ID.
    getAlertsList - a function that returns a flow of a list of weather alerts saved in the local database.
    deleteAlert - a suspend function that deletes a weather alert with the given ID from the local database.
    getAlert - a function that returns a weather alert with the given ID from the local database.
 */
interface IWeatherRepository {
    suspend fun insertFavoriteWeatherFromRemoteToLocal(
        lat: String,
        long: String,
        language: String = "en",
        units: String = "metric"
    )

    suspend fun insertCurrentWeatherFromRemoteToLocal(
        lat: String,
        long: String,
        language: String = "en",
        units: String = "metric"
    ): OpenWeatherApi

    fun getCurrentWeatherFromLocalDataSource(): OpenWeatherApi

    suspend fun deleteWeathersFromLocalDataSource()

    fun getFavoritesWeatherFromLocalDataSource(): Flow<List<OpenWeatherApi>>

    suspend fun deleteFavoriteWeather(id: Int)

    fun getFavoriteWeather(id: Int): OpenWeatherApi

    suspend fun updateWeather(weather: OpenWeatherApi)

    suspend fun updateFavoriteWeather(
        latitude: String,
        longitude: String,
        units: String,
        language: String,
        id: Int
    ): OpenWeatherApi

    suspend fun insertAlert(alert: WeatherAlert): Long

    fun getAlertsList(): Flow<List<WeatherAlert>>

    suspend fun deleteAlert(id: Int)

    fun getAlert(id: Int): WeatherAlert

}
