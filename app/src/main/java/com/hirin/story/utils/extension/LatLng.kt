package com.hirin.story.utils.extension

import android.content.Context
import android.location.Geocoder
import android.os.Build
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.*

fun LatLng.getAddressName(context: Context): String? {
    var addressName: String? = null
    val geocoder = Geocoder(context, Locale.getDefault())
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(this.latitude, this.longitude, 1) {
                addressName = it[0].getAddressLine(0)
            }
        } else {
            val list = geocoder.getFromLocation(this.latitude, this.longitude, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return addressName
}