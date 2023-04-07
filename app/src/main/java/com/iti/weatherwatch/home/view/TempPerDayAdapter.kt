package com.iti.weatherwatch.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.weatherwatch.R
import com.iti.weatherwatch.databinding.TempPerDayCardBinding
import com.iti.weatherwatch.model.Daily
import com.iti.weatherwatch.util.getIcon
import com.iti.weatherwatch.util.getSharedPreferences
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

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
        holder.binding.textCardDayTemp.text = day.temp.day.toString().plus(temperatureUnit)
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
