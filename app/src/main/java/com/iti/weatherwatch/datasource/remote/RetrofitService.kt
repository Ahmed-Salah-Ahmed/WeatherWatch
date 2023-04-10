package com.iti.weatherwatch.datasource.remote

import com.iti.weatherwatch.datasource.model.OpenWeatherApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/*
This class defines an interface for making API calls using Retrofit library. It contains a single method, getCurrentWeather, annotated with @GET annotation which specifies the HTTP GET request method and the endpoint path of the API. The method takes in 6 parameters which are annotated with @Query annotation, indicating that they are query parameters of the API. The getCurrentWeather method returns a Response object containing the response from the API call, which is expected to be of type OpenWeatherApi.

interface RetrofitService: this interface defines the endpoints for the OpenWeather API. It uses Retrofit annotations to specify the HTTP method, path, and query parameters.

@GET("onecall"): this annotation specifies the HTTP method and path for the API request. In this case, it specifies that we want to use the onecall endpoint to get the current weather.

This class also contains two private constants, appId and excludeMinutely, that are used to build the API endpoint URL. The excludeMinutely constant is used to exclude the minutely weather forecast data from the API response, while the appId constant is the API key used for authentication to access the OpenWeather API.
 */
private const val appId = "7d691d845ca1e4b20b9e90fd19b05f1a"
private const val excludeMinutely = "minutely"

interface RetrofitService {
    @GET("onecall")
    suspend fun getCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String = excludeMinutely,
        @Query("units") units: String,
        @Query("lang") lang: String,
        @Query("appid") app_id: String = appId
    ): Response<OpenWeatherApi>
}
