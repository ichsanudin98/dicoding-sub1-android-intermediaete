package com.hirin.story.utils.customview

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.transition.TransitionManager
import com.hirin.story.R
import com.hirin.story.utils.validation.EmailValidationRule
import com.hirin.story.utils.validation.MinCharacterValidationRule
import com.hirin.story.utils.validation.NotBlankValidationRule

open class InputTextView @JvmOverloads constructor(
    context: Context, containerView: View, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private var containerView: View = containerView

    fun title(): TextView = containerView.findViewById(R.id.tbTitle)

    open fun value(): EditText = containerView.findViewById(R.id.fbValue)

    fun underline(): View = containerView.findViewById(R.id.underline)

    fun imgChecklist(): ImageView = containerView.findViewById(R.id.igChecklist)

    fun message(): TextView = containerView.findViewById(R.id.tbMessage)

    fun buttonPaste(): TextView = containerView.findViewById(R.id.tbPaste)

    fun clearIcon(): ImageView = containerView.findViewById(R.id.igClear)

    init {
        showUnfocusedState()
    }

    fun showFocusedState() = containerView.run {
        underline().background = ContextCompat.getDrawable(context, R.color.black)
        message().text = ""
        message().setTextColor(ContextCompat.getColor(context, R.color.paleGreyTwo))
        message().isInvisible = true
        animateUnderline()
    }

    private fun showFocusedStateWithoutAnimation() = containerView.run {
        underline().background = ContextCompat.getDrawable(context, R.color.black)
        message().text = ""
        message().setTextColor(ContextCompat.getColor(context, R.color.paleGreyTwo))
        message().isInvisible = true
    }

    fun showUnfocusedState() = containerView.run {
        underline().background = ContextCompat.getDrawable(context, R.color.black)
        message().text = ""
        message().setTextColor(ContextCompat.getColor(context, R.color.paleGreyTwo))
        message().isInvisible = true
        resetUnderline()
    }

    fun showErrorState(message: String?) = containerView.run {
        if (message != null) {
            underline().background = ContextCompat.getDrawable(context, R.color.tomato)
            message().text = message
            message().setTextColor(ContextCompat.getColor(context, R.color.tomato))
            message().isInvisible = false
        } else showFocusedStateWithoutAnimation()
    }

    fun animateUnderline() {
        val view = containerView as? ConstraintLayout ?: return

        resetUnderline()
        containerView.post {
            val endSet = ConstraintSet()
            endSet.clone(view)
            endSet.connect(
                underline().id,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START
            )
            endSet.connect(
                underline().id,
                ConstraintSet.END,
                ConstraintSet.PARENT_ID,
                ConstraintSet.END
            )

            TransitionManager.beginDelayedTransition(view)
            endSet.applyTo(view)
        }
    }

    private fun resetUnderline() {
        val view = containerView as? ConstraintLayout ?: return

        val startSet = ConstraintSet()
        startSet.clone(view)
        startSet.connect(
            underline().id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START
        )
        startSet.clear(underline().id, ConstraintSet.END)
        startSet.applyTo(view)
    }

    open var phoneTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            actionAfterTextChanged.invoke()
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            //Code to implement in the future
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            actionOnTextChanged.invoke()
        }
    }

    var validation: () -> Boolean = { validationValueBasedOnInputType() }
    lateinit var actionAfterTextChanged: () -> Unit
    lateinit var actionPastePhoneNumber: () -> Unit
    lateinit var actionOnTextChanged: () -> Unit

    fun setTextBehaviour(onAfterTextChanged: () -> Unit, onPastePhoneNumber: () -> Unit, onTextChanged: () -> Unit = {}) {
        this.actionAfterTextChanged = onAfterTextChanged
        this.actionPastePhoneNumber = onPastePhoneNumber
        this.actionOnTextChanged = onTextChanged
        value().addTextChangedListener(phoneTextWatcher)
    }

    fun setInputType(type: Int) {
        value().inputType = type
    }

    private fun validationValueBasedOnInputType() : Boolean {
        if (value().text.isEmpty() && !NotBlankValidationRule().validate(value().text.toString())) {
            showErrorState(resources.getString(R.string.error_required))
            return false
        } else {
            if (value().inputType == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS &&
                !EmailValidationRule().validate(value().text.toString())) {
                showErrorState(resources.getString(R.string.error_email_validation))
                return false
            }
            if (value().inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD &&
                !MinCharacterValidationRule(6).validate(value().text.toString())) {
                showErrorState(resources.getString(R.string.error_min_length_6))
                return false
            }
        }

        showErrorState(null)
        return true
    }

    fun showButtonPaste(isShow: Boolean) {
        buttonPaste().isVisible = isShow
    }

    fun showTogglePassword() {
        value().run {
            val passwordVisible = transformationMethod != null
            val eyeIconRes =
                if (passwordVisible) R.drawable.ic_show_eye_24_black else R.drawable.ic_hide_eye_24_black
            setCompoundDrawablesWithIntrinsicBounds(0, 0, eyeIconRes, 0)
            val selectionStart = selectionStart
            val selectionEnd = selectionEnd
            transformationMethod = PasswordTransformationMethod().takeIf { !passwordVisible }
            setSelection(selectionStart, selectionEnd)
        }
    }

    fun setMarginEndDrawableEnd(marginEnd: Int, imageView: ImageView) {
        val layoutParams = imageView.layoutParams as LayoutParams
        layoutParams.setMargins(0, 0,
            marginEnd, 0)
        imageView.layoutParams = layoutParams
    }
}