package com.iti.weatherwatch.workmanager

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startForegroundService
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.iti.weatherwatch.service.AlertService

/*
This is a Kotlin file that defines a one-time work manager class named AlertOneTimeWorkManger. This class extends the CoroutineWorker class, which is a subclass of the ListenableWorker class. This class is used to create a work request to execute the doWork() method in a background thread.

The doWork() method is the main entry point for the work manager. It is called by the system when the work request is ready to be executed. In this method, the inputData is retrieved and passed to the startAlertService() method, which is used to start the AlertService in a foreground mode. Finally, the Result.success() is returned to indicate that the work has been completed successfully.

The startAlertService() method is used to create an Intent object that contains the description and icon as extras, which are used to pass data to the AlertService. The startForegroundService() method is used to start the AlertService in a foreground mode, which is used to display a notification to the user.
 */
class AlertOneTimeWorkManger(private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val description = inputData.getString("description")!!
        val icon = inputData.getString("icon")!!
        startAlertService(description, icon)
        return Result.success()
    }

    private fun startAlertService(description: String, icon: String) {
        val intent = Intent(applicationContext, AlertService::class.java)
        intent.putExtra("description", description)
        intent.putExtra("icon", icon)
        startForegroundService(applicationContext, intent)
    }
}
