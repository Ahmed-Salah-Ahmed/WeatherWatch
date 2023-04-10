package com.iti.weatherwatch.datasource.local

import com.iti.weatherwatch.datasource.model.OpenWeatherApi
import com.iti.weatherwatch.datasource.model.WeatherAlert
import kotlinx.coroutines.flow.Flow

/*
This is a class called RoomLocalClass, which implements the LocalSource interface.

The RoomLocalClass class serves as the implementation of the data access layer for the local database using Room, a database library for Android.

The class takes a weatherDao instance of the WeatherDao interface in its constructor, which is used to access the data stored in the database.

The class provides implementations for all the methods defined in the LocalSource interface, including getCurrentWeather(), getFavoriteWeather(id), insertAlert(alert), getAlertsList(), deleteAlert(id), insertCurrentWeather(weather), updateWeather(weather), deleteWeathers(), getFavoritesWeather(), and deleteFavoriteWeather(id).

These methods perform various database operations such as inserting and updating weather data, getting current and favorite weather data, deleting weather data, getting alerts list and getting a specific alert by id. Some of these methods return the data as Flow, which is a reactive stream that allows the data to be observed for changes.

Overall, this class acts as an intermediary between the app and the local database and provides an abstraction for the app to access and manipulate the local data.
 */
class RoomLocalClass(private val weatherDao: WeatherDao) : LocalSource {
    override fun getCurrentWeather(): OpenWeatherApi {
        return weatherDao.getCurrentWeather()
    }

    override fun getFavoriteWeather(id: Int): OpenWeatherApi {
        return weatherDao.getFavoriteWeather(id)
    }

    override suspend fun insertAlert(alert: WeatherAlert): Long {
        return weatherDao.insertAlert(alert)
    }

    override fun getAlertsList(): Flow<List<WeatherAlert>> {
        return weatherDao.getAlertsList()
    }

    override suspend fun deleteAlert(id: Int) {
        weatherDao.deleteAlert(id)
    }

    override suspend fun insertCurrentWeather(weather: OpenWeatherApi): Long {
        return weatherDao.insertWeather(weather)
    }

    override suspend fun updateWeather(weather: OpenWeatherApi) {
        weatherDao.updateWeather(weather)
    }

    override suspend fun deleteWeathers() {
        weatherDao.deleteCurrentWeather()
    }

    override fun getFavoritesWeather(): Flow<List<OpenWeatherApi>> {
        return weatherDao.getFavoritesWeather()
    }

    override suspend fun deleteFavoriteWeather(id: Int) {
        weatherDao.deleteFavoriteWeather(id)
    }

    override fun getAlert(id: Int): WeatherAlert {
        return weatherDao.getAlert(id)
    }
}
