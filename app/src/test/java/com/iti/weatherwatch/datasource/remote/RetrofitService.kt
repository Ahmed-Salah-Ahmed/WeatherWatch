package com.iti.weatherwatch.datasource.remote

import com.iti.weatherwatch.datasource.model.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.Response

class RetrofitServiceTest {
    @Mock
    private lateinit var retrofitService: RetrofitService

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testGetCurrentWeather() = runBlocking {
        // Mock the API response
        val lat = "37.7749"
        val lon = "-122.4194"
        val units = "metric"
        val lang = "en"
        val appId = "7d691d845ca1e4b20b9e90fd19b05f1a"
        val response = Response.success(
            OpenWeatherApi(
                id = 1,
                isFavorite = false,
                lat = 37.7749,
                lon = -122.4194,
                timezone = "America/Los_Angeles",
                timezoneOffset = -25200,
                current = Current(
                    dt = 1649634916,
                    sunrise = 1649604781,
                    sunset = 1649649121,
                    temp = 12.2,
                    feelsLike = 10.8,
                    pressure = 1018,
                    humidity = 82,
                    dewPoint = 9.2,
                    uvi = 1.3,
                    clouds = 75,
                    visibility = 10000,
                    windSpeed = 2.06,
                    windDeg = 250,
                    windGust = 10.0,
                    weather = listOf(
                        Weather(
                            id = 803,
                            main = "Clouds",
                            description = "broken clouds",
                            icon = "04n"
                        )
                    )
                ),
                hourly = emptyList(),
                daily = emptyList(),
                alerts = emptyList()
            )
        )
        `when`(
            retrofitService.getCurrentWeather(
                lat,
                lon,
                units,
                lang,
                appId
            )
        ).thenReturn(response)

        // Call the API and verify the response
        val result = retrofitService.getCurrentWeather(
            lat,
            lon,
            units,
            lang,
            appId
        )
        assertEquals(response, result)
    }

    // Mockito cannot mock Final Class, instead use Mockk ?
    @Test
    fun `test getCurrentWeather returns success response`() = runBlocking {
        // given
        val lat = "37.7749"
        val lon = "-122.4194"
        val response = mock(Response::class.java, RETURNS_DEEP_STUBS) as Response<OpenWeatherApi>
        `when`(
            retrofitService.getCurrentWeather(
                lat,
                lon,
                units = "metric",
                lang = "en"
            )
        ).thenReturn(response)

        // when
        val result = retrofitService.getCurrentWeather(lat, lon, units = "metric", lang = "en")

        // then
        verify(retrofitService).getCurrentWeather(lat, lon, units = "metric", lang = "en")
        assertTrue(result.isSuccessful)
        assertEquals(response, result)
    }
}
