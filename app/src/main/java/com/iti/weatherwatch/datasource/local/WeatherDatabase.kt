package com.iti.weatherwatch.datasource.local

import android.app.Application
import androidx.room.*
import com.iti.weatherwatch.model.OpenWeatherApi
import com.iti.weatherwatch.model.WeatherAlert

@TypeConverters(Converters::class)
@Database(
    entities = [OpenWeatherApi::class, WeatherAlert::class],
    version = 2,
    exportSchema = false
)
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
                ).fallbackToDestructiveMigration()
                    .build().also {
                        INSTANCE = it
                    }
            }
        }
    }
}
