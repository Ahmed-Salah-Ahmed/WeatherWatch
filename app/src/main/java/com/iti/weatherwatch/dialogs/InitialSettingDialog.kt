package com.iti.weatherwatch.dialogs

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.gms.location.*
import com.iti.weatherwatch.MainActivity
import com.iti.weatherwatch.R
import com.iti.weatherwatch.databinding.InitialSettingDialogBinding
import com.iti.weatherwatch.util.getSharedPreferences

class InitialSettingDialog : DialogFragment() {
    //for location permission
    private val PERMISSION_ID: Int = 14
    var latitude = 0.0
    var longitude = 0.0
    var fusedLocationProviderClient: FusedLocationProviderClient? = null

    private var _binding: InitialSettingDialogBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner)
        _binding = InitialSettingDialogBinding.inflate(inflater, container, false)
//        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        return binding.root
//        return inflater.inflate(R.layout.initial_setting_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        view.keyLis
        binding.btnOk.setOnClickListener {
            if (binding.radioGroup.checkedRadioButtonId == R.id.radio_gps) {
                getFreshLocation()
            }
        }
    }

    override fun onStart() {
        super.onStart()
//        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
//        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    // for get last location
    private fun isLocationEnabled(): Boolean {
        val locationManager =
            requireActivity().application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }

/*    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            // if request id cancelled, the result array will be empty
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //request location updates
//                getFreshLocation()
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                startMainActivity()
                Toast.makeText(requireContext(), "You must open the Location", Toast.LENGTH_SHORT)
                    .show()
                *//*if(yes){
                    requestPermission();
                }else{
                    use the app without permission
                }*//*
            }
        }
    }*/

    private fun getFreshLocation() {
        val locationRequest = LocationRequest.create()
        //        locationRequest.setFastestInterval(5000)
//        locationRequest.numUpdates = 1
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 1000
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(requireActivity())

                fusedLocationProviderClient?.apply {
                    this.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()!!
                    )
                }
            } else {
                enableLocationSetting()
            }
        } else {
            requestPermission()
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val location = locationResult.lastLocation
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude
            }
            Log.i("zoz", "onLocationResult: $longitude")
            saveLocationInSharedPreferences(latitude, longitude)
        }
    }

    private fun enableLocationSetting() {
        val settingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(settingIntent)
    }

    private fun saveLocationInSharedPreferences(long: Double, lat: Double) {
        val editor = getSharedPreferences(this.requireContext()).edit()
        editor.putFloat(getString(R.string.lat), lat.toFloat())
        editor.putFloat(getString(R.string.lon), long.toFloat())
        editor.putBoolean("firstTime", false)
        editor.apply()
        startMainActivity()
    }

    private fun startMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
    }

}
