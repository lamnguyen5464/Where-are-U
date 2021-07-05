package com.example.whereareu.viewmodels

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.whereareu.R
import com.example.whereareu.helpers.JSONHelper
import com.example.whereareu.helpers.PermissionHelper
import com.example.whereareu.helpers.SocketHelper
import com.example.whereareu.models.EndPoint
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import org.json.JSONArray
import org.json.JSONObject


class MapViewModel(private val activity: Activity) : LocationListener {

    private var map: GoogleMap? = null
    private var locationManager: LocationManager =
        activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val listEndPoints = ArrayList<EndPoint>()
    private val listMarkers = ArrayList<MarkerOptions>()


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

    private fun onDonePermission() {
        if (isGPSEnable && isGrantedPermission) {
            getLocation()
            return
        }
        checkLocationPermission()
    }

    private fun getLocation() {
        if ((ActivityCompat.checkSelfPermission(
                this.activity,
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
        SocketHelper.getIntance().setEventListener("server_data") {
//            Log.d("@@@ response", it[0].toString())
            val listLocations: JSONArray = JSONHelper.getJsonArrayromString(it[0].toString())
            onHandleNewResposne(listLocations)

        }
    }

    private fun clearMap() {
        activity.runOnUiThread {
            this.map?.clear()
        }
    }

    private fun onHandleNewResposne(response: JSONArray) {
        clearMap()
        listEndPoints.clear()
        listMarkers.clear()
        for (index in 0 until response.length()) {
            val endpoint = EndPoint(response.getJSONObject(index))
            listEndPoints.add(endpoint)
        }
        drawMarkerOnMap()
        updateListOnline()
    }

    @SuppressLint("SetTextI18n")
    private fun updateListOnline() {
        activity.runOnUiThread {
            val listUser: LinearLayout = activity.findViewById(R.id.listUser)
            activity.findViewById<TextView>(R.id.numOfOnlUsers).text =
                listEndPoints.size.toString() + " online"
            listUser.removeAllViews()
            listEndPoints.forEach {
                listUser.addView(createUserCell(it))
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun createUserCell(endPoint: EndPoint): View? {
        val view: ViewGroup? = null
        val inflatedView = View.inflate(activity, R.layout.online_user_item, view)

        inflatedView.findViewById<TextView>(R.id.ID).text = "ID: " + endPoint.id
        inflatedView.findViewById<TextView>(R.id.latitude).text = "Latitude: " + endPoint.latitude
        inflatedView.findViewById<TextView>(R.id.longitude).text =
            "Longitude: " + endPoint.longitude

        inflatedView.setOnClickListener {
            moveMapTo(endPoint.latLng)
        }
        return inflatedView
    }

    private fun drawMarkerOnMap() {
        listEndPoints.forEach {
            val marker = MarkerOptions()
            val isMe = SocketHelper.getIntance().isMe(it.id)
            marker.position(it.latLng)
            marker.title(it.id)
            marker.icon(
                BitmapDescriptorFactory.defaultMarker(
                    if (isMe)
                        BitmapDescriptorFactory.HUE_RED else BitmapDescriptorFactory.HUE_GREEN
                )
            )
            marker.title(if (isMe) "Me here!" else "You there?")
            marker.snippet(it.id)
            listMarkers.add(marker)
        }
        activity.runOnUiThread {
            listMarkers.forEach {
                this.map?.addMarker(it)
            }
        }
    }

    private fun moveMapTo(latLng: LatLng) {
        this.map?.moveCamera(
            CameraUpdateFactory.newLatLng(
                LatLng(
                    latLng.latitude,
                    latLng.longitude
                )
            )
        )
        this.map?.animateCamera(CameraUpdateFactory.zoomTo(17f))
    }

    override fun onLocationChanged(location: Location) {
        val obj = JSONObject()
        obj.put("latitude", location.latitude)
        obj.put("longitude", location.longitude)
        obj.put("id", SocketHelper.getIntance().id)

        SocketHelper.getIntance().socket.emit("device_data", obj)

        if (!didScaledLocation) {
            didScaledLocation = true
            moveMapTo(LatLng(location.latitude, location.longitude))
        }


    }

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

}