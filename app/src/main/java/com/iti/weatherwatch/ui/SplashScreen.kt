package com.iti.weatherwatch.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.iti.weatherwatch.databinding.ActivitySplashScreenBinding
import com.iti.weatherwatch.dialogs.view.InitialSettingDialog
import com.iti.weatherwatch.util.getSharedPreferences
import kotlinx.coroutines.*

/*
This is a Kotlin file for the SplashScreen activity in the WeatherWatch app.

The file starts with importing the necessary libraries and declaring the package name.

Next, the SplashScreen class is defined as an AppCompatActivity and marked with @SuppressLint("CustomSplashScreen") annotation to suppress the warning message for using a custom splash screen.

The class has a parentJob property of type Job that will be used to manage all coroutines started from this activity.

In the onCreate method, the layout is inflated and the isFirstTime function is called to check if the app is opened for the first time. If it is the first time, a dialog box (InitialSettingDialog) will be shown to let the user choose their preferred settings. If not, the startMainScreen function is called after a delay of 1.5 seconds using coroutines.

The onBackPressed function is overridden to call the finish function before the parent method to ensure that the activity is finished before going back to the previous activity.

The startMainScreen function starts the MainActivity and finishes the current activity.

The onStop method is overridden to cancel the parentJob when the activity is stopped to prevent any running coroutines from interfering with the app's behavior.

Finally, the isFirstTime function checks if the app is opened for the first time by getting the value of a boolean stored in the shared preferences.
 */
@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val parentJob = Job()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (isFirstTime()) {
            binding.lottieAnimationView.visibility = View.GONE
            InitialSettingDialog().show(supportFragmentManager, "InitialFragment")
        } else {
            val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)
            coroutineScope.launch {
                delay(1500)
                startMainScreen()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun startMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStop() {
        super.onStop()
        parentJob.cancel()
    }

    private fun isFirstTime(): Boolean {
        return getSharedPreferences(this).getBoolean("firstTime", true)
    }
}
