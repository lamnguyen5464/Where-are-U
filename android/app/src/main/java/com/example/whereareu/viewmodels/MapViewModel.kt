package com.example.whereareu.viewmodels

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.whereareu.helpers.PermissionHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng


class MapViewModel(activity: Activity) : LocationListener {

    private var map: GoogleMap? = null
    private var locationManager: LocationManager =
        activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val activity: Activity = activity

    private var isGPSEnable: Boolean = false
    private var isGrantedPermission: Boolean = false

    fun setMap(googleMap: GoogleMap) {
        this.map = googleMap
    }

    fun checkLocationPermission() {
        if ((ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            Log.d("@@@ isGrantedPermission", "requesting...")
            PermissionHelper.requestLocationPermission(activity)
        } else {
            Log.d("@@@ isGrantedPermission", "true")
            isGrantedPermission = true
            checkGPSEnable()
        }
    }

    private fun checkGPSEnable() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("@@@ isGPSEnable", "requesting...")
            PermissionHelper.enableLocationSettings(activity)
        } else {
            Log.d("@@@ isGPSEnable", "true")
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
            1000,
            0f,
            this
        )
    }

    override fun onLocationChanged(location: Location) {
        Log.d(
            "@@@",
            "Latitude: " + location.latitude + " , Longitude: " + location.longitude
        )

        val sydney = LatLng(location.latitude, location.longitude)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        this.map?.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        this.map?.animateCamera(CameraUpdateFactory.zoomTo(17f))
    }

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

}