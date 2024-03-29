package com.example.whereareu.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.whereareu.R
import com.example.whereareu.helpers.PermissionHelper
import com.example.whereareu.helpers.SocketHelper
import com.example.whereareu.viewmodels.MapViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment


@Suppress("DEPRECATION")
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapViewModel: MapViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        this.actionBar?.hide()

        SocketHelper.getIntance().initSocket(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mapViewModel = MapViewModel(this)
        mapViewModel.checkLocationPermission()
    }

    override fun onRestart() {
        super.onRestart()
//        Log.d("@@@", "on Restart")
        SocketHelper.getIntance().initSocket(this)
    }

//    override fun onResume() {
//        super.onResume()
//        Log.d("@@@", "on Resume")
//        SocketHelper.getIntance().initSocket(this)
//    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapViewModel.setMap(googleMap)
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketHelper.getIntance().forceDisconnect()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mapViewModel.checkLocationPermission()
        when (requestCode) {
            PermissionHelper.PERMISSION_LOCATION_CODE -> if (requestCode == PermissionHelper.PERMISSION_LOCATION_CODE) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )
        mapViewModel.checkLocationPermission()
    }

}