package com.iti.weatherwatch.datasource

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*

class MyLocationProvider(private val fragment: Fragment) {
    private val permissionId: Int = 14
    private var myLocationList = ArrayList<Double>()
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

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
        ActivityCompat.requestPermissions(
            fragment.requireActivity(), arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            permissionId
        )
    }

    // for get last location
    fun isLocationEnabled(): Boolean {
        val locationManager =
            fragment.requireActivity().application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
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

    fun getFreshLocation() {
        val locationRequest = LocationRequest.create()
        //        locationRequest.setFastestInterval(5000)
//        locationRequest.numUpdates = 1
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 1000
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(fragment.requireActivity())

                fusedLocationProviderClient?.apply {
                    this.requestLocationUpdates(
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

//    fun observeLocationData(): LiveData<ArrayList<Double>> {
//            return locationList
//    }

    private fun enableLocationSetting() {
        val settingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        fragment.startActivity(settingIntent)
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
    }

}
