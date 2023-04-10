package com.iti.weatherwatch.settings

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iti.weatherwatch.R
import com.iti.weatherwatch.databinding.SettingsFragmentBinding
import com.iti.weatherwatch.ui.MainActivity
import com.iti.weatherwatch.util.getSharedPreferences

/*
This is a Kotlin class that represents a settings screen for an Android weather app. The class extends the Fragment class and overrides some of its methods to handle the fragment's lifecycle. The class uses data binding to bind UI elements in the XML layout file to properties in the class. The class also makes use of shared preferences to persist settings data between sessions.

The onCreateView method inflates the XML layout file for the fragment and returns its root view. The onViewCreated method is called after the view has been created and initialized, and sets up event handlers for the "Save" and "Back" buttons in the fragment.

The getUnitSettings, getLanguagesSettings, and getLocationSettings methods retrieve the user's selected settings from the appropriate radio buttons in the UI.

The setSettings method retrieves the user's previous settings from shared preferences and sets the appropriate radio buttons in the UI to reflect those settings.

The getSettingsFromSharedPreferences and setSettingsToSharedPreferences methods retrieve and save the user's settings to shared preferences.

The resetLocationData method is used to reset location-related data in shared preferences when the user changes their location setting from "Use Current Location" to "Enter Location Manually".

The handleBackButton method sets up a listener to handle the back button being pressed on the device. If the back button is pressed, the method calls backToHomeScreen, which finishes the current activity and returns the user to the home screen.

The changeMapLocationDialog method displays a dialog to the user asking if they want to reset location data when changing the location setting from "Use Current Location" to "Enter Location Manually". If the user chooses to reset location data, the method calls resetLocationData and then setSettingsToSharedPreferences. If the user chooses not to reset location data, the method calls setSettingsToSharedPreferences. In both cases, the method calls backToHomeScreen to return the user to the home screen.
 */
class SettingsFragment : Fragment() {

    private var _binding: SettingsFragmentBinding? = null
    private lateinit var newUnitSetting: String
    private lateinit var newLanguageSetting: String
    private var newLocationSetting: Boolean = false

    private lateinit var oldUnitSetting: String
    private lateinit var oldLanguageSetting: String
    private var oldLocationSetting: Boolean = false

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleBackButton()
        setSettings()
        binding.btnSave.setOnClickListener {
            getLocationSettings()
            getLanguagesSettings()
            getUnitSettings()
            if (newLocationSetting && oldLocationSetting) {
                changeMapLocationDialog()
            } else {
                setSettingsToSharedPreferences()
                backToHomeScreen()
            }
        }
        binding.btnBack.setOnClickListener {
            backToHomeScreen()
        }
    }

    private fun backToHomeScreen() {
        val refresh = Intent(requireContext(), MainActivity::class.java)
        activity?.finish()
        startActivity(refresh)
    }

    private fun getUnitSettings() {
        when (binding.radioGroupUnits.checkedRadioButtonId) {
            R.id.unit_celsius    -> newUnitSetting = "metric"
            R.id.unit_fahrenheit -> newUnitSetting = "imperial"
            R.id.unit_kelvin     -> newUnitSetting = "standard"
        }
    }

    private fun getLanguagesSettings() {
        when (binding.radioGroupLanguage.checkedRadioButtonId) {
            R.id.settings_arabic  -> newLanguageSetting = "ar"
            R.id.settings_english -> newLanguageSetting = "en"
        }
    }

    private fun getLocationSettings() {
        when (binding.radioGroupLocation.checkedRadioButtonId) {
            R.id.settings_map      -> newLocationSetting = true
            R.id.settings_location -> newLocationSetting = false
        }
    }

    private fun setSettings() {
        getSettingsFromSharedPreferences()

        when (oldUnitSetting) {
            "metric"   -> binding.unitCelsius.isChecked = true
            "imperial" -> binding.unitFahrenheit.isChecked = true
            "standard" -> binding.unitKelvin.isChecked = true
        }

        when (oldLanguageSetting) {
            "ar" -> binding.settingsArabic.isChecked = true
            "en" -> binding.settingsEnglish.isChecked = true
        }

        when (oldLocationSetting) {
            true  -> binding.settingsMap.isChecked = true
            false -> binding.settingsLocation.isChecked = true
        }

    }

    private fun getSettingsFromSharedPreferences() {
        getSharedPreferences(requireContext()).apply {
            oldUnitSetting = getString(getString(R.string.unitsSetting), "metric")!!
            oldLanguageSetting = getString(getString(R.string.languageSetting), "en")!!
            oldLocationSetting = getBoolean(getString(R.string.isMap), false)
        }
    }

    private fun setSettingsToSharedPreferences() {
        getSharedPreferences(requireContext()).edit().apply {
            putString(getString(R.string.unitsSetting), newUnitSetting)
            putString(getString(R.string.languageSetting), newLanguageSetting)
            if (newLocationSetting && !oldLocationSetting) {
                resetLocationData()
            } else if (oldLocationSetting && !newLocationSetting) {
                resetLocationData()
            }
            putBoolean(getString(R.string.isMap), newLocationSetting)
            apply()
        }
    }

    private fun resetLocationData() {
        getSharedPreferences(requireContext()).edit().apply {
            remove(getString(R.string.timeZone))
            remove(getString(R.string.location))
            remove(getString(R.string.lat))
            remove(getString(R.string.lon))
            apply()
        }
    }

    private fun handleBackButton() {
        binding.root.isFocusableInTouchMode = true
        binding.root.requestFocus()
        binding.root.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                backToHomeScreen()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun changeMapLocationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.map_dialog_title))
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                setSettingsToSharedPreferences()
                backToHomeScreen()
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                resetLocationData()
                setSettingsToSharedPreferences()
                backToHomeScreen()
                dialog.dismiss()
            }
            .show()
    }

}
