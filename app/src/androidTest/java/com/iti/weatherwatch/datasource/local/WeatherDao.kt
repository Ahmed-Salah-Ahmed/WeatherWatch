package com.iti.weatherwatch.datasource.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.iti.weatherwatch.datasource.model.*
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherDaoTest {

    private lateinit var database: WeatherDatabase
    private lateinit var weatherDao: WeatherDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, WeatherDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        weatherDao = database.weatherDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertWeatherAndGetById() = runBlocking {
        val weather = OpenWeatherApi(
            id = 1,
            isFavorite = false,
            lat = 30.0444,
            lon = 31.2357,
            timezone = "Africa/Cairo",
            timezoneOffset = 7200,
            current = Current(
                dt = 1649044800,
                sunrise = 1649025087,
                sunset = 1649071171,
                temp = 25.0,
                feelsLike = 25.0,
                pressure = 1012,
                humidity = 70,
                dewPoint = 19.17,
                uvi = 10.0,
                clouds = 0,
                visibility = 10000,
                windSpeed = 3.13,
                windDeg = 90,
                windGust = 5.14,
                weather = listOf(
                    Weather(id = 800, main = "Clear", description = "clear sky", icon = "01d")
                )
            ),
            hourly = listOf(),
            daily = listOf(),
            alerts = listOf()
        )
        weatherDao.insertWeather(weather)
        val result = weatherDao.getFavoriteWeather(1)
        assertEquals(weather, result)
    }

    @Test
    fun updateWeatherAndGetById() = runBlocking {
        val weather = OpenWeatherApi(
            id = 1,
            isFavorite = false,
            lat = 30.0444,
            lon = 31.2357,
            timezone = "Africa/Cairo",
            timezoneOffset = 7200,
            current = Current(
                dt = 1649044800,
                sunrise = 1649025087,
                sunset = 1649071171,
                temp = 25.0,
                feelsLike = 25.0,
                pressure = 1012,
                humidity = 70,
                dewPoint = 19.17,
                uvi = 10.0,
                clouds = 0,
                visibility = 10000,
                windSpeed = 3.13,
                windDeg = 90,
                windGust = 5.14,
                weather = listOf(
                    Weather(id = 800, main = "Clear", description = "clear sky", icon = "01d")
                )
            ),
            hourly = listOf(),
            daily = listOf(),
            alerts = listOf()
        )
        weatherDao.insertWeather(weather)
        val updatedWeather = weather.copy(isFavorite = true)
        weatherDao.updateWeather(updatedWeather)
        val result = weatherDao.getFavoriteWeather(1)
        assertEquals(updatedWeather, result)
    }

    @Ignore("Never Completes")
    @Test
    fun testDeleteCurrentWeather() = runBlocking {
        // Given a list of weather data
        val weatherData = listOf(
            OpenWeatherApi(
                1,
                true,
                30.0,
                31.0,
                "timezone",
                0,
                Current(0, 0, 0, 0.0, 0.0, 0, 0, 0.0, 0.0, 0, 0, 0.0, 0, 0.0, emptyList()),
                emptyList(),
                emptyList(),
                emptyList()
            ),
            OpenWeatherApi(
                2,
                false,
                30.0,
                31.0,
                "timezone",
                0,
                Current(0, 0, 0, 0.0, 0.0, 0, 0, 0.0, 0.0, 0, 0, 0.0, 0, 0.0, emptyList()),
                emptyList(),
                emptyList(),
                emptyList()
            ),
            OpenWeatherApi(
                3,
                false,
                30.0,
                31.0,
                "timezone",
                0,
                Current(0, 0, 0, 0.0, 0.0, 0, 0, 0.0, 0.0, 0, 0, 0.0, 0, 0.0, emptyList()),
                emptyList(),
                emptyList(),
                emptyList()
            )
        )

        weatherDao.insertWeather(weatherData[0])
        weatherDao.insertWeather(weatherData[1])
        weatherDao.insertWeather(weatherData[2])

        // When deleteCurrentWeather is called
        weatherDao.deleteCurrentWeather()

        // Then the list should only contain the favorite weather data
        weatherDao.getFavoritesWeather().collect {
            assertTrue(it.size == 1)
        }
    }
}
