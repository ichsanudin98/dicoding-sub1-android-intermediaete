package com.hirin.story.ui.auth.pages.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import com.hirin.story.R
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.data.user.response.LoginResponse
import com.hirin.story.databinding.FragmentLoginBinding
import com.hirin.story.ui.base.BaseFragment
import com.hirin.story.ui.main.MainActivity
import com.hirin.story.utils.EditTextUtils
import com.hirin.story.utils.customview.InputTextView
import com.hirin.story.utils.extension.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    // <editor-fold defaultstate="collapsed" desc="initialize ui">
    private lateinit var emailColumn: InputTextView
    private lateinit var passwordColumn: InputTextView
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="initialize data">
    private val viewModel: LoginViewModel by viewModel()
    // </editor-fold>

    override fun getViewBinding(): FragmentLoginBinding =
        FragmentLoginBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        this.binding.viewModel = viewModel.also {
            it.loginLiveData.observeNonNull(viewLifecycleOwner, ::handleSuccessLogin)
            it.genericErrorLiveData.observeNonNull(viewLifecycleOwner, ::handleErrorLogin)
            it.networkErrorLiveData.observeNonNull(viewLifecycleOwner, ::handleNetworkError)
            it.loadingWidgetLiveData.observeNonNull(viewLifecycleOwner, ::handleLoadingWidget)
        }
    }

    private fun initUI() {
        with(binding) {
            emailColumn = InputTextView(requireContext(), fbEmail.root).apply {
                title().text = resources.getString(R.string.title_email)
                value().run {
                    inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    typeface = Typeface.DEFAULT
                    imeOptions = EditorInfo.IME_ACTION_NEXT
                    setOnFocusChangeListener { _, hasFocus ->
                        if (hasFocus) showFocusedState() else {
                            if (value().text.isNotEmpty()) if (isValidation(emailColumn)) showUnfocusedState()
                        }
                    }
                }
                setMarginEndDrawableEnd(resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._10sdp), clearIcon())
            }
            passwordColumn = InputTextView(requireContext(), fbPassword.root).apply {
                title().text = resources.getString(R.string.title_password)
                value().run {
                    hint = resources.getString(R.string.error_min_length_6)
                    inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                    typeface = Typeface.DEFAULT
                    transformationMethod = null
                    imeOptions = EditorInfo.IME_ACTION_DONE
                    EditTextUtils.disableCopyPasteOperations(this)
                    setOnFocusChangeListener { _, hasFocus ->
                        if (hasFocus) showFocusedState() else {
                            if (value().text.isNotEmpty()) if (isValidation(passwordColumn)) showUnfocusedState()
                        }
                    }
                    setOnTouchListener { _, motionEvent ->
                        if (motionEvent.action == MotionEvent.ACTION_UP && handleRightDrawableTouch(
                                motionEvent)
                        ) showTogglePassword()
                        false
                    }
                }
                setMarginEndDrawableEnd(resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._30sdp), clearIcon())
            }
            with(emailColumn) {
                setTextBehaviour({
                    clearIcon().isVisible = value().text.isNotEmpty()
                    btLogin.isEnabled = checkingIsValid()
                }, {})
                clearIcon().setOnClickListener { this.value().setText("") }
            }
            with(passwordColumn) {
                showTogglePassword()
                setTextBehaviour({
                    clearIcon().isVisible = value().text.isNotEmpty()
                    btLogin.isEnabled = checkingIsValid()
                }, {})
                clearIcon().setOnClickListener { this.value().setText("") }
            }

            btLogin.setOnClickListener {
                hideKeyboard()
                this@LoginFragment.viewModel.login(
                    email = emailColumn.value().text.toString(),
                    password = passwordColumn.value().text.toString().md5()
                )
            }

            val head = ObjectAnimator.ofFloat(tbHead, View.ALPHA, 1F).setDuration(500)
            val email = ObjectAnimator.ofFloat(fbEmail.root, View.ALPHA, 1F).setDuration(500)
            val password = ObjectAnimator.ofFloat(fbPassword.root, View.ALPHA, 1F).setDuration(500)
            val login = ObjectAnimator.ofFloat(btLogin, View.ALPHA, 1F).setDuration(500)
            AnimatorSet().apply {
                playSequentially(head, email, password, login)
                start()
            }
        }
    }

    private fun handleSuccessLogin(response: LoginResponse) {
        showSuccessSnackbar(response.message)
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finishAffinity()
    }

    private fun handleErrorLogin(response: GenericErrorResponse) {
        handleGenericError(response)
    }

    private fun checkingIsValid() = isValidation(emailColumn) && isValidation(passwordColumn)

    private fun isValidation(inputTextView: InputTextView): Boolean =
        when (inputTextView) {
            passwordColumn -> {
                passwordColumn.validation.invoke()
            }
            emailColumn -> {
                emailColumn.validation.invoke()
            }
            else -> { true }
        }
}