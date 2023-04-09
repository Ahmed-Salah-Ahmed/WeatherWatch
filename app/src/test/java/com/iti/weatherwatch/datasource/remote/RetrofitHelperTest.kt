package com.iti.weatherwatch.datasource.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.iti.weatherwatch.datasource.model.OpenWeatherApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

//@RunWith(AndroidJUnit4::class)
class RetrofitHelperTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun getCurrentWeather_latAndLong_returnOpenWeatherApiModelHasValue() {
        val lat = "31.19264"
        val long = "29.9060892"
        val language = "en"
        val units = "metric"
        val retrofitHelper = RetrofitHelper
        var result: Response<OpenWeatherApi>?
        runBlocking {
            result = retrofitHelper.getCurrentWeather(lat, long, language, units)
            assertNotNull(result?.body())
        }
    }

    @Test
    fun testGetCurrentWeather() = runBlocking {
        val response = RetrofitHelper.getCurrentWeather(
            lat = "31.19264",
            long = "29.9060892",
            language = "en",
            units = "metric"
        )
        assertEquals(true, response.isSuccessful)
        val weather = response.body()
        assertEquals(OpenWeatherApi::class.java, weather?.javaClass)
    }
}
