package com.iti.weatherwatch.datasource.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.iti.weatherwatch.model.OpenWeatherApi

@TypeConverters(Converters::class)
@Database(entities = [OpenWeatherApi::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase? = null
        fun getDatabase(application: Application): WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    application.applicationContext,
                    WeatherDatabase::class.java,
                    "weather.db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}
