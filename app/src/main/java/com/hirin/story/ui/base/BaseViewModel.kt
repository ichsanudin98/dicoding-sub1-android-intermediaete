package com.hirin.story.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hirin.story.data.GenericErrorResponse
import com.hirin.story.utils.PostLiveData
import com.hirin.story.utils.SnackBarData
import kotlinx.coroutines.launch
import java.io.IOException

open class BaseViewModel: ViewModel() {
    val genericErrorLiveData = PostLiveData<GenericErrorResponse?>()
    val networkErrorLiveData = PostLiveData<IOException?>()
    val loadingWidgetLiveData = PostLiveData<Boolean?>()
    val snackBar = PostLiveData<SnackBarData?>()

    fun showLoadingWidget(isPost:Boolean = false) {
        viewModelScope.launch {
            if(isPost) {
                loadingWidgetLiveData.postValue(true)
            } else {
                loadingWidgetLiveData.value = true
            }
        }
    }

    fun hideLoadingWidget(isPost:Boolean = false) {
        viewModelScope.launch {
            if(isPost){
                loadingWidgetLiveData.postValue(false)
            } else {
                loadingWidgetLiveData.value = false
            }
        }
    }

    fun showSnackBar(snackBarData: SnackBarData) {
        viewModelScope.launch {
            snackBar.value = snackBarData
        }
    }
}