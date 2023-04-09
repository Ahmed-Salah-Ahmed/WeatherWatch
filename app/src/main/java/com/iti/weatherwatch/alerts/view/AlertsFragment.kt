package com.iti.weatherwatch.alerts.view

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iti.weatherwatch.R
import com.iti.weatherwatch.alerts.viewmodel.AlertsViewModel
import com.iti.weatherwatch.alerts.viewmodel.AlertsViewModelFactory
import com.iti.weatherwatch.databinding.FragmentAlertsBinding
import com.iti.weatherwatch.datasource.WeatherRepository
import com.iti.weatherwatch.datasource.model.WeatherAlert
import com.iti.weatherwatch.dialogs.view.AlertTimeDialog
import com.iti.weatherwatch.favorites.view.FavoritesFragment
import com.iti.weatherwatch.util.getSharedPreferences

class AlertsFragment : Fragment() {

    private var _binding: FragmentAlertsBinding? = null

    private val viewModel: AlertsViewModel by viewModels {
        AlertsViewModelFactory(WeatherRepository.getRepository(requireActivity().application))
    }

    private lateinit var alertsAdapter: AlertAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlertsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backToHomeScreen()

        initFavoritesRecyclerView()

        binding.btnAddAlert.setOnClickListener {
            if (checkFirstTime()) {
                checkDrawOverlayPermission()
                setNotFirstTime()
            } else {
                showAlertDialog()
            }
        }

        viewModel.getFavorites()
        super.onViewCreated(view, savedInstanceState)
        backToHomeScreen()

        initFavoritesRecyclerView()

        binding.btnAddAlert.setOnClickListener {
            if (checkFirstTime()) {
                checkDrawOverlayPermission()
                setNotFirstTime()
            } else {
                showAlertDialog()
            }
        }

        viewModel.getFavorites()

        lifecycleScope.launchWhenStarted {
            viewModel.alerts.collect {
                if (it.isNotEmpty()) {
                    binding.textEmptyAlert.visibility = View.GONE
                } else {
                    binding.textEmptyAlert.visibility = View.VISIBLE
                }
                fetchAlertsRecycler(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.alerts.collect {
                if (it.isNotEmpty()) {
                    binding.textEmptyAlert.visibility = View.GONE
                } else {
                    binding.textEmptyAlert.visibility = View.VISIBLE
                }
                fetchAlertsRecycler(it)
            }
        }
    }

    private fun showAlertDialog() {
        AlertTimeDialog().show(requireActivity().supportFragmentManager, "AlertDialog")
    }

    private fun setNotFirstTime() {
        getSharedPreferences(requireContext()).edit().putBoolean("permission", false).apply()
    }

    private fun checkFirstTime(): Boolean {
        return getSharedPreferences(requireContext()).getBoolean("permission", true)
    }

    private fun fetchAlertsRecycler(list: List<WeatherAlert>?) {
        alertsAdapter.alertsList = list ?: emptyList()
        alertsAdapter.notifyDataSetChanged()
    }

    private fun initFavoritesRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(FavoritesFragment().context)
        alertsAdapter = AlertAdapter(this.requireContext(), viewModel)
        binding.alertRecyclerView.layoutManager = linearLayoutManager
        binding.alertRecyclerView.adapter = alertsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun backToHomeScreen() {
        binding.root.isFocusableInTouchMode = true
        binding.root.requestFocus()
        binding.root.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                Navigation.findNavController(v)
                    .navigate(R.id.action_navigation_alerts_to_navigation_home)
                return@OnKeyListener true
            }
            false
        })
    }

    private fun checkDrawOverlayPermission() {
        // Check if we already  have permission to draw over other apps
        if (!Settings.canDrawOverlays(requireContext())) {
            // if not construct intent to request permission
            val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            alertDialogBuilder.setTitle(getString(R.string.overlay_title))
                .setMessage(getString(R.string.alert_dialog_body))
                .setPositiveButton(getString(R.string.btn_ok)) { dialog: DialogInterface, _: Int ->
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + requireContext().applicationContext.packageName)
                    )
                    // request permission via start activity for result
                    startActivityForResult(
                        intent,
                        1
                    ) //It will call onActivityResult Function After you press Yes/No and go Back after giving permission
                    dialog.dismiss()
                    showAlertDialog()
                }.setNegativeButton(
                    getString(R.string.btn_cancel)
                ) { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                    showAlertDialog()
                }.show()
        }
    }

}
