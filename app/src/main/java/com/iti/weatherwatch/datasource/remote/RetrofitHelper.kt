package com.iti.weatherwatch.datasource.remote

import com.iti.weatherwatch.datasource.model.OpenWeatherApi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
This is a Kotlin singleton object that implements the RemoteSource interface, which provides a method to fetch the current weather data from the OpenWeather API.

The class has a lazy initialization of a retrofitService which is an instance of Retrofit that is created only when it is accessed for the first time. It uses GsonConverterFactory to convert JSON responses to Kotlin data classes.

The RetrofitHelper object uses Retrofit to create a service for communicating with the OpenWeather API. It creates a singleton instance of RetrofitService using the base URL for the OpenWeather API and the GsonConverterFactory for JSON serialization and deserialization.

The getCurrentWeather method takes four parameters representing the latitude and longitude of the location, the language of the weather data, and the unit system to use. It calls the getCurrentWeather method of the RetrofitService to make a network call to the OpenWeather API and retrieve the current weather data for the specified location.

Overall, this class provides an easy way to interact with the OpenWeather API and get the current weather data for a given location.
 */
object RetrofitHelper : RemoteSource {
    private const val baseUrl = "https://api.openweathermap.org/data/2.5/"

    private val retrofitService by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitService::class.java)
    }

    override suspend fun getCurrentWeather(
        lat: String,
        long: String,
        language: String,
        units: String
    ): Response<OpenWeatherApi> =
        retrofitService.getCurrentWeather(lat, long, lang = language, units = units)

}
