package com.iti.weatherwatch.datasource.remote

import com.iti.weatherwatch.model.OpenWeatherApi
import retrofit2.Response

interface RemoteSource {
    suspend fun getCurrentWeather(
        lat: String,
        long: String,
        language: String,
        units: String
    ): Response<OpenWeatherApi>
}
