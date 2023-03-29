package com.iti.weatherwatch.network

import com.iti.weatherwatch.model.OpenWeatherApi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper : RemoteSource {
    private const val baseUrl = "https://api.openweathermap.org/data/2.5/"

    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofitService: RetrofitService by lazy {
        retrofit.create(RetrofitService::class.java)
    }

    override suspend fun getCurrentWeather(
        lat: Double,
        long: Double,
        language: String,
        units: String
    ): Response<OpenWeatherApi> =
        retrofitService.getCurrentWeather(lat, long, language, units)

}
