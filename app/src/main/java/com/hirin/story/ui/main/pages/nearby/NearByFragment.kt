package com.hirin.story.ui.main.pages.nearby

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.hirin.story.R
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.data.moment.response.MomentListResponse
import com.hirin.story.data.moment.response.MomentListStoryResponse
import com.hirin.story.databinding.FragmentNearByBinding
import com.hirin.story.ui.base.BaseFragment
import com.hirin.story.ui.main.MainActivity
import com.hirin.story.utils.extension.checkLocationPermission
import com.hirin.story.utils.extension.getAddressName
import com.hirin.story.utils.extension.getLastLocation
import com.hirin.story.utils.extension.observeNonNull
import org.koin.androidx.viewmodel.ext.android.viewModel

class NearByFragment : BaseFragment<FragmentNearByBinding>(), OnInfoWindowClickListener {
    // <editor-fold defaultstate="collapsed" desc="initialize ui">
    private var mapFragment: SupportMapFragment? = null
    private var gMap: GoogleMap? = null
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="initialize data">
    private val viewModel: NearByViewModel by viewModel()
    private var page = 1
    private var totalSize = 10
    private val boundsBuilder = LatLngBounds.Builder()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
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
        gMap = googleMap
        gMap?.apply {
            setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(), R.raw.map_style))
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isCompassEnabled = true
            if (requireActivity().checkLocationPermission()) {
                isMyLocationEnabled = true
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                fusedLocationClient.getLastLocation(requireActivity()) {
                    createMyLocationMarker(it)
                }
            }
        }

    override fun getViewBinding(): FragmentNearByBinding =
        FragmentNearByBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        initPermission()
        this.binding.viewModel = viewModel.also {
            it.momentListLiveData.observeNonNull(viewLifecycleOwner, ::handleSuccessGetData)
            it.genericErrorLiveData.observeNonNull(viewLifecycleOwner, ::handleErrorGetData)
            it.networkErrorLiveData.observeNonNull(viewLifecycleOwner, ::handleNetworkError)
            it.loadingWidgetLiveData.observeNonNull(viewLifecycleOwner, ::handleLoadingWidget)

            it.getAllMoment(page, totalSize, 1)
        }
    }

    override fun onResume() {
        super.onResume()
        with(binding) {
            (requireActivity() as MainActivity).supportActionBar?.title = resources.getString(R.string.menu_nearby)
        }
    }

    override fun onInfoWindowClick(p0: Marker) {
        p0.tag?.let {
            val direction = NearByFragmentDirections.actionToMomentDetailFragment(
                data = it as MomentListStoryResponse
            )
            findNavController().navigate(direction)
        }
    }

    private fun initUI() {
        with(binding) {
            (requireActivity() as MainActivity).supportActionBar?.title = resources.getString(R.string.menu_nearby)
            mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)
        }
    }

    private fun initPermission() {
        if (!requireActivity().checkLocationPermission()) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            fusedLocationClient.getLastLocation(requireActivity()) {
                createMyLocationMarker(it)
            }
        }
    }

    private fun createMyLocationMarker(location: Location) {
        gMap?.apply {
            val myLocation = LatLng(location.latitude, location.longitude)
            animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f))
        }
    }

    private fun handleSuccessGetData(response: MomentListResponse) {
        if (response.listStory.isNotEmpty()) {
            gMap?.apply {
                for (data in response.listStory) {
                    val position = LatLng(data.lat, data.lon)
                    val marker = addMarker(
                        MarkerOptions()
                            .position(position)
                            .title(data.name)
                            .snippet(position.getAddressName(requireContext()))
                            .icon(vectorToBitmap(R.drawable.ic_person_pin_circle_24_black, Color.parseColor("#000000")))
                    )
                    marker?.tag = data
                    boundsBuilder.include(position)
                    setOnInfoWindowClickListener(this@NearByFragment)
                }
                val bounds: LatLngBounds = boundsBuilder.build()
                animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        bounds,
                        resources.displayMetrics.widthPixels,
                        resources.displayMetrics.heightPixels,
                        300
                    )
                )
            }
        }
    }

    private fun handleErrorGetData(response: GenericErrorResponse) {
        handleGenericError(response)
    }

    private fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
            ?: return BitmapDescriptorFactory.defaultMarker()
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}