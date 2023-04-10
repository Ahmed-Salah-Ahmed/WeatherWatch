package com.iti.weatherwatch.datasource

import android.app.Application
import android.content.Context
import com.iti.weatherwatch.datasource.local.*
import com.iti.weatherwatch.datasource.model.OpenWeatherApi
import com.iti.weatherwatch.datasource.model.WeatherAlert
import com.iti.weatherwatch.datasource.remote.RemoteSource
import com.iti.weatherwatch.datasource.remote.RetrofitHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

/*
This class is a WeatherRepository that acts as an intermediate between the app and different data sources, local and remote. It implements the IWeatherRepository interface, which defines a set of methods to interact with the data sources.

The class has a constructor that takes two parameters of types RemoteSource and LocalSource, which are used to specify the remote and local data sources. It also has an optional parameter ioDispatcher of type CoroutineDispatcher to specify the dispatcher for executing asynchronous operations.

The class has two companion object methods named getRepository that returns an instance of the WeatherRepository. The first method takes an Application parameter and the second method takes a Context parameter.

The class implements the methods of the IWeatherRepository interface to perform operations like fetching weather data from remote and local sources, inserting, updating and deleting weather data, and fetching alerts.

The class uses the Retrofit library to make network calls to the remote data source and uses the Room library for the local data source. The class also uses Kotlin coroutines to perform asynchronous operations.
 */
class WeatherRepository(
    private val weatherRemoteDataSource: RemoteSource,
    private val weatherLocalDataSource: LocalSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IWeatherRepository {

    companion object {
        @Volatile
        private var INSTANCE: WeatherRepository? = null

        fun getRepository(app: Application): WeatherRepository {
            return INSTANCE ?: synchronized(this) {
                WeatherRepository(
                    RetrofitHelper,
                    RoomLocalClass(WeatherDatabase.getDatabase(app).weatherDao())
                ).also {
                    INSTANCE = it
                }
            }
        }

        fun getRepository(context: Context): WeatherRepository {
            return INSTANCE ?: synchronized(this) {
                WeatherRepository(
                    RetrofitHelper,
                    RoomLocalClass(WeatherDatabase.getDatabase(context).weatherDao())
                ).also {
                    INSTANCE = it
                }
            }
        }
    }

    override suspend fun insertFavoriteWeatherFromRemoteToLocal(
        lat: String,
        long: String,
        language: String,
        units: String
    ) {
        val remoteWeather = weatherRemoteDataSource.getCurrentWeather(lat, long, language, units)
        if (remoteWeather.isSuccessful) {
            remoteWeather.body()?.let {
                it.isFavorite = true
                weatherLocalDataSource.insertCurrentWeather(it)
            }
        } else {
            throw Exception("${remoteWeather.errorBody()}")
        }
    }

    override suspend fun insertCurrentWeatherFromRemoteToLocal(
        lat: String,
        long: String,
        language: String,
        units: String
    ): OpenWeatherApi {
        val remoteWeather = weatherRemoteDataSource.getCurrentWeather(lat, long, language, units)
        return if (remoteWeather.isSuccessful) {
            remoteWeather.body()?.let {
                deleteWeathersFromLocalDataSource()
                it.isFavorite = false
                weatherLocalDataSource.insertCurrentWeather(it)
            }
            remoteWeather.body()!!
        } else {
            throw Exception("${remoteWeather.errorBody()}")
        }
    }

    override fun getCurrentWeatherFromLocalDataSource(): OpenWeatherApi {
        return weatherLocalDataSource.getCurrentWeather()
    }

    override suspend fun deleteWeathersFromLocalDataSource() {
        weatherLocalDataSource.deleteWeathers()
    }

    override fun getFavoritesWeatherFromLocalDataSource(): Flow<List<OpenWeatherApi>> {
        return weatherLocalDataSource.getFavoritesWeather()
    }

    override suspend fun deleteFavoriteWeather(id: Int) {
        weatherLocalDataSource.deleteFavoriteWeather(id)
    }

    override fun getFavoriteWeather(id: Int): OpenWeatherApi {
        return weatherLocalDataSource.getFavoriteWeather(id)
    }

    override suspend fun updateWeather(weather: OpenWeatherApi) {
        weatherLocalDataSource.updateWeather(weather)
    }

    override suspend fun updateFavoriteWeather(
        latitude: String,
        longitude: String,
        units: String,
        language: String,
        id: Int
    ): OpenWeatherApi {
        val response =
            weatherRemoteDataSource.getCurrentWeather(latitude, longitude, language, units)
        if (response.isSuccessful) {
            response.body()?.let {
                it.id = id
                it.isFavorite = true
                updateWeather(it)
            }
            return getFavoriteWeather(id)
        } else {
            throw Exception("${response.errorBody()}")
        }
    }

    override suspend fun insertAlert(alert: WeatherAlert): Long {
        return weatherLocalDataSource.insertAlert(alert)
    }

    override fun getAlertsList(): Flow<List<WeatherAlert>> {
        return weatherLocalDataSource.getAlertsList()
    }

    override suspend fun deleteAlert(id: Int) {
        weatherLocalDataSource.deleteAlert(id)
    }

    override fun getAlert(id: Int): WeatherAlert {
        return weatherLocalDataSource.getAlert(id)
    }

}
