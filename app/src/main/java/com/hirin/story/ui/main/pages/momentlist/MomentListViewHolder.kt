package com.hirin.story.ui.main.pages.momentlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hirin.story.R
import com.hirin.story.data.moment.response.MomentListStoryResponse
import com.hirin.story.databinding.MomentListItemBinding
import com.hirin.story.utils.extension.convertDate
import com.hirin.story.utils.extension.getFirstLetterWords

class MomentListViewHolder(
    val binding: MomentListItemBinding,
    private val onItemClick: (MomentListStoryResponse) -> Unit,
    private val latestTitle: String,
    private val minuteTitle: String,
    private val hourTitle: String,
    private val dayTitle: String,
    private val agoTitle: String
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: MomentListStoryResponse) {
        binding.apply {
            tbDescription.text = item.description
            tbName.text = item.name
            tbInitial.text = item.name.getFirstLetterWords()
            tbDate.text = item.createdAt.convertDate("yyyy-MM-dd'T'HH:mm:ss.SSSX",
            item.createdAt, latestTitle, minuteTitle, hourTitle, dayTitle, agoTitle)
            igMoment.load(item.photoUrl)

            root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClicked: (MomentListStoryResponse) -> Unit,
            latestTitle: String, minuteTitle: String, hourTitle: String,
            dayTitle: String, agoTitle: String
        ): MomentListViewHolder {
            val view = DataBindingUtil
                .inflate<MomentListItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.moment_list_item,
                    parent, false
                )
            return MomentListViewHolder(view, onItemClicked, latestTitle, minuteTitle, hourTitle, dayTitle, agoTitle)
        }
    }
}