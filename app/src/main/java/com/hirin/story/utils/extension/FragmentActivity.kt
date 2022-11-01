package com.hirin.story.utils.extension

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.minimizeApp() {
    val startMain = Intent(Intent.ACTION_MAIN)
    startMain.addCategory(Intent.CATEGORY_HOME)
    startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(startMain)
}

fun FragmentActivity.checkLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(applicationContext,
        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
}

fun FragmentActivity.checkCameraPermission(): Boolean {
    return ContextCompat.checkSelfPermission(applicationContext,
        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
}