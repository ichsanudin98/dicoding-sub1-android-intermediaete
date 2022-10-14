package com.hirin.story.ui.auth.pages.login

import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.data.user.response.LoginResponse
import com.hirin.story.domain.user.LoginUseCase
import com.hirin.story.ui.base.BaseViewModel
import com.hirin.story.utils.PostLiveData
import com.hirin.story.utils.SharedPreferencesUtil
import com.hirin.story.utils.constant.type.SharedPrefsKeyEnum
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {
    internal val loginLiveData = PostLiveData<LoginResponse?>()

    fun login(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            showLoadingWidget()
            when (val response = loginUseCase(
                email, password
            )) {
                is NetworkResponse.Success -> {
                    response.body.result?.let {
                        SharedPreferencesUtil.put(SharedPrefsKeyEnum.USER_ID.name, it.userId)
                        SharedPreferencesUtil.put(SharedPrefsKeyEnum.NAME.name, it.name)
                        SharedPreferencesUtil.put(SharedPrefsKeyEnum.TOKEN.name, it.token)
                    }
                    loginLiveData.postValue(response.body)
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