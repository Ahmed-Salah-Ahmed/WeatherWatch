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
