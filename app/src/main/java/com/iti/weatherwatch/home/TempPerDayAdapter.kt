package com.iti.weatherwatch.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.weatherwatch.R
import com.iti.weatherwatch.databinding.TempPerDayCardBinding

class TempPerDayAdapter(private val context: Context) :
    RecyclerView.Adapter<TempPerDayAdapter.ViewHolder>() {

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
        holder.binding.imageCardDayIcon.setImageResource(R.drawable.clouds)
        holder.binding.textCardDay.text = "Monday"
        holder.binding.textCardDayTempDescription.text = "Sunny"
        holder.binding.textCardDayTemp.text = "32*c"
    }

    override fun getItemCount(): Int {
        return 7
    }
}
