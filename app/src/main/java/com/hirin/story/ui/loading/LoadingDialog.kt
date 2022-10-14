package com.hirin.story.ui.loading

import android.os.Bundle
import android.view.View
import com.hirin.story.databinding.FragmentLoadingDialogBinding
import com.hirin.story.ui.base.BaseDialog

class LoadingDialog : BaseDialog<FragmentLoadingDialogBinding>() {
    companion object {
        fun newInstance() = LoadingDialog()
    }

    override fun getViewBinding(): FragmentLoadingDialogBinding =
        FragmentLoadingDialogBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        isCancelable = false
    }
}