package com.iti.weatherwatch.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.weatherwatch.R
import com.iti.weatherwatch.databinding.TempPerDayCardBinding
import com.iti.weatherwatch.datasource.model.Daily
import com.iti.weatherwatch.util.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/*
This is a Kotlin class named TempPerDayAdapter, which extends RecyclerView.Adapter to bind daily temperature data to a RecyclerView in a weather app. The ViewHolder class represents a single item view in the RecyclerView.

The class has three properties:

    daily of type List<Daily> that holds daily temperature data.
    temperatureUnit of type String that holds the unit of temperature (Celsius or Fahrenheit).
    language of type String that holds the language selected by the user.

The class has three methods:

    onCreateViewHolder creates a ViewHolder instance by inflating the layout of the item view using the TempPerDayCardBinding class.
    onBindViewHolder binds the daily temperature data to a ViewHolder by setting the appropriate values to the views in the layout.
    getItemCount returns the number of items in the RecyclerView.

The class also has a private method convertLongToDay that takes a timestamp as input and returns the corresponding day in a specific format (EEE, d MMM yyyy) based on the language selected by the user.
*/
class TempPerDayAdapter(private val context: Context) :
    RecyclerView.Adapter<TempPerDayAdapter.ViewHolder>() {

    var daily: List<Daily> = emptyList()
    var temperatureUnit: String = ""
    private lateinit var language: String

    class ViewHolder(val binding: TempPerDayCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TempPerDayCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = daily[position + 1]
        language = getSharedPreferences(context).getString(
            context.getString(R.string.languageSetting),
            "en"
        )!!
        holder.binding.imageCardDayIcon.setImageResource(getIcon(day.weather[0].icon))
        holder.binding.textCardDay.text = convertLongToDay(day.dt)
        holder.binding.textCardDayTempDescription.text = day.weather[0].description
        if (language == "ar") {
            holder.binding.textCardDayTemp.text =
                convertNumbersToArabic(day.temp.day.toInt()).plus(temperatureUnit)
        } else {
            holder.binding.textCardDayTemp.text =
                day.temp.day.toInt().toString().plus(temperatureUnit)
        }
    }

    override fun getItemCount(): Int {
        return daily.size - 1
    }

    private fun convertLongToDay(time: Long): String {
        val date = Date(TimeUnit.SECONDS.toMillis(time))
        val format = SimpleDateFormat("EEE, d MMM yyyy", Locale(language))
        return format.format(date)
    }
}
