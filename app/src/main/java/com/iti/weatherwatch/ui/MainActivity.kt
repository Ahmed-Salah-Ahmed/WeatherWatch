package com.iti.weatherwatch.ui

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.iti.weatherwatch.R
import com.iti.weatherwatch.databinding.ActivityMainBinding
import com.iti.weatherwatch.util.getCurrentLocale
import com.iti.weatherwatch.util.getSharedPreferences
import java.util.*

/*
This is the implementation of the MainActivity class in an Android application. This class extends AppCompatActivity and provides the main UI for the Weather Watch application.

In the onCreate method, the language is set based on the user's preference stored in shared preferences, and the ActivityMainBinding object is inflated and set as the content view for the activity. The bottom navigation view is also initialized with the navigation controller, and a listener is added to hide the navigation view when navigating to certain destinations.

The setLocale method sets the locale for the app, based on the selected language. It updates the configuration object to reflect the new locale and updates the resources to use the updated configuration.

The onBackPressed method checks if the current destination is the home screen and finishes the activity if it is. Otherwise, the default behavior of onBackPressed is called.

Overall, this class provides the main UI for the Weather Watch app and sets the locale based on the user's preference.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val local = getCurrentLocale(this)
        val language = getSharedPreferences(this).getString(
            getString(R.string.languageSetting),
            local?.language
        ) ?: local?.language
        setLocale(language!!)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            if (nd.id == R.id.navigation_home || nd.id == R.id.navigation_favorites
                || nd.id == R.id.navigation_notifications) {
                navView.visibility = View.VISIBLE
            } else {
                navView.visibility = View.GONE
            }
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_favorites, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)*/
        navView.setupWithNavController(navController)
    }

    private fun setLocale(lang: String) {
        val myLocale = Locale(lang)
        Locale.setDefault(myLocale)
        val res: Resources = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.locale = myLocale
        conf.setLayoutDirection(myLocale)
        res.updateConfiguration(conf, dm)
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.navigation_home) {
            finish()
        }
        super.onBackPressed()
    }

}
