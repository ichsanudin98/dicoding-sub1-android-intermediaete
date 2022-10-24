package com.hirin.story.ui.main.pages.nearby

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.navigation.fragment.findNavController
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
import com.hirin.story.ui.main.pages.momentlist.MomentListFragmentDirections
import com.hirin.story.utils.extension.getAddressName
import com.hirin.story.utils.extension.navigateSlideHorizontal
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
    private var totalSize = 1
    private val boundsBuilder = LatLngBounds.Builder()
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
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private val infoWindowCallback = GoogleMap.OnInfoWindowClickListener { marker ->
        marker.id
    }

    override fun getViewBinding(): FragmentNearByBinding =
        FragmentNearByBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
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
            val direction = MomentListFragmentDirections.actionToMomentDetailFragment(
                data = it as MomentListStoryResponse
            )
            findNavController().navigateSlideHorizontal(direction)
        }
    }

    private fun initUI() {
        with(binding) {
            (requireActivity() as MainActivity).supportActionBar?.title = resources.getString(R.string.menu_nearby)
            mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)
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
                            .icon(vectorToBitmap(R.drawable.ic_person_pin_circle_24_black, Color.parseColor("#3DDC84")))
                    )
                    marker?.tag = data
                    setOnInfoWindowClickListener(this@NearByFragment)
                    marker?.showInfoWindow()
                    boundsBuilder.include(position)
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