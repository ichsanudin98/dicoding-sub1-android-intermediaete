package com.hirin.story.utils.extension

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.hirin.story.R
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.ui.auth.AuthActivity
import com.hirin.story.ui.main.MainActivity
import com.hirin.story.ui.main.pages.momentlist.MomentListFragment
import com.hirin.story.utils.SnackBarData
import com.hirin.story.utils.constant.type.SnackBarEnum

fun Fragment.isSafe(): Boolean {
    return !(this.isRemoving || this.activity == null || this.isDetached || !this.isAdded || this.view == null)
}

fun Fragment.isMomentListFragment() = this is MomentListFragment

fun Fragment.showSuccessSnackbar(message: String) {
    (activity as? MainActivity)?.showToast(message, R.drawable.bg_snackbar_success)
    (activity as? AuthActivity)?.showToast(message, R.drawable.bg_snackbar_success)
}

fun Fragment.showWarningSnackbar(message: String) {
    (activity as? MainActivity)?.showToast(message, R.drawable.bg_snackbar_warning)
    (activity as? AuthActivity)?.showToast(message, R.drawable.bg_snackbar_warning)
}

fun Fragment.showNeutralSnackbar(message: String) {
    (activity as? MainActivity)?.showToast(message, R.drawable.bg_snackbar_neutral)
    (activity as? AuthActivity)?.showToast(message, R.drawable.bg_snackbar_neutral)
}

fun Fragment.showErrorSnackbar(message: String) {
    (activity as? MainActivity)?.showToast(message, R.drawable.bg_snackbar_danger)
    (activity as? AuthActivity)?.showToast(message, R.drawable.bg_snackbar_danger)
}

fun Fragment.showErrorSnackbar(error: GenericErrorResponse) {
    showErrorSnackbar(error.message)
}

fun Fragment.showSuccessSnackbar(message: Int) {
    showSuccessSnackbar(getString(message))
}

fun Fragment.showWarningSnackbar(message: Int) {
    showWarningSnackbar(getString(message))
}

fun Fragment.showNeutralSnackbar(message: Int) {
    showNeutralSnackbar(getString(message))
}

fun Fragment.showErrorSnackbar(message: Int) {
    showErrorSnackbar(getString(message))
}

fun Fragment.showSnackBar(data: SnackBarData) {
    when (data.type) {
        SnackBarEnum.WARNING -> when (data.messageRes) {
            null -> showWarningSnackbar(data.messageStr)
            else -> showWarningSnackbar(data.messageRes)
        }
        SnackBarEnum.SUCCESS -> when (data.messageRes) {
            null -> showSuccessSnackbar(data.messageStr)
            else -> showSuccessSnackbar(data.messageRes)
        }
        SnackBarEnum.ERROR -> when (data.messageRes) {
            null -> showErrorSnackbar(data.messageStr)
            else -> showErrorSnackbar(data.messageRes)
        }
        SnackBarEnum.INFO -> when (data.messageRes) {
            null -> showNeutralSnackbar(data.messageStr)
            else -> showNeutralSnackbar(data.messageRes)
        }
        else -> when (data.messageRes) {
            null -> showNeutralSnackbar(data.messageStr)
            else -> showNeutralSnackbar(data.messageRes)
        }
    }
}

fun Fragment.hideKeyboard() {
    val currentFocus = if (this is DialogFragment) dialog?.currentFocus else activity?.currentFocus
    val imm = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Fragment.showKeyboard(focusView: View) {
    if (!isSafe()) return
    if (focusView.isEnabled) {
        focusView.requestFocus()
        try {
            val imm =
                requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(focusView.findFocus(), InputMethodManager.SHOW_IMPLICIT)
        } catch (e: Exception) {
            //Don't do anything
        }
    }
}