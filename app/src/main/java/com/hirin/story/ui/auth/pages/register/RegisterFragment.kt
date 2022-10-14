package com.hirin.story.ui.auth.pages.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.hirin.story.R
import com.hirin.story.data.BasicResponse
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.databinding.FragmentRegisterBinding
import com.hirin.story.ui.base.BaseFragment
import com.hirin.story.utils.EditTextUtils
import com.hirin.story.utils.customview.InputTextView
import com.hirin.story.utils.extension.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    // <editor-fold defaultstate="collapsed" desc="initialize ui">
    private lateinit var emailColumn: InputTextView
    private lateinit var nameColumn: InputTextView
    private lateinit var passwordColumn: InputTextView
    private lateinit var confirmationPasswordColumn: InputTextView
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="initialize data">
    private val viewModel: RegisterViewModel by viewModel()
    // </editor-fold>

    override fun getViewBinding(): FragmentRegisterBinding =
        FragmentRegisterBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        this.binding.viewModel = viewModel.also {
            it.registerLiveData.observeNonNull(viewLifecycleOwner, ::handleSuccessRegister)
            it.networkErrorLiveData.observeNonNull(viewLifecycleOwner, ::handleNetworkError)
            it.loadingWidgetLiveData.observeNonNull(viewLifecycleOwner, ::handleLoadingWidget)
            it.genericErrorLiveData.observeNonNull(viewLifecycleOwner, ::handleErrorRegister)
            it.passwordConfirmValidationLiveData.observe(viewLifecycleOwner, ::handleConfirmationPasswordValidation)
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
            nameColumn = InputTextView(requireContext(), fbName.root).apply {
                title().text = resources.getString(R.string.title_name)
                value().run {
                    inputType = InputType.TYPE_CLASS_TEXT
                    typeface = Typeface.DEFAULT
                    imeOptions = EditorInfo.IME_ACTION_NEXT
                    setOnFocusChangeListener { _, hasFocus ->
                        if (hasFocus) showFocusedState() else {
                            if (value().text.isNotEmpty()) if (isValidation(nameColumn)) showUnfocusedState()
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
                    imeOptions = EditorInfo.IME_ACTION_NEXT
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
            confirmationPasswordColumn = InputTextView(requireContext(), fbConfirmationPassword.root).apply {
                title().text = resources.getString(R.string.title_password_confirmation)
                value().run {
                    hint = resources.getString(R.string.error_min_length_6)
                    inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                    typeface = Typeface.DEFAULT
                    transformationMethod = null
                    imeOptions = EditorInfo.IME_ACTION_DONE
                    EditTextUtils.disableCopyPasteOperations(this)
                    setOnFocusChangeListener { _, hasFocus ->
                        if (hasFocus) showFocusedState() else {
                            if (value().text.isNotEmpty()) if (isValidation(confirmationPasswordColumn)) showUnfocusedState()
                        }
                    }
                    setOnTouchListener { _, motionEvent ->
                        if (motionEvent.action == MotionEvent.ACTION_UP && handleRightDrawableTouch(
                                motionEvent)
                        ) showTogglePassword()
                        false
                    }
                    setMarginEndDrawableEnd(resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._30sdp), clearIcon())
                }
            }

            with(emailColumn) {
                setTextBehaviour({
                    clearIcon().isVisible = value().text.isNotEmpty()
                    btRegister.isEnabled = checkingIsValid()
                }, {})
                clearIcon().setOnClickListener { this.value().setText("") }
            }
            with(nameColumn) {
                setTextBehaviour({
                    clearIcon().isVisible = value().text.isNotEmpty()
                    btRegister.isEnabled = checkingIsValid()
                }, {})
                clearIcon().setOnClickListener { this.value().setText("") }
            }
            with(passwordColumn) {
                showTogglePassword()
                setTextBehaviour({
                    clearIcon().isVisible = value().text.isNotEmpty()
                    btRegister.isEnabled = checkingIsValid()
                }, {})
                clearIcon().setOnClickListener { this.value().setText("") }
            }
            with(confirmationPasswordColumn) {
                showTogglePassword()
                setTextBehaviour({
                    clearIcon().isVisible = value().text.isNotEmpty()
                    btRegister.isEnabled = checkingIsValid()
                }, {})
                clearIcon().setOnClickListener { this.value().setText("") }
            }

            btRegister.setOnClickListener {
                hideKeyboard()
                this@RegisterFragment.viewModel.register(
                    name = nameColumn.value().text.toString(),
                    email = emailColumn.value().text.toString(),
                    password = confirmationPasswordColumn.value().text.toString().md5()
                )
            }

            val head = ObjectAnimator.ofFloat(tbHead, View.ALPHA, 1F).setDuration(500)
            val name = ObjectAnimator.ofFloat(fbName.root, View.ALPHA, 1F).setDuration(500)
            val email = ObjectAnimator.ofFloat(fbEmail.root, View.ALPHA, 1F).setDuration(500)
            val password = ObjectAnimator.ofFloat(fbPassword.root, View.ALPHA, 1F).setDuration(500)
            val confirmPassword = ObjectAnimator.ofFloat(fbConfirmationPassword.root, View.ALPHA, 1F).setDuration(500)
            val register = ObjectAnimator.ofFloat(btRegister, View.ALPHA, 1F).setDuration(500)
            AnimatorSet().apply {
                playSequentially(head, email, name, password, confirmPassword, register)
                start()
            }
        }
    }

    private fun handleSuccessRegister(response: BasicResponse) {
        showSuccessSnackbar(response.message)
        val direction = RegisterFragmentDirections.actionToLoginFragment()
        findNavController().navigateSlideHorizontal(direction, R.id.nav_welcome, true)
    }

    private fun handleErrorRegister(response: GenericErrorResponse) {
        handleGenericError(response)
    }

    private fun handleConfirmationPasswordValidation(message: Int?) = confirmationPasswordColumn.showErrorState(
        message?.let { getString(it) }
    )

    private fun checkingIsValid() = isValidation(nameColumn) && isValidation(emailColumn) && isValidation(passwordColumn) && isValidation(confirmationPasswordColumn)

    private fun isValidation(inputTextView: InputTextView): Boolean =
        when (inputTextView) {
            confirmationPasswordColumn -> {
                confirmationPasswordColumn.validation.invoke()
                viewModel.isValidatePasswordConfirm(
                    confirmationPasswordColumn.value().text.toString(), passwordColumn.value().text.toString()
                )
            }
            passwordColumn -> {
                passwordColumn.validation.invoke()
            }
            emailColumn -> {
                emailColumn.validation.invoke()
            }
            nameColumn -> {
                nameColumn.validation.invoke()
            }
            else -> { true }
        }
}