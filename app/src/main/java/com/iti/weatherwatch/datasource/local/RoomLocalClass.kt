package com.iti.weatherwatch.datasource.local

import androidx.lifecycle.LiveData
import com.iti.weatherwatch.model.OpenWeatherApi

class RoomLocalClass(private val weatherDao: WeatherDao) : LocalSource {

    override fun getCurrentWeather(timeZone: String): LiveData<OpenWeatherApi> {
        return weatherDao.getCurrentWeather(timeZone)
    }

    override suspend fun insertCurrentWeather(weather: OpenWeatherApi) {
        weatherDao.insertCurrentWeather(weather)
    }

    override suspend fun updateCurrentWeather(weather: OpenWeatherApi) {
        weatherDao.updateCurrentWeather(weather)
    }

    override suspend fun deleteWeather(timeZone: String) {
        weatherDao.deleteCurrentWeather(timeZone)
    }

}
