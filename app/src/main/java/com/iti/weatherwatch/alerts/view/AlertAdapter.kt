package com.iti.weatherwatch.alerts.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import com.iti.weatherwatch.R
import com.iti.weatherwatch.alerts.viewmodel.AlertsViewModel
import com.iti.weatherwatch.databinding.AlertCardBinding
import com.iti.weatherwatch.datasource.model.WeatherAlert
import com.iti.weatherwatch.util.*

/*
This is a Kotlin class called AlertAdapter which is used to display a list of weather alerts using a RecyclerView in an Android app.

The class has two parameters: context and viewModel, which are the context of the calling activity or fragment and the view model used to handle interactions with the data source, respectively.

The class contains a ViewHolder class, which is used to hold the views for each item in the RecyclerView. It also has a property called alertsList which is a list of WeatherAlert objects to be displayed.

The class implements three methods:

onCreateViewHolder() - which creates a new ViewHolder and returns it.
onBindViewHolder() - which binds the data to the ViewHolder at the specified position.
getItemCount() - which returns the number of items in the alertsList.
The onBindViewHolder() method uses the WeatherAlert object at the current position to populate the data for each view in the ViewHolder. It also contains a listener for the delete button, which deletes the selected alert and cancels any associated work with it.
 */
class AlertAdapter(private val context: Context, private val viewModel: AlertsViewModel) :
    RecyclerView.Adapter<AlertAdapter.ViewHolder>() {

    var alertsList: List<WeatherAlert> = emptyList()

    class ViewHolder(val binding: AlertCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AlertCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val language: String = getSharedPreferences(context).getString(
            context.getString(R.string.languageSetting),
            getCurrentLocale(context)?.language
        )!!
        val alert = alertsList[position]
        holder.binding.btnDelete.setOnClickListener {
            viewModel.deleteFavoriteWeather(alert.id!!)
            WorkManager.getInstance().cancelAllWorkByTag("${alert.id}")
        }
        holder.binding.textFrom.text =
            convertLongToDayDate(alert.startDate, language).plus(" ").plus(
                convertLongToTime(alert.startTime, language)
            )

        holder.binding.textTo.text = convertLongToDayDate(alert.endDate, language).plus(" ")
            .plus(convertLongToTime(alert.endTime, language))
    }

    override fun getItemCount(): Int {
        return alertsList.size
    }
}
