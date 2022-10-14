package com.hirin.story.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.hirin.story.R
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.ui.loading.LoadingDialog
import com.hirin.story.utils.extension.*
import java.io.IOException

abstract class BaseFragment<VB : ViewBinding>(
    val pagePermission: Array<String> = arrayOf()
) : Fragment() {
    lateinit var requestMultiplePermissions: ActivityResultLauncher<Array<String>>
    open var pagePermissions: Array<String> = arrayOf()

    var loadingDialog: LoadingDialog? = null

    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun getViewBinding(): VB

    open fun onBackPressed() {
        //Method For Back pressed event
        if (isSafe()) {
            if (isMomentListFragment()) {
                activity?.minimizeApp()
            } else {
                findNavController().popBackStack()
            }
        }
    }

    private fun showLoadingWidget() {
        loadingDialog = LoadingDialog.newInstance()
        loadingDialog?.show(childFragmentManager, "LOADING_DIALOG")
    }

    private fun hideLoadingWidget() {
        if (loadingDialog?.isSafe() == true) loadingDialog?.dismiss()
    }

    fun handleLoadingWidget(isShow: Boolean) {
        if (!isSafe()) return
        if (isShow) showLoadingWidget() else hideLoadingWidget()
    }

    open fun handleGenericError(response: GenericErrorResponse) {
        response.messageRes?.let {
            showErrorSnackbar(resources.getString(it).orDash())
        } ?: run { showErrorSnackbar(response.message.orDash()) }
    }

    fun handleNetworkError(exception: IOException) {
        showErrorSnackbar(resources.getString(R.string.error_network_message))
    }
}