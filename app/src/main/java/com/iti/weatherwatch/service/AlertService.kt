package com.iti.weatherwatch.service

import android.app.*
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.IBinder
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.iti.weatherwatch.R
import com.iti.weatherwatch.ui.MainActivity
import com.iti.weatherwatch.util.getIcon
import com.iti.weatherwatch.workmanager.AlertWindowManger

/*
This is a Kotlin file that defines an Android service called AlertService. The service starts a foreground notification that displays weather information and creates a window that overlays on top of other apps. It also sets the vibration, sound, and lights for the notification.

The AlertService class extends the Service class, which is a base class for Android services. The onStartCommand method is called when the service is started. It receives an Intent object that contains the weather information, including the description and icon. The method creates a notification channel and starts the foreground notification using the makeNotification method. If the app has permission to draw overlays, the method also starts a window work manager.

The makeNotification method creates a notification using the NotificationCompat.Builder class. It sets the small and large icon, title, description, and vibration, sound, and lights for the notification.

The notificationChannel method creates a notification channel that specifies the name, description, and importance of the notification. It also sets the sound and vibration attributes for the channel.
 */
class AlertService : Service() {

    private val CHANNEL_ID = 14
    private val FOREGROUND_ID = 7
    private var notificationManager: NotificationManager? = null
    var alertWindowManger: AlertWindowManger? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val description = intent?.getStringExtra("description")
        val icon = intent?.getStringExtra("icon")
        notificationChannel()
        startForeground(FOREGROUND_ID, makeNotification(description!!, icon!!))
        //start window workmanager
        if (Settings.canDrawOverlays(this)) {
            alertWindowManger = AlertWindowManger(this, description, icon)
            alertWindowManger!!.setMyWindowManger()
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun makeNotification(description: String, icon: String): Notification {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val bitmap = BitmapFactory.decodeResource(resources, getIcon(icon))

        return NotificationCompat.Builder(
            applicationContext, "$CHANNEL_ID"
        )
            .setSmallIcon(getIcon(icon))
            .setContentText(description)
            .setContentTitle("Weather Alarm")
            .setLargeIcon(bitmap)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(description)
            )
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setLights(Color.RED, 3000, 3000)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setAutoCancel(true)
            .build()
    }

    private fun notificationChannel() {
        val name: CharSequence = getString(R.string.channel_name)
        val description = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
            "$CHANNEL_ID",
            name, importance
        )
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()
        channel.enableVibration(true)
        channel.setSound(sound, attributes)
        channel.description = description
        notificationManager = this.getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)
    }
}
