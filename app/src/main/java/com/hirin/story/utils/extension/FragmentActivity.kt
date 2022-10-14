package com.hirin.story.utils.extension

import android.content.Intent
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.minimizeApp() {
    val startMain = Intent(Intent.ACTION_MAIN)
    startMain.addCategory(Intent.CATEGORY_HOME)
    startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(startMain)
}