package com.hirin.story.ui.main.pages.momentcreate

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.hirin.story.R
import com.hirin.story.data.BasicResponse
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.databinding.FragmentMomentCreateBinding
import com.hirin.story.ui.base.BaseFragment
import com.hirin.story.ui.camera.CameraActivity
import com.hirin.story.ui.main.MainActivity
import com.hirin.story.utils.CameraUtil
import com.hirin.story.utils.customview.InputTextView
import com.hirin.story.utils.extension.observeNonNull
import com.hirin.story.utils.extension.showSuccessSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class MomentCreateFragment : BaseFragment<FragmentMomentCreateBinding>() {
    // <editor-fold defaultstate="collapsed" desc="initialize ui">
    private lateinit var descriptionColumn: InputTextView
    private var mapFragment: SupportMapFragment? = null
    private var gMap: GoogleMap? = null
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="initialize data">
    private val viewModel: MomentCreateViewModel by viewModel()
    private var photoFile: File? = null

    private var locationSelected: LatLng? = null
    // </editor-fold>

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        gMap = googleMap
        gMap?.apply {
            val oldPosition = cameraPosition.target

            setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(), R.raw.map_style))
            setOnCameraMoveStartedListener {
                binding.contentView.requestDisallowInterceptTouchEvent(true)
                binding.igMarker.animate().translationY(-50f).start()
                binding.igMarkerShadow.animate().withStartAction {
                    binding.igMarkerShadow.setPadding(10)
                }.start()
            }
            setOnCameraIdleListener {
                binding.contentView.requestDisallowInterceptTouchEvent(false)
                val newPosition = cameraPosition.target
                if (newPosition != oldPosition) {
                    binding.igMarker.animate().translationY(0f).start()
                    binding.igMarkerShadow.animate().withStartAction {
                        binding.igMarkerShadow.setPadding(0)
                    }.start()
                }

                locationSelected = newPosition
            }
        }
    }

    override fun getViewBinding(): FragmentMomentCreateBinding =
        FragmentMomentCreateBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()

        if (!isAllPermissionGranted())
            requestPermission()

        this.binding.viewModel = viewModel.also {
            it.momentCreateLiveData.observeNonNull(viewLifecycleOwner, ::handleSuccessCreatedMoment)
            it.genericErrorLiveData.observeNonNull(viewLifecycleOwner, ::handleErrorCreatedMoment)
            it.networkErrorLiveData.observeNonNull(viewLifecycleOwner, ::handleNetworkError)
            it.loadingWidgetLiveData.observeNonNull(viewLifecycleOwner, ::handleLoadingWidget)
        }
    }

    private fun initUI() {
        with(binding) {
            (requireActivity() as MainActivity).supportActionBar?.title = resources.getString(R.string.menu_moment_create)
            mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)
            btCamera.setOnClickListener {
                if (!isAllPermissionGranted())
                    requestPermission()
                else
                    launcherCamera.launch(
                        Intent(requireContext(), CameraActivity::class.java)
                    )
            }
            btGallery.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_GET_CONTENT
                intent.type = "image/*"
                val chooser = Intent.createChooser(intent, "Choose a Picture")
                launcherIntentGallery.launch(chooser)
            }
            btUpload.setOnClickListener {
                photoFile?.let { file ->
                    viewModel?.createMoment(
                        file,
                        descriptionColumn.value().text.toString(),
                        locationSelected?.latitude?.toString(), locationSelected?.longitude?.toString()
                    )
                }
            }
            descriptionColumn = InputTextView(requireContext(), fbDescription.root).apply {
                title().text = resources.getString(R.string.title_description)
                value().run {
                    inputType = InputType.TYPE_CLASS_TEXT
                    minLines = 6
                    typeface = Typeface.DEFAULT
                    imeOptions = EditorInfo.IME_ACTION_NEXT
                    setOnFocusChangeListener { _, hasFocus ->
                        if (hasFocus) showFocusedState() else {
                            if (value().text.isNotEmpty()) if (isValidation(descriptionColumn)) showUnfocusedState()
                        }
                    }
                }
                setMarginEndDrawableEnd(resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._10sdp), clearIcon())
            }
            with(descriptionColumn) {
                setTextBehaviour({
                    clearIcon().isVisible = value().text.isNotEmpty()
                    btUpload.isEnabled = checkingIsValid()
                }, {})
                clearIcon().setOnClickListener { this.value().setText("") }
            }

            val image = ObjectAnimator.ofFloat(igMoment, View.ALPHA, 1f).setDuration(500)
            val gallery = ObjectAnimator.ofFloat(btGallery, View.ALPHA, 1f).setDuration(500)
            val camera = ObjectAnimator.ofFloat(btCamera, View.ALPHA, 1f).setDuration(500)

            val titleMap = ObjectAnimator.ofFloat(tbSelectLocation, View.ALPHA, 1f).setDuration(500)
            val map = ObjectAnimator.ofFloat(map, View.ALPHA, 1f).setDuration(500)
            val marker = ObjectAnimator.ofFloat(igMarker, View.ALPHA, 1f).setDuration(500)
            val shadow = ObjectAnimator.ofFloat(igMarkerShadow, View.ALPHA, 0.3f).setDuration(500)

            val setImage = AnimatorSet().apply {
                playTogether(gallery, camera)
            }
            val setMaps = AnimatorSet().apply {
                playTogether(titleMap, map, marker, shadow)
            }
            val description = ObjectAnimator.ofFloat(fbDescription.root, View.ALPHA, 1f).setDuration(500)
            val upload = ObjectAnimator.ofFloat(btUpload, View.ALPHA, 1f).setDuration(500)

            AnimatorSet().apply {
                playSequentially(image, setImage, description, setMaps, upload)
                start()
            }
        }
    }

    private fun checkingIsValid() = isValidation(descriptionColumn)

    private fun isValidation(inputTextView: InputTextView): Boolean =
        when (inputTextView) {
            descriptionColumn -> {
                descriptionColumn.validation.invoke() && photoFile != null
            }
            else -> { true }
        }

    private fun handleSuccessCreatedMoment(response: BasicResponse) {
        showSuccessSnackbar(response.message)
        findNavController().navigateUp()
    }

    private fun handleErrorCreatedMoment(response: GenericErrorResponse) {
        handleGenericError(response)
    }

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == CameraUtil.CAMERAX_RESULT) {
            photoFile = result.data?.getSerializableExtra(CameraActivity.PICTURE) as File
            binding.igMoment.load(photoFile)
            binding.btUpload.isEnabled = checkingIsValid()
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selected: Uri = result.data?.data as Uri
            photoFile = CameraUtil.uriToFile(selected, requireContext())

            binding.igMoment.load(photoFile)
            binding.btUpload.isEnabled = checkingIsValid()
        }
    }

    private fun isAllPermissionGranted() = arrayOf(
        Manifest.permission.CAMERA
    ).all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.CAMERA
            ),
            CameraUtil.REQUEST_CODE_PERMISSIONS
        )
    }
}