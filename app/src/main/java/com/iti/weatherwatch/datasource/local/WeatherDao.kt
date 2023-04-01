package com.iti.weatherwatch.datasource.local

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.IGNORE
import com.iti.weatherwatch.model.OpenWeatherApi

@Dao
interface WeatherDao {
    @Query("select * from weather where timezone = :timeZone")
    fun getCurrentWeather(timeZone: String): LiveData<OpenWeatherApi>

    @Insert(onConflict = IGNORE)
    suspend fun insertCurrentWeather(weather: OpenWeatherApi)

    @Update
    suspend fun updateCurrentWeather(weather: OpenWeatherApi)

    @Query("DELETE FROM weather WHERE timezone = :timeZone")
    suspend fun deleteCurrentWeather(timeZone: String)
}
