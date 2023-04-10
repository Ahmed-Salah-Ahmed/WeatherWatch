package com.iti.weatherwatch.datasource

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*

/*
This is a class called MyLocationProvider which is responsible for providing the user's current location. It requires a Fragment object to be passed in the constructor. It has the following methods:

    checkPermission() method which checks if the user has granted location permission or not.
    requestPermission() method which requests the user for location permission.
    isLocationEnabled() method which checks if the user's device location is enabled or not.
    getFreshLocation() method which gets the user's current location and updates it in the MutableLiveData object.
    locationCallback is a private object which is used to get the user's current location in getFreshLocation() method.
    _locationList is a private MutableLiveData object which holds the user's current location.
    locationList is a public LiveData object that can be observed to get the user's current location.
    _denyPermission is a private MutableLiveData object which is used to inform the user when the location permission is denied.
    denyPermission is a public LiveData object that can be observed to know when the location permission is denied.
    enableLocationSetting() method which opens the device's location settings page.
    stopLocationUpdates() method which stops the location updates when the user's location is obtained.
    myLocationList is a private ArrayList object that holds the latitude and longitude values of the user's current location.
    fusedLocationProviderClient is a private FusedLocationProviderClient object which is used to get the user's current location.
 */
class MyLocationProvider(private val fragment: Fragment) {
    private var myLocationList = ArrayList<Double>()
    private var fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(fragment.requireActivity())

    fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            fragment.requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            fragment.requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    // for get last location
    fun isLocationEnabled(): Boolean {
        val locationManager =
            fragment.requireActivity().application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        fragment.registerForActivityResult(RequestPermission()) { isGranted ->
            if (isGranted) {
                getFreshLocation()
            } else {
                denyPermission.postValue("denied")
            }
        }

    fun getFreshLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest.interval = 120000
        locationRequest.fastestInterval = 120000
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.apply {
                    requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
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
            myLocationList.apply {
                add(location!!.latitude)
                add(location.longitude)
            }
            _locationList.postValue(myLocationList)
            stopLocationUpdates()
        }
    }

    private var _locationList = MutableLiveData<ArrayList<Double>>()
    val locationList = _locationList

    private var _denyPermission = MutableLiveData<String>()
    val denyPermission = _denyPermission

    private fun enableLocationSetting() {
        val settingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        fragment.startActivity(settingIntent)
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

}
