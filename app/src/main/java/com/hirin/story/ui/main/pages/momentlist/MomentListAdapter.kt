package com.hirin.story.ui.main.pages.momentlist

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hirin.story.data.moment.response.MomentListStoryResponse

class MomentListAdapter(
    _items: List<MomentListStoryResponse>,
    private val onItemClick: (MomentListStoryResponse) -> Unit,
    private val latestTitle: String,
    private val minuteTitle: String,
    private val hourTitle: String,
    private val dayTitle: String,
    private val agoTitle: String
): RecyclerView.Adapter<MomentListViewHolder>() {
    var items = _items
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MomentListViewHolder {
        return MomentListViewHolder.create(parent, onItemClick, latestTitle, minuteTitle, hourTitle, dayTitle, agoTitle)
    }

    override fun onBindViewHolder(holder: MomentListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}