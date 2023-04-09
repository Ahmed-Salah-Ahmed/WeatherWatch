package com.iti.weatherwatch.datasource.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.iti.weatherwatch.datasource.model.*
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherDatabaseTest {

    private lateinit var database: WeatherDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testInsertAndGetWeather() = runBlocking {
        // Given
        val weather = OpenWeatherApi(
            1,
            true,
            10.0,
            20.0,
            "America/New_York",
            -14400,
            Current(
                1649022949,
                1649005471,
                1649050158,
                23.0,
                21.0,
                1013,
                60,
                18.0,
                5.0,
                90,
                10000,
                5.1,
                150,
                10.0,
                listOf(Weather(801, "Clouds", "few clouds", "02d"))
            ),
            listOf(
                Hourly(
                    1649022000,
                    22.0,
                    20.0,
                    1007,
                    52,
                    11.0,
                    0.0,
                    5,
                    10000,
                    3.2,
                    123,
                    7.8,
                    listOf(Weather(801, "Clouds", "few clouds", "02d")),
                    0.0
                )
            ),
            listOf(
                Daily(
                    1649022000,
                    1649005471,
                    1649050158,
                    1648987751,
                    1649034249,
                    0.28,
                    Temp(23.0, 20.0, 24.0, 20.0, 23.0, 24.0),
                    FeelsLike(21.0, 19.0, 21.0, 20.0),
                    1013,
                    60,
                    18.0,
                    5.1,
                    150,
                    10.0,
                    listOf(Weather(801, "Clouds", "few clouds", "02d")),
                    0,
                    0.0,
                    0.0
                )
            ),
            null
        )

        // When
        database.weatherDao().insertWeather(weather)
        val result = database.weatherDao().getFavoriteWeather(1)

        // Then
        assert(result == weather)
    }

}
