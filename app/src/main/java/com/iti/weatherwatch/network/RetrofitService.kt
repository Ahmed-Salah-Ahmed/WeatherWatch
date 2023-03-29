package com.iti.weatherwatch.network

import com.iti.weatherwatch.model.OpenWeatherApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val appId = "7d691d845ca1e4b20b9e90fd19b05f1a"
private const val _exclude = "minutely"

interface RetrofitService {
    @GET("onecall")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") language: String = "en",
        @Query("units") units: String = "imperial",
        @Query("exclude") exclude: String = _exclude,
        @Query("appid") appid: String = appId
    ): Response<OpenWeatherApi>
}
