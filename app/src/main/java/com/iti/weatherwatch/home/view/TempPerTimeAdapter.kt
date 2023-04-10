package com.iti.weatherwatch.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.weatherwatch.R
import com.iti.weatherwatch.databinding.TempPerTimeCardBinding
import com.iti.weatherwatch.datasource.model.Hourly
import com.iti.weatherwatch.util.*

/*
This code defines a TempPerTimeAdapter class which is a subclass of RecyclerView.Adapter used to populate a RecyclerView with temperature data for each hour.

The TempPerTimeAdapter class has a ViewHolder subclass, an hourly list of type Hourly, and a temperatureUnit string.

The onCreateViewHolder() method inflates the layout file for each item in the RecyclerView.

The onBindViewHolder() method binds the temperature data for each hour to the corresponding item view.

The getItemCount() method returns the size of the hourly list, which is always 24 hours.
 */
class TempPerTimeAdapter(private val context: Context) :
    RecyclerView.Adapter<TempPerTimeAdapter.ViewHolder>() {

    var hourly: List<Hourly> = emptyList()
    var temperatureUnit: String = ""
    private lateinit var language: String

    class ViewHolder(val binding: TempPerTimeCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TempPerTimeCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        language = getSharedPreferences(context).getString(
            context.getString(
                R.string.languageSetting
            ), "en"
        )!!
        val hour = hourly[position + 1]
        holder.binding.imageCardTempIcon.setImageResource(getIcon(hour.weather[0].icon))
        if (language == "ar") {
            holder.binding.textCardTemp.text =
                convertNumbersToArabic(hour.temp.toInt()).plus(temperatureUnit)
        } else {
            holder.binding.textCardTemp.text = "${hour.temp.toInt()}".plus(temperatureUnit)
        }
        holder.binding.textCardTime.text = convertLongToTime(hour.dt, language).lowercase()
    }

    override fun getItemCount(): Int {
        var size = 0
        if (hourly.isNotEmpty()) {
            size = 24
        }
        return size
    }

}
