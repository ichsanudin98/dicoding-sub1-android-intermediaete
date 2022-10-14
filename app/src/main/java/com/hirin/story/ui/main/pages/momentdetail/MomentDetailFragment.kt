package com.hirin.story.ui.main.pages.momentdetail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import coil.load
import com.hirin.story.R
import com.hirin.story.databinding.FragmentMomentDetailBinding
import com.hirin.story.ui.base.BaseFragment
import com.hirin.story.ui.main.MainActivity
import com.hirin.story.utils.extension.convertDate

class MomentDetailFragment : BaseFragment<FragmentMomentDetailBinding>() {
    // <editor-fold defaultstate="collapsed" desc="initialize data">
    private val args by navArgs<MomentDetailFragmentArgs>()
    // </editor-fold>

    override fun getViewBinding(): FragmentMomentDetailBinding =
        FragmentMomentDetailBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        with(binding) {
            (requireActivity() as MainActivity).supportActionBar?.title = args.data.name

            binding.tbDate.text = args.data.createdAt.convertDate("yyyy-MM-dd'T'HH:mm:ss.SSSX",
                args.data.createdAt,
                resources.getString(R.string.title_latest),
                resources.getString(R.string.title_minute),
                resources.getString(R.string.title_hour),
                resources.getString(R.string.title_day),
                resources.getString(R.string.title_ago))
            binding.tbDescription.text = args.data.description
            binding.igMoment.load(args.data.photoUrl)
        }
    }
}