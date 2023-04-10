package com.iti.weatherwatch.datasource.local

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import com.iti.weatherwatch.datasource.model.OpenWeatherApi
import com.iti.weatherwatch.datasource.model.WeatherAlert
import kotlinx.coroutines.flow.Flow

/*
This is an interface called WeatherDao, which represents the data access object (DAO) for the weather and alert data in the local database. It contains several functions that define the database operations that can be performed on the data. Here is a summary of what each function does:

getCurrentWeather(): retrieves the current weather data that is not marked as a favorite.
insertWeather(weather: OpenWeatherApi): inserts weather data into the database, replacing existing data if there is a conflict.
updateWeather(weather: OpenWeatherApi): updates existing weather data in the database.
deleteCurrentWeather(): deletes all current weather data that is not marked as a favorite.
getFavoritesWeather(): retrieves all weather data that is marked as a favorite.
deleteFavoriteWeather(id: Int): deletes a single weather data record that is marked as a favorite, specified by its ID.
getFavoriteWeather(id: Int): retrieves a single weather data record that is marked as a favorite, specified by its ID.
insertAlert(alert: WeatherAlert): inserts an alert data record into the database, replacing existing data if there is a conflict.
getAlertsList(): retrieves a list of all alert data records in the database.
deleteAlert(id: Int): deletes a single alert data record from the database, specified by its ID.
getAlert(id: Int): retrieves a single alert data record from the database, specified by its ID.
 */
@Dao
interface WeatherDao {
    @Query("select * from weather where isFavorite = 0")
    fun getCurrentWeather(): OpenWeatherApi

    @Insert(onConflict = REPLACE)
    suspend fun insertWeather(weather: OpenWeatherApi): Long

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
    suspend fun insertAlert(alert: WeatherAlert): Long

    @Query("select * from alert")
    fun getAlertsList(): Flow<List<WeatherAlert>>

    @Query("DELETE FROM alert where id = :id")
    suspend fun deleteAlert(id: Int)

    @Query("select * from alert where id = :id")
    fun getAlert(id: Int): WeatherAlert
}
