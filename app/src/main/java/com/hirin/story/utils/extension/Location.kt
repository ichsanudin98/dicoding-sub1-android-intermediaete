package com.hirin.story.utils.extension

import android.location.Location
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.FusedLocationProviderClient

fun FusedLocationProviderClient.getLastLocation(activity: FragmentActivity, listener: (Location) -> Unit) {
    if (activity.checkLocationPermission()) {
        lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                listener.invoke(location)
            }
        }
    }
}