package com.hirin.story.ui.main.pages.momentlist

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.hirin.story.domain.moment.MomentListWithPagingUseCase
import com.hirin.story.ui.base.BaseViewModel

class MomentListViewModel(
    private val momentListWithPagingUseCase: MomentListWithPagingUseCase
) : BaseViewModel() {
    fun getAllMomentWithPaging() = momentListWithPagingUseCase().cachedIn(viewModelScope)
}