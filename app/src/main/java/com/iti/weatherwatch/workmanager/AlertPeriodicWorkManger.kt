package com.iti.weatherwatch.workmanager

import android.content.Context
import androidx.work.*
import com.iti.weatherwatch.R
import com.iti.weatherwatch.datasource.WeatherRepository
import com.iti.weatherwatch.datasource.model.WeatherAlert
import com.iti.weatherwatch.util.getCurrentLocale
import com.iti.weatherwatch.util.getSharedPreferences
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AlertPeriodicWorkManger(private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    val repository = WeatherRepository.getRepository(context)

    override suspend fun doWork(): Result {
        if (!isStopped) {
            val data = inputData
            val id = data.getLong("id", 0)
            getCurrentData(id.toInt())
        }
        return Result.success()
    }

    private suspend fun getCurrentData(id: Int) {
        val currentWeather = repository.getCurrentWeatherFromLocalDataSource()
        val alert = repository.getAlert(id)
        if (checkTime(alert)) {
            val delay: Long = getDelay(alert)
            if (currentWeather.alerts.isNullOrEmpty()) {
                setOneTimeWorkManger(
                    delay,
                    alert.id,
                    currentWeather.current.weather[0].description,
                    currentWeather.current.weather[0].icon
                )
            } else {
                setOneTimeWorkManger(
                    delay,
                    alert.id,
                    currentWeather.alerts!![0].tags[0],
                    currentWeather.current.weather[0].icon
                )
            }
        } else {
            repository.deleteAlert(id)
            WorkManager.getInstance().cancelAllWorkByTag("$id")
        }
    }

    private fun setOneTimeWorkManger(delay: Long, id: Int?, description: String, icon: String) {
        val data = Data.Builder()
        data.putString("description", description)
        data.putString("icon", icon)
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(
            AlertOneTimeWorkManger::class.java,
        )
            .setInitialDelay(delay, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "$id",
            ExistingWorkPolicy.REPLACE,
            oneTimeWorkRequest
        )
    }

    private fun getDelay(alert: WeatherAlert): Long {
        val hour =
            TimeUnit.HOURS.toSeconds(Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toLong())
        val minute =
            TimeUnit.MINUTES.toSeconds(Calendar.getInstance().get(Calendar.MINUTE).toLong())
        return alert.startTime - ((hour + minute) - (2 * 3600L))
    }

    private fun checkTime(alert: WeatherAlert): Boolean {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val date = "$day/${month + 1}/$year"
        val dayNow = getDateMillis(date)
        return dayNow >= alert.startDate && dayNow <= alert.endDate
    }

    private fun getDateMillis(date: String): Long {
        val language = getSharedPreferences(context).getString(
            context.getString(R.string.languageSetting),
            getCurrentLocale(context)?.language
        )
        val f = SimpleDateFormat("dd/MM/yyyy", Locale(language!!))
        val d: Date = f.parse(date)
        return d.time
    }

}
