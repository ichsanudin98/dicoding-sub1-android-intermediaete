package com.hirin.story.ui.main.pages.momentcreate

import com.haroldadmin.cnradapter.NetworkResponse
import com.hirin.story.data.BasicResponse
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.domain.moment.MomentCreateUseCase
import com.hirin.story.ui.base.BaseViewModel
import com.hirin.story.utils.PostLiveData
import java.io.File

class MomentCreateViewModel(
    private val momentCreateUseCase: MomentCreateUseCase
) : BaseViewModel() {
    internal val momentCreateLiveData = PostLiveData<BasicResponse?>()

    suspend fun createMoment(
        photoFile: File,
        description: String,
        latitude: String?,
        longitude: String?
    ) {
        showLoadingWidget()
        when (val response = momentCreateUseCase(
            photoFile, description, latitude, longitude
        )) {
            is NetworkResponse.Success -> {
                momentCreateLiveData.postValue(response.body)
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