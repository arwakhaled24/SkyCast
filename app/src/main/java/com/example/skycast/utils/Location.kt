/*
package com.example.skycast.utils

import android.Manifest
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class Location(context: Context) {

    private  var fusedLocationProviderClient: FusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(context)
     var locationState: MutableState<Location> =  mutableStateOf(Location(LocationManager.GPS_PROVIDER))

    fun CheckPermissionAndIsLocationEnabled(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                getCurrentLocation()
            } else {
                enableLocationServices()
            }
        } else {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1
            )
        }
    }
}*/
