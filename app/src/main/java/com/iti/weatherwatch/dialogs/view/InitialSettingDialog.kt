package com.iti.weatherwatch.dialogs.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.iti.weatherwatch.R
import com.iti.weatherwatch.databinding.InitialSettingDialogBinding
import com.iti.weatherwatch.datasource.MyLocationProvider
import com.iti.weatherwatch.dialogs.viewmodel.DialogViewModel
import com.iti.weatherwatch.dialogs.viewmodel.DialogViewModelFactory
import com.iti.weatherwatch.ui.MainActivity
import com.iti.weatherwatch.util.getCurrentLocale
import com.iti.weatherwatch.util.getSharedPreferences

class InitialSettingDialog : DialogFragment() {
    private var language: String? = null
    private val viewModel: DialogViewModel by viewModels {
        DialogViewModelFactory(MyLocationProvider(this))
    }
    private var _binding: InitialSettingDialogBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner)
        _binding = InitialSettingDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleBackButton()
        val local = getCurrentLocale(requireContext())
        language = local?.language
        binding.btnOk.setOnClickListener {
            if (binding.radioGroup.checkedRadioButtonId == R.id.radio_gps) {
                viewModel.getFreshLocation()
            } else if (binding.radioGroup.checkedRadioButtonId == R.id.radio_maps) {
                saveIsMapInSharedPreferences()
            }
        }

        viewModel.observeLocation().observe(viewLifecycleOwner) {
            if (it[0] != 0.0 && it[1] != 0.0) {
                saveLocationInSharedPreferences(it[0], it[1])
            }
        }

        viewModel.observePermission().observe(viewLifecycleOwner) {
            if (it == "denied") {
                getSharedPreferences(this.requireContext()).edit().apply {
                    putBoolean("firstTime", false)
                    putString(getString(R.string.languageSetting), language)
                    putString(getString(R.string.unitsSetting), "metric")
                    apply()
                }
                dialog!!.dismiss()
                startMainActivity()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun saveLocationInSharedPreferences(lat: Double, long: Double) {
        getSharedPreferences(this.requireContext()).edit().apply {
            putFloat(getString(R.string.lat), lat.toFloat())
            putFloat(getString(R.string.lon), long.toFloat())
            putString(getString(R.string.languageSetting), language)
            putString(getString(R.string.unitsSetting), "metric")
            putBoolean("firstTime", false)
            apply()
        }
        startMainActivity()
    }

    private fun saveIsMapInSharedPreferences() {
        getSharedPreferences(this.requireContext()).edit().apply {
            putBoolean("firstTime", false)
            putBoolean(getString(R.string.isMap), true)
            putString(getString(R.string.languageSetting), language)
            putString(getString(R.string.unitsSetting), "metric")
            apply()
        }
        startMainActivity()
    }

    private fun startMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun handleBackButton() {
        binding.root.isFocusableInTouchMode = true
        binding.root.requestFocus()
        binding.root.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                activity?.finish()
                return@OnKeyListener true
            }
            false
        })
    }
}
