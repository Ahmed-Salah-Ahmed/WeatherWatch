package com.iti.weatherwatch.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.weatherwatch.databinding.TempPerTimeCardBinding
import com.iti.weatherwatch.model.Hourly
import com.iti.weatherwatch.util.convertLongToTime
import com.iti.weatherwatch.util.getIcon

class TempPerTimeAdapter(private val context: Context) :
    RecyclerView.Adapter<TempPerTimeAdapter.ViewHolder>() {

    var hourly: List<Hourly> = emptyList()

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
        val hour = hourly[position + 1]

        holder.binding.imageCardTempIcon.setImageResource(getIcon(hour.weather[0].icon))
        holder.binding.textCardTemp.text = "${hour.temp}"
        holder.binding.textCardTime.text = convertLongToTime(hour.dt)
    }

    override fun getItemCount(): Int {
        return hourly.size - 1
    }
}
