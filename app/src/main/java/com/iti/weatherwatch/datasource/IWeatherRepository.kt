package com.iti.weatherwatch.datasource

import androidx.lifecycle.LiveData
import com.iti.weatherwatch.model.OpenWeatherApi

interface IWeatherRepository {
    suspend fun updateWeatherFromRemoteDataSource(
        lat: String,
        long: String,
        language: String = "en",
        units: String = "metric"
    ): OpenWeatherApi

    suspend fun insertWeatherFromRemoteDataSource(
        lat: String,
        long: String,
        language: String = "en",
        units: String = "metric"
    ): OpenWeatherApi

    fun getWeatherFromLocalDataSource(
        timeZone: String
    ): LiveData<OpenWeatherApi>

    suspend fun deleteWeatherFromLocalDataSource(
        timeZone: String
    )
}
