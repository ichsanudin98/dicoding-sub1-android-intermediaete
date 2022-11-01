package com.hirin.story.utils

import android.content.Context
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.size
import java.io.File

object ImageUtil {
    private lateinit var context: Context

    fun init(c: Context) {
        context = c
    }

    suspend fun compressImage(imageFile: File): File {
        return Compressor.compress(context, imageFile) {
            size(1_000_000)
        }
    }
}