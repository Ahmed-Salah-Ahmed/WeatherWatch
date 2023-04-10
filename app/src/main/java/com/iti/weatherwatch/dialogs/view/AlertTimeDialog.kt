package com.iti.weatherwatch.dialogs.view

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.work.*
import com.iti.weatherwatch.R
import com.iti.weatherwatch.databinding.AlertTimeDialogFragmentBinding
import com.iti.weatherwatch.datasource.WeatherRepository
import com.iti.weatherwatch.datasource.model.WeatherAlert
import com.iti.weatherwatch.dialogs.viewmodel.AlertTimeDialogViewModel
import com.iti.weatherwatch.dialogs.viewmodel.AlertTimeViewModelFactory
import com.iti.weatherwatch.util.*
import com.iti.weatherwatch.workmanager.AlertPeriodicWorkManger
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/*
This is a Kotlin class for an Android app that displays a dialog box for setting up a weather alert. It uses a ViewModel to retrieve the alert information from the database and update it accordingly. The class imports various Android classes, such as DatePickerDialog and TimePickerDialog, and several other classes specific to the app's implementation.

The AlertTimeDialog class extends the DialogFragment class and overrides several of its lifecycle methods. It also defines several helper methods for showing a time picker or date picker dialog box, as well as for setting up and scheduling a work manager for periodic alert notifications.

In the onCreateView method, the dialog window's background is set, and the AlertTimeDialogFragmentBinding object is inflated and returned.

In the onViewCreated method, the ViewModel is initialized, the initial data is set, and the click listeners for the From, To, Save, and Cancel buttons are set. Additionally, the method to set up the work manager is called.

The setPeriodWorkManger method creates a work request for the AlertPeriodicWorkManager class and schedules it for periodic execution using WorkManager.

The setInitialData method initializes the current time and date, sets the model data, and sets the button text.

The showTimePicker method creates a time picker dialog box for setting the alert start and end times.

The showDatePicker method creates a date picker dialog box for setting the alert start and end dates.
 */
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
        setInitialData()

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

        binding.btnCancel.setOnClickListener {
            dialog!!.dismiss()
        }

        viewModel.id.observe(viewLifecycleOwner) {
            setPeriodWorkManger(it)
        }
    }

    private fun setPeriodWorkManger(id: Long) {

        val data = Data.Builder()
        data.putLong("id", id)

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            AlertPeriodicWorkManger::class.java,
            24, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "$id",
            ExistingPeriodicWorkPolicy.UPDATE,
            periodicWorkRequest
        )
    }

    private fun setInitialData() {
        val rightNow = Calendar.getInstance()
        // init time
        val currentHour = TimeUnit.HOURS.toSeconds(rightNow.get(Calendar.HOUR_OF_DAY).toLong())
        val currentMinute = TimeUnit.MINUTES.toSeconds(rightNow.get(Calendar.MINUTE).toLong())
        val currentTime = (currentHour + currentMinute).minus(3600L * 2)
        val currentTimeText = convertLongToTime((currentTime + 60), language)
        val afterOneHour = currentTime.plus(3600L)
        val afterOneHourText = convertLongToTime(afterOneHour, language)
        // init day
        val year = rightNow.get(Calendar.YEAR)
        val month = rightNow.get(Calendar.MONTH)
        val day = rightNow.get(Calendar.DAY_OF_MONTH)
        val date = "$day/${month + 1}/$year"
        val dayNow = getDateMillis(date)
        val currentDate = convertLongToDayDate(dayNow, language)
        //init model
        weatherAlert =
            WeatherAlert(null, (currentTime + 60), afterOneHour, dayNow, dayNow)
        //init text
        binding.btnFrom.text = currentDate.plus("\n").plus(currentTimeText)
        binding.btnTo.text = currentDate.plus("\n").plus(afterOneHourText)
    }

    private fun showTimePicker(isFrom: Boolean, datePicker: Long) {
        Locale.setDefault(Locale(language))
        val rightNow = Calendar.getInstance()
        val currentHour = rightNow.get(Calendar.HOUR_OF_DAY)
        val currentMinute = rightNow.get(Calendar.MINUTE)
        val listener: (TimePicker?, Int, Int) -> Unit =
            { _: TimePicker?, hour: Int, minute: Int ->
                val time = TimeUnit.MINUTES.toSeconds(minute.toLong()) +
                        TimeUnit.HOURS.toSeconds(hour.toLong()) - (3600L * 2)
                val dateString = convertLongToDayDate(datePicker, language)
                val timeString = convertLongToTime(time, language)
                val text = dateString.plus("\n").plus(timeString)
                if (isFrom) {
                    weatherAlert.startTime = time
                    weatherAlert.startDate = datePicker
                    binding.btnFrom.text = text
                } else {
                    weatherAlert.endTime = time
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
