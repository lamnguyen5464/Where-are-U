package com.example.whereareu.viewmodels

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.whereareu.helpers.PermissionHelper
import com.google.android.gms.maps.GoogleMap

class MapViewModel(activity: Activity) {

    private lateinit var mMap: GoogleMap
    private var locationManager: LocationManager =
        activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val activity: Activity = activity

    private var isGPSEnable: Boolean = false
    private var isGrantedPermission: Boolean = false

    fun checkLocationPermission() {
        if ((ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            Log.d("@@@ isGrantedPermission", "requesting..." )
            PermissionHelper.requestLocationPermission(activity)
        } else {
            Log.d("@@@ isGrantedPermission", "true" )
            isGrantedPermission = true
            checkGPSEnable()
        }
    }

    private fun checkGPSEnable() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("@@@ isGPSEnable", "requesting..." )
            PermissionHelper.enableLocationSettings(activity)
        } else {
            Log.d("@@@ isGPSEnable", "true" )
            isGPSEnable = true
            onDonePermission()
        }
    }

    fun onDonePermission() {
        if (isGPSEnable && isGrantedPermission) {
            getLocation()
            return
        }
        checkLocationPermission()
    }

    fun getLocation() {
        if ((ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            return
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            10,
            0f,
            LocationListener {
                val location: Location = it
                Log.d(
                    "@@@",
                    "Latitude: " + location.latitude + " , Longitude: " + location.longitude
                )

//        val sydney = LatLng(location.latitude, location.longitude)
////        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(17f))
            })
    }

}