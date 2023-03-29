package com.iti.weatherwatch.network

import com.iti.weatherwatch.model.OpenWeatherApi
import retrofit2.Response

interface RemoteSource {
    suspend fun getCurrentWeather(
        lat: Double,
        long: Double,
        language: String = "en",
        units: String = "imperial",
    ): Response<OpenWeatherApi>
}
