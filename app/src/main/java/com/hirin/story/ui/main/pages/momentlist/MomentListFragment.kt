package com.hirin.story.ui.main.pages.momentlist

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.hirin.story.R
import com.hirin.story.data.moment.response.MomentListStoryResponse
import com.hirin.story.databinding.FragmentMomentListBinding
import com.hirin.story.ui.base.BaseFragment
import com.hirin.story.ui.main.MainActivity
import com.hirin.story.utils.constant.GoToConstant
import com.hirin.story.utils.extension.checkLocationPermission
import com.hirin.story.utils.extension.navigateSlideHorizontal
import org.koin.androidx.viewmodel.ext.android.viewModel

class MomentListFragment : BaseFragment<FragmentMomentListBinding>() {
    // <editor-fold defaultstate="collapsed" desc="initialize data">
    private val viewModel: MomentListViewModel by viewModel()
    private val momentListAdapter by lazy {
        MomentListPagingAdapter(::onItemClicked,
            resources.getString(R.string.title_latest),
            resources.getString(R.string.title_minute),
            resources.getString(R.string.title_hour),
            resources.getString(R.string.title_day),
            resources.getString(R.string.title_ago))
    }
    private var page = 1
    private var totalSize = 6
    // </editor-fold>

    override fun getViewBinding(): FragmentMomentListBinding =
        FragmentMomentListBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        this.binding.viewModel = viewModel.also {
            it.getAllMomentWithPaging().observe(viewLifecycleOwner) { result ->
                momentListAdapter.submitData(lifecycle, result)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        with(binding) {
            (requireActivity() as MainActivity).supportActionBar?.title = getString(R.string.app_name)
        }
    }

    private fun initUI() {
        with(binding) {
            sfMoment.setOnRefreshListener {
                sfMoment.isRefreshing = false
                viewModel?.getAllMomentWithPaging()?.observe(viewLifecycleOwner) { result ->
                    momentListAdapter.submitData(lifecycle, result)
                }
            }
            btAdd.setOnClickListener {
                checkingLocationPermission(GoToConstant.MOMENT_CREATE)
            }
            btNearby.setOnClickListener {
                checkingLocationPermission(GoToConstant.NEARBY)
            }

            rvMoment.adapter = momentListAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    momentListAdapter.retry()
                }
            )
        }
    }

    private fun onItemClicked(momentListStoryResponse: MomentListStoryResponse) {
        val direction = MomentListFragmentDirections.actionToMomentDetailFragment(
            data = momentListStoryResponse
        )
        findNavController().navigateSlideHorizontal(direction)
    }

    private fun checkingLocationPermission(goToPage: Int) {
        if (!requireActivity().checkLocationPermission()) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            val direction = if (goToPage == GoToConstant.NEARBY) {
                MomentListFragmentDirections.actionToNearByFragment()
            } else {
                MomentListFragmentDirections.actionToMomentCreateFragment()
            }
            findNavController().navigateSlideHorizontal(direction)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                val direction = MomentListFragmentDirections.actionToNearByFragment()
                findNavController().navigateSlideHorizontal(direction)
            }
        }
}