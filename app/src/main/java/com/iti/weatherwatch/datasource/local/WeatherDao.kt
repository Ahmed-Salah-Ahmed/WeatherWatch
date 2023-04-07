package com.iti.weatherwatch.datasource.local

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import com.iti.weatherwatch.model.OpenWeatherApi
import com.iti.weatherwatch.model.WeatherAlert
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("select * from weather where isFavorite = 0")
    fun getCurrentWeather(): OpenWeatherApi

    @Insert(onConflict = REPLACE)
    suspend fun insertWeather(weather: OpenWeatherApi)

    @Update
    suspend fun updateWeather(weather: OpenWeatherApi)

    @Query("DELETE FROM weather where isFavorite = 0")
    suspend fun deleteCurrentWeather()

    @Query("select * from weather where isFavorite = 1")
    fun getFavoritesWeather(): Flow<List<OpenWeatherApi>>

    @Query("DELETE FROM weather where id = :id")
    suspend fun deleteFavoriteWeather(id: Int)

    @Query("select * from weather where id = :id")
    fun getFavoriteWeather(id: Int): OpenWeatherApi

    @Insert(onConflict = REPLACE)
    suspend fun insertAlert(alert: WeatherAlert)

    @Query("select * from alert")
    fun getAlertsList(): Flow<List<WeatherAlert>>

    @Query("DELETE FROM alert where id = :id")
    suspend fun deleteAlert(id: Int)
}
