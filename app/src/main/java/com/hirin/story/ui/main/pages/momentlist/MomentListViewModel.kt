package com.hirin.story.ui.main.pages.momentlist

import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.data.moment.response.MomentListResponse
import com.hirin.story.domain.moment.MomentListUseCase
import com.hirin.story.ui.base.BaseViewModel
import com.hirin.story.utils.PostLiveData
import kotlinx.coroutines.launch

class MomentListViewModel(
    private val momentListUseCase: MomentListUseCase
) : BaseViewModel() {
    internal val momentListLiveData = PostLiveData<MomentListResponse?>()

    fun getAllMoment(
        page: Int,
        size: Int,
        location: Int
    ) {
        viewModelScope.launch {
            showLoadingWidget()
            when (val response = momentListUseCase(
                page, size, location
            )) {
                is NetworkResponse.Success -> {
                    momentListLiveData.postValue(response.body)
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