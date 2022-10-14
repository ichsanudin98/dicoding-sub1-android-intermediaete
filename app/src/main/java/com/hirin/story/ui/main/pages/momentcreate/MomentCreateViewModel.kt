package com.hirin.story.ui.main.pages.momentcreate

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import com.hirin.story.data.BasicResponse
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.domain.moment.MomentCreateUseCase
import com.hirin.story.ui.base.BaseViewModel
import com.hirin.story.utils.PostLiveData
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.launch
import java.io.File

class MomentCreateViewModel(
    private val context: Context,
    private val momentCreateUseCase: MomentCreateUseCase
) : BaseViewModel() {
    internal val momentCreateLiveData = PostLiveData<BasicResponse?>()

    fun create(
        photoFile: File,
        description: String,
        latitude: String?,
        longitude: String?
    ) {
        viewModelScope.launch {
            showLoadingWidget()
            val file = Compressor.compress(context, photoFile) {
                size(1_000_000)
            }
            when (val response = momentCreateUseCase(
                file, description, latitude, longitude
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
}