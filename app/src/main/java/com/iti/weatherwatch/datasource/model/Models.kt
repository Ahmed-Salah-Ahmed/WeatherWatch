package com.iti.weatherwatch.datasource.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/*
This class represents data models used for weather-related data. It includes the following data classes:

OpenWeatherApi: This is the main data class representing weather data from OpenWeatherAPI. It contains various properties related to weather such as latitude, longitude, timezone, current weather, hourly weather, daily weather, and alerts. It is annotated with @Entity to use Room database and has a tableName property set to "weather". It also has a data modifier to create a data class and includes an id property annotated with @PrimaryKey to uniquely identify each instance of the class.

Weather: This data class represents the basic information about the weather, such as the weather's main description, brief description, and icon.

Current: This data class represents the current weather conditions, including properties such as date and time, sunrise, sunset, temperature, feels like temperature, pressure, humidity, dew point, UV index, clouds, visibility, wind speed, wind degree, wind gust, and a list of weather objects.

Hourly: This data class represents the weather information hour by hour for the next 48 hours, including properties such as date and time, temperature, feels like temperature, pressure, humidity, dew point, UV index, clouds, visibility, wind speed, wind degree, wind gust, probability of precipitation, and a list of weather objects.

Temp: This data class represents the temperature at various times of the day, including properties such as the temperature during the day, the minimum temperature, the maximum temperature, the temperature at night, the temperature in the evening, and the temperature in the morning.

FeelsLike: This data class represents the feels like temperature at various times of the day, including properties such as the feels like temperature during the day, the feels like temperature at night, the feels like temperature in the evening, and the feels like temperature in the morning.

Daily: This data class represents the daily weather forecast for the next 7 days, including properties such as date and time, sunrise, sunset, moonrise, moonset, moon phase, temperature, feels like temperature, pressure, humidity, dew point, wind speed, wind degree, wind gust, probability of precipitation, UV index, and a list of weather objects.

Alerts: This data class represents the weather alerts, including properties such as the name of the sender, the event, the start time, the end time, the description, and a list of tags.

WeatherAlert: This data class represents the weather alert information to be stored in the Room database. It includes properties such as id, startTime, endTime, startDate, and endDate. It is annotated with @Entity to use Room database and has a tableName property set to "alert". It also has a data modifier to create a data class and includes an id property annotated with @PrimaryKey to uniquely identify each instance of the class.
 */
@Entity(tableName = "weather")
data class OpenWeatherApi(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var isFavorite: Boolean = false,
    @SerializedName("lat") var lat: Double,
    @SerializedName("lon") var lon: Double,
    @SerializedName("timezone") var timezone: String,
    @SerializedName("timezone_offset") var timezoneOffset: Int,
    @SerializedName("current") var current: Current,
    @SerializedName("hourly") var hourly: List<Hourly>,
    @SerializedName("daily") var daily: List<Daily>,
    @SerializedName("alerts") var alerts: List<Alerts>?
)

data class Weather(
    @SerializedName("id") var id: Int,
    @SerializedName("main") var main: String,
    @SerializedName("description") var description: String,
    @SerializedName("icon") var icon: String
)

data class Current(
    @SerializedName("dt") var dt: Long,
    @SerializedName("sunrise") var sunrise: Int,
    @SerializedName("sunset") var sunset: Int,
    @SerializedName("temp") var temp: Double,
    @SerializedName("feels_like") var feelsLike: Double,
    @SerializedName("pressure") var pressure: Int,
    @SerializedName("humidity") var humidity: Int,
    @SerializedName("dew_point") var dewPoint: Double,
    @SerializedName("uvi") var uvi: Double,
    @SerializedName("clouds") var clouds: Int,
    @SerializedName("visibility") var visibility: Int,
    @SerializedName("wind_speed") var windSpeed: Double,
    @SerializedName("wind_deg") var windDeg: Int,
    @SerializedName("wind_gust") var windGust: Double,
    @SerializedName("weather") var weather: List<Weather>
)

data class Hourly(

    @SerializedName("dt") var dt: Long,
    @SerializedName("temp") var temp: Double,
    @SerializedName("feels_like") var feelsLike: Double,
    @SerializedName("pressure") var pressure: Int,
    @SerializedName("humidity") var humidity: Int,
    @SerializedName("dew_point") var dewPoint: Double,
    @SerializedName("uvi") var uvi: Double,
    @SerializedName("clouds") var clouds: Int,
    @SerializedName("visibility") var visibility: Int,
    @SerializedName("wind_speed") var windSpeed: Double,
    @SerializedName("wind_deg") var windDeg: Int,
    @SerializedName("wind_gust") var windGust: Double,
    @SerializedName("weather") var weather: List<Weather>,
    @SerializedName("pop") var pop: Double

)

data class Temp(

    @SerializedName("day") var day: Double,
    @SerializedName("min") var min: Double,
    @SerializedName("max") var max: Double,
    @SerializedName("night") var night: Double,
    @SerializedName("eve") var eve: Double,
    @SerializedName("morn") var morn: Double

)

data class FeelsLike(

    @SerializedName("day") var day: Double,
    @SerializedName("night") var night: Double,
    @SerializedName("eve") var eve: Double,
    @SerializedName("morn") var morn: Double

)

data class Daily(

    @SerializedName("dt") var dt: Long,
    @SerializedName("sunrise") var sunrise: Int,
    @SerializedName("sunset") var sunset: Int,
    @SerializedName("moonrise") var moonrise: Int,
    @SerializedName("moonset") var moonset: Int,
    @SerializedName("moon_phase") var moonPhase: Double,
    @SerializedName("temp") var temp: Temp,
    @SerializedName("feels_like") var feelsLike: FeelsLike,
    @SerializedName("pressure") var pressure: Int,
    @SerializedName("humidity") var humidity: Int,
    @SerializedName("dew_point") var dewPoint: Double,
    @SerializedName("wind_speed") var windSpeed: Double,
    @SerializedName("wind_deg") var windDeg: Int,
    @SerializedName("wind_gust") var windGust: Double,
    @SerializedName("weather") var weather: List<Weather>,
    @SerializedName("clouds") var clouds: Int,
    @SerializedName("pop") var pop: Double,
    @SerializedName("uvi") var uvi: Double

)

data class Alerts(

    @SerializedName("sender_name") var senderName: String? = null,
    @SerializedName("event") var event: String? = null,
    @SerializedName("start") var start: Long? = null,
    @SerializedName("end") var end: Long? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("tags") var tags: List<String>

)

@Entity(tableName = "alert")
data class WeatherAlert(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var startTime: Long,
    var endTime: Long,
    var startDate: Long,
    var endDate: Long
)
