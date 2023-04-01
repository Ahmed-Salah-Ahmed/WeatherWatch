package com.iti.weatherwatch.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.iti.weatherwatch.datasource.remote.RetrofitHelper
import com.iti.weatherwatch.model.OpenWeatherApi
import kotlinx.coroutines.runBlocking
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
        val retrofitHelper = RetrofitHelper
        var result: Response<OpenWeatherApi>?
        runBlocking {
            result = retrofitHelper.getCurrentWeather(lat, long, "en", "metric")
            assertNotNull(result?.body())
        }
    }
}
