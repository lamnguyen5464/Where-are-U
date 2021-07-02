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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.whereareu.helpers.ConfigHelper
import com.example.whereareu.helpers.JSONHelper
import com.example.whereareu.helpers.PermissionHelper
import com.example.whereareu.helpers.SocketHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject


class MapViewModel(activity: Activity) : LocationListener {

    private var map: GoogleMap? = null
    private var locationManager: LocationManager =
        activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val activity: Activity = activity

    private var isGPSEnable: Boolean = false
    private var isGrantedPermission: Boolean = false
    private var didScaledLocation: Boolean = false

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

        Toast.makeText(activity, "Getting your location...", Toast.LENGTH_LONG).show()

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            3000,
            0f,
            this
        )

        //Listen
        SocketHelper.getIntance().setEventListener("server_data", Emitter.Listener {
            Log.d("@@@ response", it[0].toString())
//            onHandleLocationResponse(JSONObject(it[0].toString()))
        })
    }

    fun onHandleLocationResponse(response: JSONObject) {
        val latitude = JSONHelper.getFieldSafely(response, "latitude").toDouble()
        val longitude = JSONHelper.getFieldSafely(response, "longitude").toDouble()
        val id = JSONHelper.getFieldSafely(response, "id")


        activity.runOnUiThread {
            Log.d(
                "@@@",
                "Latitude: " + latitude + " , Longitude: " + longitude + ", id: " + id
            )

            val location = LatLng(latitude, longitude)

            this.map?.addMarker(MarkerOptions().position(location).title("Here"))

        }
    }

    override fun onLocationChanged(location: Location) {
        val obj = JSONObject()
        obj.put("latitude", location.latitude)
        obj.put("longitude", location.longitude)
        obj.put("id", SocketHelper.getIntance().id)

        SocketHelper.getIntance().socket.emit("device_data", obj)

        if (!didScaledLocation) {
            didScaledLocation = true
            this.map?.moveCamera(
                CameraUpdateFactory.newLatLng(
                    LatLng(
                        location.latitude,
                        location.longitude
                    )
                )
            )
            this.map?.animateCamera(CameraUpdateFactory.zoomTo(17f))
        }


    }

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

}