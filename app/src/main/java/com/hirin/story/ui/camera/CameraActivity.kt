package com.hirin.story.ui.camera

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.hirin.story.R
import com.hirin.story.databinding.ActivityCameraBinding
import com.hirin.story.ui.base.BaseActivity
import com.hirin.story.utils.CameraUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CameraActivity : BaseActivity<ActivityCameraBinding>(ActivityCameraBinding::inflate) {
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null

    companion object {
        const val PICTURE: String = "picture"
        const val CAMERA_TYPE_SELECTED: String = "isBackCamera"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    private fun initUI() {
        with(binding) {
            captureImage.setOnClickListener { takePhoto() }
            switchCamera.setOnClickListener {
                cameraSelector = if (cameraSelector.equals(CameraSelector.DEFAULT_BACK_CAMERA)) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA
                startCamera()
            }
        }
    }

    private fun takePhoto() {
        val photoFile = CameraUtil.createFile(application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture?.takePicture(
            outputOptions, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val intent = Intent()
                    intent.putExtra(PICTURE, photoFile)
                    intent.putExtra(CAMERA_TYPE_SELECTED, cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                    setResult(CameraUtil.CAMERAX_RESULT, intent)
                    finish()

                    showToast(resources.getString(R.string.title_success_capturing_photo), R.drawable.bg_snackbar_success)
                }

                override fun onError(exception: ImageCaptureException) {
                    showToast(resources.getString(R.string.error_capturing_photo), R.drawable.bg_snackbar_danger)
                }

            }
        )
    }

    private fun startCamera() {
        // showCamera
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                Toast.makeText(
                    this@CameraActivity,
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    fun showToast(message: String, background: Int = R.drawable.bg_snackbar_success, delay:Long = 5000) {
        binding.toastWidget.visibility = View.VISIBLE
        binding.toastWidget.background = ContextCompat.getDrawable(applicationContext, background)
        binding.toastWidget.animate().alpha(1.0f)
        binding.message.text = message
        hideToast(delay)
    }

    fun hideToast(delay:Long){
        lifecycleScope.launch {
            delay(delay)
            binding.toastWidget.animate().alpha(0.0f)
            binding.toastWidget.visibility = View.GONE
        }
    }
}