package com.iti.weatherwatch.datasource.local

import android.app.Application
import android.content.Context
import androidx.room.*
import com.iti.weatherwatch.datasource.model.OpenWeatherApi
import com.iti.weatherwatch.datasource.model.WeatherAlert

/*
This class represents a Room database for storing weather data and alerts. Here are the details of the class:

@TypeConverters(Converters::class): This annotation specifies that the Converters class contains type converters that the Room database will use to convert custom data types to and from their representation in the database.

@Database(entities = [OpenWeatherApi::class, WeatherAlert::class], version = 2, exportSchema = false): This annotation specifies the database entities, their version, and export schema option. In this case, the entities are OpenWeatherApi and WeatherAlert.

abstract class WeatherDatabase : RoomDatabase(): This is an abstract class that extends RoomDatabase. It provides an abstract method for getting a DAO instance.

abstract fun weatherDao(): WeatherDao: This abstract method returns an instance of the WeatherDao interface.

companion object: This is a companion object that provides access to database instance.

@Volatile private var INSTANCE: WeatherDatabase? = null: This is a volatile variable for holding the database instance.

fun getDatabase(application: Application): WeatherDatabase: This method returns an instance of the WeatherDatabase class by building a database instance using Room.databaseBuilder(). It uses the application object to get the context.

fun getDatabase(context: Context): WeatherDatabase: This method is similar to the previous method but accepts a Context object instead of an Application object.

Room.databaseBuilder(): This method creates a new instance of the Room database using the context, database class, and database name. It also specifies a fallback to destructive migration. The also function is used to assign the database instance to the INSTANCE variable.
 */
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

        fun getDatabase(context: Context): WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
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
