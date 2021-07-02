package com.example.whereareu.helpers

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.core.app.ActivityCompat

class PermissionHelper {
    companion object {
        val PERMISSION_LOCATION_CODE = 101
        val INTENT_LOCATION_SETTING_CODE = 201

        fun enableLocationSettings(activity: Activity) {
            val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            activity.startActivityForResult(settingsIntent, INTENT_LOCATION_SETTING_CODE)
        }

        fun requestLocationPermission(activity: Activity) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_LOCATION_CODE
            )
        }
    }
}