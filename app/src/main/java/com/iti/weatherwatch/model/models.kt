package com.iti.weatherwatch.model

import com.google.gson.annotations.SerializedName

data class OpenWeatherApi(
    var lat: Double? = null,
    var lon: Double? = null,
    var timezone: String? = null,
    var timezoneOffset: Int? = null,
    var current: Current? = Current(),
    var hourly: ArrayList<Hourly> = arrayListOf(),
    var daily: ArrayList<Daily> = arrayListOf(),
    var alerts: ArrayList<Alerts> = arrayListOf()
)

data class Weather(
    var id: Int? = null,
    var main: String? = null,
    var description: String? = null,
    var icon: String? = null
)

data class Current(
    var dt: Int? = null,
    var sunrise: Int? = null,
    var sunset: Int? = null,
    var temp: Double? = null,
    var feelsLike: Double? = null,
    var pressure: Int? = null,
    var humidity: Int? = null,
    var dewPoint: Double? = null,
    var uvi: Double? = null,
    var clouds: Int? = null,
    var visibility: Int? = null,
    var windSpeed: Double? = null,
    var windDeg: Int? = null,
    var windGust: Double? = null,
    var weather: ArrayList<Weather> = arrayListOf()
)

data class Hourly(
    @SerializedName("dt") var dt: Int? = null,
    @SerializedName("temp") var temp: Double? = null,
    @SerializedName("feels_like") var feelsLike: Double? = null,
    @SerializedName("pressure") var pressure: Int? = null,
    @SerializedName("humidity") var humidity: Int? = null,
    @SerializedName("dew_point") var dewPoint: Double? = null,
    @SerializedName("uvi") var uvi: Double? = null,
    @SerializedName("clouds") var clouds: Int? = null,
    @SerializedName("visibility") var visibility: Int? = null,
    @SerializedName("wind_speed") var windSpeed: Double? = null,
    @SerializedName("wind_deg") var windDeg: Int? = null,
    @SerializedName("wind_gust") var windGust: Double? = null,
    @SerializedName("weather") var weather: ArrayList<Weather> = arrayListOf(),
    @SerializedName("pop") var pop: Double? = null
)

data class Daily(
    @SerializedName("dt") var dt: Int? = null,
    @SerializedName("sunrise") var sunrise: Int? = null,
    @SerializedName("sunset") var sunset: Int? = null,
    @SerializedName("moonrise") var moonrise: Int? = null,
    @SerializedName("moonset") var moonset: Int? = null,
    @SerializedName("moon_phase") var moonPhase: Double? = null,
    @SerializedName("temp") var temp: Temp? = Temp(),
    @SerializedName("feels_like") var feelsLike: FeelsLike? = FeelsLike(),
    @SerializedName("pressure") var pressure: Int? = null,
    @SerializedName("humidity") var humidity: Int? = null,
    @SerializedName("dew_point") var dewPoint: Double? = null,
    @SerializedName("wind_speed") var windSpeed: Double? = null,
    @SerializedName("wind_deg") var windDeg: Int? = null,
    @SerializedName("wind_gust") var windGust: Double? = null,
    @SerializedName("weather") var weather: ArrayList<Weather> = arrayListOf(),
    @SerializedName("clouds") var clouds: Int? = null,
    @SerializedName("pop") var pop: Double? = null,
    @SerializedName("uvi") var uvi: Double? = null
)

data class Alerts(
    @SerializedName("sender_name") var senderName: String? = null,
    @SerializedName("event") var event: String? = null,
    @SerializedName("start") var start: Long? = null,
    @SerializedName("end") var end: Long? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("tags") var tags: ArrayList<String> = arrayListOf()

)

data class Temp(
    @SerializedName("day") var day: Double? = null,
    @SerializedName("min") var min: Double? = null,
    @SerializedName("max") var max: Double? = null,
    @SerializedName("night") var night: Double? = null,
    @SerializedName("eve") var eve: Double? = null,
    @SerializedName("morn") var morn: Double? = null
)

data class FeelsLike(
    @SerializedName("day") var day: Double? = null,
    @SerializedName("night") var night: Double? = null,
    @SerializedName("eve") var eve: Double? = null,
    @SerializedName("morn") var morn: Double? = null
)
