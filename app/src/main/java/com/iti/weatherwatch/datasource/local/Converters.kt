package com.iti.weatherwatch.datasource.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.iti.weatherwatch.datasource.model.*

/*
This is the Converters class used in Room Database for converting custom data types into a form that can be stored in the database.

The class defines several functions with the @TypeConverter annotation. These functions take custom data types as arguments and convert them to and from their JSON representation using the Gson library.

For example, the currentToJson function takes a Current object as an argument and converts it to a JSON string using Gson. The jsonToCurrent function takes a JSON string and converts it back to a Current object.

Similarly, the hourlyListToJson function takes a list of Hourly objects and converts it to a JSON string, while the jsonToHourlyList function takes a JSON string and converts it back to a list of Hourly objects.

There are similar functions for Daily, Weather, and Alerts custom data types. These functions enable Room to store and retrieve these custom data types in the database.
 */
class Converters {
    @TypeConverter
    fun currentToJson(current: Current?) = Gson().toJson(current)

    @TypeConverter
    fun jsonToCurrent(currentString: String) =
        Gson().fromJson(currentString, Current::class.java)

    @TypeConverter
    fun hourlyListToJson(hourlyList: List<Hourly>?) = Gson().toJson(hourlyList)

    @TypeConverter
    fun jsonToHourlyList(hourlyString: String) =
        Gson().fromJson(hourlyString, Array<Hourly>::class.java)?.toList()

    @TypeConverter
    fun dailyListToJson(dailyList: List<Daily>) = Gson().toJson(dailyList)

    @TypeConverter
    fun jsonToDailyList(dailyString: String) =
        Gson().fromJson(dailyString, Array<Daily>::class.java).toList()

    @TypeConverter
    fun weatherListToJson(weatherList: List<Weather>) = Gson().toJson(weatherList)

    @TypeConverter
    fun jsonToWeatherList(weatherString: String) =
        Gson().fromJson(weatherString, Array<Weather>::class.java).toList()

    @TypeConverter
    fun alertListToJson(alertList: List<Alerts>?) = Gson().toJson(alertList)

    @TypeConverter
    fun jsonToAlertList(alertString: String?): List<Alerts>? {
        alertString?.let {
            return Gson().fromJson(alertString, Array<Alerts>::class.java)?.toList()
        }
        return emptyList()
    }
}
