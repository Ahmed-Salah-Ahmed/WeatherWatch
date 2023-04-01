package com.iti.weatherwatch.datasource.remote

import com.iti.weatherwatch.model.OpenWeatherApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val appId = "7d691d845ca1e4b20b9e90fd19b05f1a"
private const val excludeMinutely = "minutely"
private const val defaultUnits = "metric"
private const val defaultLanguage = "en"

interface RetrofitService {
    @GET("onecall")
    suspend fun getCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String = excludeMinutely,
        @Query("units") units: String = defaultUnits,
        @Query("lang") lang: String = defaultLanguage,
        @Query("appid") app_id: String = appId
    ): Response<OpenWeatherApi>
}
