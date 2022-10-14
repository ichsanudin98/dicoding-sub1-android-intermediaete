package com.hirin.story.ui.main.pages.momentlist

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.hirin.story.R
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.data.moment.response.MomentListResponse
import com.hirin.story.data.moment.response.MomentListStoryResponse
import com.hirin.story.databinding.FragmentMomentListBinding
import com.hirin.story.ui.base.BaseFragment
import com.hirin.story.ui.main.MainActivity
import com.hirin.story.utils.extension.navigateSlideHorizontal
import com.hirin.story.utils.extension.observeNonNull
import org.koin.androidx.viewmodel.ext.android.viewModel

class MomentListFragment : BaseFragment<FragmentMomentListBinding>() {
    // <editor-fold defaultstate="collapsed" desc="initialize data">
    private val viewModel: MomentListViewModel by viewModel()
    private val momentListAdapter by lazy {
        MomentListAdapter(emptyList(), ::onItemClicked,
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
            it.momentListLiveData.observeNonNull(viewLifecycleOwner, ::handleSuccessGetData)
            it.genericErrorLiveData.observeNonNull(viewLifecycleOwner, ::handleErrorGetData)
            it.networkErrorLiveData.observeNonNull(viewLifecycleOwner, ::handleNetworkError)
            it.loadingWidgetLiveData.observeNonNull(viewLifecycleOwner, ::handleLoadingWidget)

            it.getAllMoment(page, totalSize, 0)
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
                viewModel?.getAllMoment(page, totalSize, 0)
            }
            btAdd.setOnClickListener {
                val direction = MomentListFragmentDirections.actionToMomentCreateFragment()
                findNavController().navigateSlideHorizontal(direction)
            }

            rvMoment.adapter = momentListAdapter
        }
    }

    private fun handleSuccessGetData(response: MomentListResponse) {
        if (response.listStory.isEmpty()) {
//            binding.ltEmpty.visibility = View.VISIBLE
            binding.rvMoment.visibility = View.GONE
        } else {
            momentListAdapter.items = response.listStory

//            binding.ltEmpty.visibility = View.GONE
            binding.rvMoment.visibility = View.VISIBLE
        }
    }

    private fun handleErrorGetData(response: GenericErrorResponse) {
        handleGenericError(response)
    }

    private fun onItemClicked(momentListStoryResponse: MomentListStoryResponse) {
        val direction = MomentListFragmentDirections.actionToMomentDetailFragment(
            data = momentListStoryResponse
        )
        findNavController().navigateSlideHorizontal(direction)
    }
}