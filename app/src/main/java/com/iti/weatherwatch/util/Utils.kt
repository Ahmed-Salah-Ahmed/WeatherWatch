package com.iti.weatherwatch.util

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.iti.weatherwatch.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun getIcon(imageString: String): Int {
    var imageInInteger: Int
    when (imageString) {
        "01d" -> imageInInteger = R.drawable.icon_01d
        "01n" -> imageInInteger = R.drawable.icon_01n
        "02d" -> imageInInteger = R.drawable.icon_02d
        "02n" -> imageInInteger = R.drawable.icon_02n
        "03n" -> imageInInteger = R.drawable.icon_03n
        "03d" -> imageInInteger = R.drawable.icon_03d
        "04d" -> imageInInteger = R.drawable.icon_04d
        "04n" -> imageInInteger = R.drawable.icon_04n
        "09d" -> imageInInteger = R.drawable.icon_09d
        "09n" -> imageInInteger = R.drawable.icon_09n
        "10d" -> imageInInteger = R.drawable.icon_10d
        "10n" -> imageInInteger = R.drawable.icon_10n
        "11d" -> imageInInteger = R.drawable.icon_11d
        "11n" -> imageInInteger = R.drawable.icon_11n
        "13d" -> imageInInteger = R.drawable.icon_13d
        "13n" -> imageInInteger = R.drawable.icon_13n
        "50d" -> imageInInteger = R.drawable.icon_50d
        "50n" -> imageInInteger = R.drawable.icon_50n
        else  -> imageInInteger = R.drawable.clouds
    }
    return imageInInteger
}

fun convertLongToTime(time: Long): String {
    val date = Date(TimeUnit.SECONDS.toMillis(time))
    val format = SimpleDateFormat("h:mm a")
    return format.format(date)
}

fun convertCalenderToDayString(calendar: Calendar): String {
    return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH)
}

fun convertCalenderToDayDate(calendar: Calendar): String {
    val date = Date(calendar.timeInMillis)
    val format = SimpleDateFormat("d MMM, yyyy")
    return format.format(date)
}

fun getSharedPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences(
        context.getString(R.string.shared_pref),
        Context.MODE_PRIVATE
    )
}

fun isSharedPreferencesLocationAndTimeZoneNull(context: Context): Boolean {
    val myPref = getSharedPreferences(context)
    val location = myPref.getString(context.getString(R.string.location), null)
    val timeZone = myPref.getString(context.getString(R.string.timeZone), null)
    return location.isNullOrEmpty() && timeZone.isNullOrEmpty()
}

fun isSharedPreferencesLatAndLongNull(context: Context): Boolean {
    val myPref = getSharedPreferences(context)
    val lat = myPref.getFloat(context.getString(R.string.lat), 0.0f)
    val long = myPref.getFloat(context.getString(R.string.lon), 0.0f)
    return lat == 0.0f && long == 0.0f
}

fun updateSharedPreferences(
    context: Context,
    lat: Double,
    long: Double,
    location: String,
    timeZone: String
) {
    val editor = getSharedPreferences(context).edit()
//    editor.clear()
    editor.putFloat(context.getString(R.string.lat), lat.toFloat())
    editor.putFloat(context.getString(R.string.lon), long.toFloat())
    editor.putString(context.getString(R.string.location), location)
    editor.putString(context.getString(R.string.timeZone), timeZone)
    editor.apply()
}

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)     -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            return true
        }
    }
    return false
}

fun String.fullTrim() = trim().replace("\uFEFF", "")
