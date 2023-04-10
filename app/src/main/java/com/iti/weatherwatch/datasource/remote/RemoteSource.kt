package com.iti.weatherwatch.datasource.remote

import com.iti.weatherwatch.datasource.model.OpenWeatherApi
import retrofit2.Response

/*
This is an interface that defines the methods to fetch data from a remote source, specifically the OpenWeather API.

It includes one method, getCurrentWeather(), which takes in latitude and longitude values as strings, as well as language and units as additional parameters. This method is expected to return a Retrofit Response object containing the data from the OpenWeather API in the form of an OpenWeatherApi object.

The use of suspend indicates that this method is meant to be executed asynchronously.
 */
interface RemoteSource {
    suspend fun getCurrentWeather(
        lat: String,
        long: String,
        language: String,
        units: String
    ): Response<OpenWeatherApi>
}
