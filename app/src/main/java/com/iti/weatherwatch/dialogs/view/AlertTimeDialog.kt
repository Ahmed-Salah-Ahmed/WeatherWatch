package com.iti.weatherwatch.dialogs.view

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.iti.weatherwatch.R
import com.iti.weatherwatch.databinding.AlertTimeDialogFragmentBinding
import com.iti.weatherwatch.datasource.WeatherRepository
import com.iti.weatherwatch.dialogs.viewmodel.AlertTimeDialogViewModel
import com.iti.weatherwatch.dialogs.viewmodel.AlertTimeViewModelFactory
import com.iti.weatherwatch.model.WeatherAlert
import com.iti.weatherwatch.util.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AlertTimeDialog : DialogFragment() {

    private val viewModel: AlertTimeDialogViewModel by viewModels {
        AlertTimeViewModelFactory(WeatherRepository.getRepository(requireActivity().application))
    }

    private lateinit var binding: AlertTimeDialogFragmentBinding
    private lateinit var language: String
    private lateinit var weatherAlert: WeatherAlert

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner)
        binding = AlertTimeDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        language = getSharedPreferences(requireContext()).getString(
            getString(R.string.languageSetting),
            getCurrentLocale(requireContext())?.language
        )!!
        setCardsInitialText()

        binding.btnFrom.setOnClickListener {
            showDatePicker(true)
        }

        binding.btnTo.setOnClickListener {
            showDatePicker(false)
        }

        binding.btnSave.setOnClickListener {
            viewModel.insertAlert(weatherAlert)
            dialog!!.dismiss()
        }
    }

    private fun setCardsInitialText() {
        val current = Calendar.getInstance().timeInMillis
        val timeNow = convertLongToTime(current / 1000L, language)
        val currentDate = convertLongToDayDate(current, language)
        val oneHour = 3600000L
        val afterOneHour = current + oneHour
        val timeAfterOneHour = convertLongToTime(afterOneHour / 1000L, language)
        weatherAlert = WeatherAlert(null, current / 1000, afterOneHour / 1000, current, current)
        binding.btnFrom.text = currentDate.plus("\n").plus(timeNow)
        binding.btnTo.text = currentDate.plus("\n").plus(timeAfterOneHour)
    }

    private fun showTimePicker(isFrom: Boolean, datePicker: Long) {
        Locale.setDefault(Locale(language))
        val currentHour = Calendar.HOUR_OF_DAY
        val currentMinute = Calendar.MINUTE
        val listener: (TimePicker?, Int, Int) -> Unit =
            { _: TimePicker?, hour: Int, minute: Int ->
                val time = TimeUnit.MINUTES.toSeconds(minute.toLong()) +
                        TimeUnit.HOURS.toSeconds(hour.toLong()) - (3600L * 2)
                val dateString = convertLongToDayDate(datePicker, language)
                val timeString = convertLongToTime(time, language)
                val text = dateString.plus("\n").plus(timeString)
                if (isFrom) {
                    weatherAlert.startTime = time
                    Log.i("yoka", "startTime: $time")
                    weatherAlert.startDate = datePicker
                    binding.btnFrom.text = text
                } else {
                    weatherAlert.endTime = time
                    Log.i("yoka", "endTime: $time")
                    weatherAlert.endDate = datePicker
                    binding.btnTo.text = text
                }
            }

        val timePickerDialog = TimePickerDialog(
            requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
            listener, currentHour, currentMinute, false
        )

        timePickerDialog.setTitle(getString(R.string.time_picker))
        timePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        timePickerDialog.show()
    }

    private fun showDatePicker(isFrom: Boolean) {
        Locale.setDefault(Locale(language))
        val myCalender = Calendar.getInstance()
        val year = myCalender[Calendar.YEAR]
        val month = myCalender[Calendar.MONTH]
        val day = myCalender[Calendar.DAY_OF_MONTH]
        val myDateListener =
            OnDateSetListener { view, year, month, day ->
                if (view.isShown) {
                    val date = "$day/${month + 1}/$year"
                    showTimePicker(isFrom, getDateMillis(date))
                }
            }
        val datePickerDialog = DatePickerDialog(
            requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
            myDateListener, year, month, day
        )
        datePickerDialog.setTitle(getString(R.string.date_picker))
        datePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        datePickerDialog.show()
    }

    private fun getDateMillis(date: String): Long {
        val f = SimpleDateFormat("dd/MM/yyyy", Locale(language))
        val d: Date = f.parse(date)
        return d.time
    }

    override fun onStart() {
        super.onStart()
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

}
