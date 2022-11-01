package com.hirin.story.ui.main.pages.momentlist

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.hirin.story.data.moment.response.MomentListStoryResponse

class MomentListPagingAdapter(
    private val onItemClick: (MomentListStoryResponse) -> Unit,
    private val latestTitle: String,
    private val minuteTitle: String,
    private val hourTitle: String,
    private val dayTitle: String,
    private val agoTitle: String
): PagingDataAdapter<MomentListStoryResponse, MomentListViewHolder>(diffUtils) {
    companion object {
        private val diffUtils = object: DiffUtil.ItemCallback<MomentListStoryResponse>() {
            override fun areItemsTheSame(
                oldItem: MomentListStoryResponse,
                newItem: MomentListStoryResponse
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: MomentListStoryResponse,
                newItem: MomentListStoryResponse
            ): Boolean = oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MomentListViewHolder {
        return MomentListViewHolder.create(parent, onItemClick, latestTitle, minuteTitle, hourTitle, dayTitle, agoTitle)
    }

    override fun onBindViewHolder(holder: MomentListViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}