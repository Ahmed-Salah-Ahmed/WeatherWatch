package com.iti.weatherwatch

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.iti.weatherwatch.dialogs.view.InitialSettingDialog
import com.iti.weatherwatch.util.getSharedPreferences
import kotlinx.coroutines.*

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private val parentJob = Job()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        if (isFirstTime()) {
            InitialSettingDialog().show(supportFragmentManager, "InitialFragment")
        } else {
            val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)
            coroutineScope.launch {
                delay(4000)
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
