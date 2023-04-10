package com.iti.weatherwatch.workmanager

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.util.Log
import android.view.*
import com.iti.weatherwatch.R
import com.iti.weatherwatch.databinding.AlertWindowMangerBinding
import com.iti.weatherwatch.service.AlertService
import com.iti.weatherwatch.util.getIcon

/*
This code defines a class AlertWindowManger that is responsible for creating and managing a custom alert window in an Android application. The window is displayed as an overlay on top of the current screen and contains an icon and a description text.

The class takes three parameters in its constructor: a Context object, a String representing the description text, and a String representing the icon resource name. It defines a few private variables to hold references to the WindowManager, the custom view that will be displayed in the window, and the AlertWindowMangerBinding object that binds the view to the layout file.

The setMyWindowManger method inflates the custom view from the layout file, sets up the WindowManager, and adds the view to the window. The view's dimensions are set to 85% of the screen width, and its layout flags ensure that it stays on top of other windows and does not go to sleep.

The bindView method sets the icon and description text in the view, as well as the text for the "OK" button that closes the window. It also sets an OnClickListener for the "OK" button that calls the close method and stops an associated service called AlertService.

The close method removes the view from the window and releases its resources. The stopMyService method stops the associated AlertService using an intent.

Overall, this class provides a simple way to display a custom alert window in an Android application that can be easily dismissed by the user.
 */
class AlertWindowManger(
    private val context: Context,
    private val description: String,
    private val icon: String
) {

    private var windowManager: WindowManager? = null
    private var customNotificationDialogView: View? = null
    private var binding: AlertWindowMangerBinding? = null

    fun setMyWindowManger() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        customNotificationDialogView =
            inflater.inflate(R.layout.alert_window_manger, null)
        binding = AlertWindowMangerBinding.bind(customNotificationDialogView!!)
        bindView()
        val LAYOUT_FLAG: Int = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val width = (context.resources.displayMetrics.widthPixels * 0.85).toInt()
        val params = WindowManager.LayoutParams(
            width,
            WindowManager.LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE,
            PixelFormat.TRANSLUCENT
        )
        windowManager!!.addView(customNotificationDialogView, params)
    }

    private fun bindView() {
        binding?.imageIcon?.setImageResource(getIcon(icon))
        binding?.textDescription?.text = description
        binding?.btnOk?.text = context.getString(R.string.btn_ok)
        binding?.btnOk?.setOnClickListener {
            close()
            stopMyService()
        }
    }

    private fun close() {
        try {
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).removeView(
                customNotificationDialogView
            )
            customNotificationDialogView!!.invalidate()
            (customNotificationDialogView!!.parent as ViewGroup).removeAllViews()
        } catch (e: Exception) {
            Log.d("Error in Alert Window Manager", e.toString())
        }
    }

    private fun stopMyService() {
        context.stopService(Intent(context, AlertService::class.java))
    }
}
