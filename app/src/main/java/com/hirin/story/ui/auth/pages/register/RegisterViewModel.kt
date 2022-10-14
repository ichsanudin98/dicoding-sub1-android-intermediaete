package com.hirin.story.ui.auth.pages.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import com.hirin.story.R
import com.hirin.story.data.BasicResponse
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.domain.user.RegisterUseCase
import com.hirin.story.ui.base.BaseViewModel
import com.hirin.story.utils.PostLiveData
import com.hirin.story.utils.validation.NotSameInputValidationRule
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : BaseViewModel() {
    internal val registerLiveData = PostLiveData<BasicResponse?>()
    internal val passwordConfirmValidationLiveData = MutableLiveData<Int?>()

    fun isValidatePasswordConfirm(password: String, newPassword: String): Boolean {
        val validate = when {
            !NotSameInputValidationRule(newPassword).validate(password) -> R.string.error_not_same_password
            else -> null
        }

        passwordConfirmValidationLiveData.postValue(validate)
        return validate == null
    }

    fun register(
        name: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            showLoadingWidget()
            when (val response = registerUseCase(
                name,
                email,
                password
            )) {
                is NetworkResponse.Success -> {
                    registerLiveData.postValue(response.body)
                }
                is NetworkResponse.ServerError -> {
                    genericErrorLiveData.value = response.body
                }
                is NetworkResponse.NetworkError -> {
                    networkErrorLiveData.value = response.error
                }
                is NetworkResponse.UnknownError -> {
                    genericErrorLiveData.value = GenericErrorResponse.generalError()
                }
            }
            hideLoadingWidget()
        }
    }
}